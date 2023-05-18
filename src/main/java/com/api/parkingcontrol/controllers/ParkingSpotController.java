package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.dtos.ParkingSpotRequestDto;
import com.api.parkingcontrol.dtos.ParkingSpotResponseDto;
import com.api.parkingcontrol.services.ParkingSpotServiceImpl;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/rest/api/v1/parking-spot")
public class ParkingSpotController {
    private final ParkingSpotServiceImpl parkingSpotService;


    public ParkingSpotController(ParkingSpotServiceImpl parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @PostMapping
    public ResponseEntity<ParkingSpotResponseDto> saveParkingSpot(@RequestBody @Valid ParkingSpotRequestDto parkingSpotRequestDto) {
        ParkingSpotResponseDto parkingSpotResponseDto = parkingSpotService.save(parkingSpotRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotResponseDto);
    }

    @GetMapping
    public ResponseEntity<Page<ParkingSpotResponseDto>> getAllParkingSpots(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ParkingSpotResponseDto> page = parkingSpotService.findAll(pageable);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ParkingSpotResponseDto> getParkingSpotById(@PathVariable("id") UUID id) {
        ParkingSpotResponseDto responseDto = parkingSpotService.findById(id);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParkingSpot(@PathVariable("id") UUID id) {
        parkingSpotService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParkingSpotResponseDto> updateParkingSpot(@PathVariable("id") UUID id, @RequestBody @Valid ParkingSpotRequestDto parkingSpotRequestDto) {
        ParkingSpotResponseDto responseDto = parkingSpotService.update(id, parkingSpotRequestDto);
        return ResponseEntity.ok().body(responseDto);
    }

}
