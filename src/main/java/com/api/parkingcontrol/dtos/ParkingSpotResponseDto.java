package com.api.parkingcontrol.dtos;

import java.time.LocalDateTime;

public record ParkingSpotResponseDto(String parkingSpotNumber, String licensePlateCar, String brandCar, String modelCar,
                                     String colorCar, LocalDateTime registrationDate, String responsibleName,
                                     String apartment, String block) {
}
