package com.api.parkingcontrol.services;

import com.api.parkingcontrol.dtos.ParkingSpotRequestDto;
import com.api.parkingcontrol.dtos.ParkingSpotResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ParkingSpotService {

    ParkingSpotResponseDto save(ParkingSpotRequestDto parkingSpotModel);

    boolean existsByParkingSpotNumber(String parkingSpotNumber);

    boolean existsByLicensePlateCar(String licensePlateCar);

    boolean existsByApartmentAndBlock(String apartment, String block);

    Page<ParkingSpotResponseDto> findAll(Pageable pageable);

    ParkingSpotResponseDto findById(UUID uuid);

    void deleteById(UUID id);

    ParkingSpotResponseDto update(UUID id, ParkingSpotRequestDto parkingSpotRequestDto);

}
