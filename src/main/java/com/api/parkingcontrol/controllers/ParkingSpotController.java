package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.dtos.ParkingSpotRequestDto;
import com.api.parkingcontrol.dtos.ParkingSpotResponseDto;
import com.api.parkingcontrol.mapper.ModelMapper;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/rest/api/v1/parking-spot")
public class ParkingSpotController {
    private final ParkingSpotService parkingSpotService;
    private final ModelMapper parkingSpotMapper;

    public ParkingSpotController(ModelMapper parkingSpotMapper, ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
        this.parkingSpotMapper = parkingSpotMapper;
    }

    @PostMapping
    public ResponseEntity<ParkingSpotResponseDto> saveParkingSpot(@RequestBody @Valid ParkingSpotRequestDto parkingSpotRequestDto) {
        if (parkingSpotService.existsByParkingSpotNumber(parkingSpotRequestDto.getParkingSpotNumber()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Parking Spot Number " + parkingSpotRequestDto.getParkingSpotNumber() + " already exists.");

        if (parkingSpotService.existsByLicensePlateCar(parkingSpotRequestDto.getLicensePlateCar())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "License Plate Car " + parkingSpotRequestDto.getLicensePlateCar() + " already exists.");
        }

        if (parkingSpotService.existsByApartmentAndBlock(parkingSpotRequestDto.getApartment(), parkingSpotRequestDto.getBlock())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Apartment " + parkingSpotRequestDto.getApartment() + " and " + parkingSpotRequestDto.getBlock() + " block already exists.");
        }

        ParkingSpotModel parkingSpotModel = parkingSpotMapper.toModel(parkingSpotRequestDto);
        ParkingSpotModel savedParkingSpot = parkingSpotService.save(parkingSpotModel);
        ParkingSpotResponseDto responseDto = parkingSpotMapper.toResponseDto(savedParkingSpot);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<Page<ParkingSpotResponseDto>> getAllParkingSpots(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok().body(parkingSpotMapper.toPageResponseDto(parkingSpotService.findAll(pageable)));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ParkingSpotResponseDto> getParkingSpotById(@PathVariable("id") UUID id) {
        ParkingSpotModel parkingSpotModel = parkingSpotService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Parking Service Spot by id: " + id));

        ParkingSpotResponseDto responseDto = parkingSpotMapper.toResponseDto(parkingSpotModel);

        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParkingSpot(@PathVariable("id") UUID id) {
        ParkingSpotModel parkingSpotModel = parkingSpotService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Parking Service Spot by id: " + id));
        parkingSpotService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParkingSpotResponseDto> updateParkingSpot(@PathVariable("id") UUID id, @RequestBody @Valid ParkingSpotRequestDto parkingSpotRequestDto) {
        ParkingSpotModel m = parkingSpotService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Parking Service Spot by id: " + id));

        ParkingSpotModel parkingSpotModel = parkingSpotMapper.toModel(parkingSpotRequestDto);
        parkingSpotModel.setId(id);

        parkingSpotService.save(parkingSpotModel);

        ParkingSpotResponseDto response = parkingSpotMapper.toResponseDto(parkingSpotModel);

        return ResponseEntity.ok().body(response);
    }

}
