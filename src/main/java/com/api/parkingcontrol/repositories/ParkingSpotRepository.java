package com.api.parkingcontrol.repositories;

import com.api.parkingcontrol.models.ParkingSpotModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpotModel, UUID> {

    boolean existsByParkingSpotNumber(String parkingSpotNumber);

    boolean existsByLicensePlateCar(String licensePlateCar);

    boolean existsByApartmentAndBlock(String apartment, String block);

}

