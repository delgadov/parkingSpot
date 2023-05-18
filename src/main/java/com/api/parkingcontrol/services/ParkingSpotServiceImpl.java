package com.api.parkingcontrol.services;

import com.api.parkingcontrol.dtos.ParkingSpotRequestDto;
import com.api.parkingcontrol.dtos.ParkingSpotResponseDto;
import com.api.parkingcontrol.mapper.ModelMapper;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class ParkingSpotServiceImpl implements ParkingSpotService {
    private final ParkingSpotRepository parkingSpotRepository;
    private final ModelMapper parkingSpotMapper;

    public ParkingSpotServiceImpl(ParkingSpotRepository parkingSpotRepository, ModelMapper parkingSpotModelMapper) {
        this.parkingSpotRepository = parkingSpotRepository;
        this.parkingSpotMapper = parkingSpotModelMapper;
    }

    @Override
    @Transactional
    public ParkingSpotResponseDto save(ParkingSpotRequestDto parkingSpotRequestDto) {
        if (parkingSpotRepository.existsByParkingSpotNumber(parkingSpotRequestDto.getParkingSpotNumber()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Parking Spot Number " + parkingSpotRequestDto.getParkingSpotNumber() + " already exists.");

        if (parkingSpotRepository.existsByLicensePlateCar(parkingSpotRequestDto.getLicensePlateCar())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "License Plate Car " + parkingSpotRequestDto.getLicensePlateCar() + " already exists.");
        }

        if (parkingSpotRepository.existsByApartmentAndBlock(parkingSpotRequestDto.getApartment(), parkingSpotRequestDto.getBlock())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Apartment " + parkingSpotRequestDto.getApartment() + " and " + parkingSpotRequestDto.getBlock() + " block already exists.");
        }

        ParkingSpotModel parkingSpotModel = parkingSpotMapper.toModel(parkingSpotRequestDto);
        ParkingSpotModel savedParkingSpot = parkingSpotRepository.save(parkingSpotModel);
        ParkingSpotResponseDto responseDto = parkingSpotMapper.toResponseDto(savedParkingSpot);

        return responseDto;
    }

    @Override
    public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
        return parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber);
    }

    @Override
    public boolean existsByLicensePlateCar(String licensePlateCar) {
        return parkingSpotRepository.existsByLicensePlateCar(licensePlateCar);
    }

    @Override
    public boolean existsByApartmentAndBlock(String apartment, String block) {
        return parkingSpotRepository.existsByApartmentAndBlock(apartment, block);
    }

    @Override
    public Page<ParkingSpotResponseDto> findAll(Pageable pageable) {
        Page<ParkingSpotModel> parkingSpotPage = parkingSpotRepository.findAll(pageable);
        return parkingSpotMapper.toPageResponseDto(parkingSpotPage);
    }

    @Override
    public ParkingSpotResponseDto findById(UUID id) {
        ParkingSpotModel parkingSpotModel = parkingSpotRepository.findById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Parking Service Spot by id: " + id));

        ParkingSpotResponseDto responseDto = parkingSpotMapper.toResponseDto(parkingSpotModel);

        return responseDto;
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        ParkingSpotModel parkingSpotModel = parkingSpotRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Parking Service Spot by id: " + id));
        parkingSpotRepository.deleteById(id);
    }

    @Override
    public ParkingSpotResponseDto update(UUID id, ParkingSpotRequestDto parkingSpotRequestDto) {
        ParkingSpotModel m = parkingSpotRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Parking Service Spot by id: " + id));

        ParkingSpotModel parkingSpotModel = parkingSpotMapper.toModel(parkingSpotRequestDto);
        parkingSpotModel.setId(id);

        parkingSpotRepository.save(parkingSpotModel);

        ParkingSpotResponseDto response = parkingSpotMapper.toResponseDto(parkingSpotModel);

        return response;
    }


}
