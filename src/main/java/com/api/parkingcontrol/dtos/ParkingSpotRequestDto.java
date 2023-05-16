package com.api.parkingcontrol.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ParkingSpotRequestDto {
    @NotBlank
    private String parkingSpotNumber;
    @NotBlank
    @Length(max = 7, min = 7, message = "License Plate car must have 7 chars")
    private String licensePlateCar;
    @NotBlank
    private String brandCar;
    @NotBlank
    private String modelCar;
    @NotBlank
    private String colorCar;
    @NotBlank
    private String responsibleName;
    @NotBlank
    private String apartment;
    @NotBlank
    private String block;
}
