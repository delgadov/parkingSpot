package com.api.parkingcontrol.services;

import com.api.parkingcontrol.models.ParkingSpotModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingSpotServiceTest {

    @Mock
    private ParkingSpotService parkingSpotService;

    @DisplayName("Find all Parking Spots")
    @Test
    public void testFindAllParkingSpots() {
        List<ParkingSpotModel> parkingSpotModelList = List.of(
                ParkingSpotModel.builder()
                        .id(UUID.randomUUID())
                        .parkingSpotNumber("1")
                        .build(),
                ParkingSpotModel.builder()
                        .id(UUID.randomUUID())
                        .parkingSpotNumber("2")
                        .build()
        );

        Pageable pageable = PageRequest.of(0, 10);

        Page<ParkingSpotModel> page = new PageImpl<>(parkingSpotModelList, pageable, parkingSpotModelList.size());

        when(parkingSpotService.findAll(pageable))
                .thenReturn(page);

        Page<ParkingSpotModel> result = parkingSpotService.findAll(pageable);

        Assertions.assertEquals(page, result);
    }


    @DisplayName("Find Parking Spot By ID")
    @Test
    public void testFindParkingSpotById() {
        UUID id = UUID.randomUUID();
        ParkingSpotModel parkingSpotModel = ParkingSpotModel.builder()
                .id(id)
                .build();

        when(parkingSpotService.findById(id))
                .thenReturn(Optional.of(parkingSpotModel));

        Optional<ParkingSpotModel> optionalParkingSpotModel = parkingSpotService.findById(id);

        Assertions.assertTrue(optionalParkingSpotModel.isPresent());
        Assertions.assertEquals(parkingSpotModel, optionalParkingSpotModel.get());
    }

    @DisplayName("Save new Parking Spot")
    @Test
    public void testSaveParkingSpot() {
        ParkingSpotModel parkingSpotModel = ParkingSpotModel.builder()
                .id(UUID.randomUUID())
                .parkingSpotNumber("1")
                .build();

        when(parkingSpotService.save(parkingSpotModel))
                .thenReturn(parkingSpotModel);

        ParkingSpotModel savedParkingSpot = parkingSpotService.save(parkingSpotModel);

        verify(parkingSpotService, times(1))
                .save(savedParkingSpot);
        Assertions.assertEquals(parkingSpotModel, savedParkingSpot);
    }


    @DisplayName("Delete Parking Spot By ID")
    @Test
    public void testDeleteParkingSpotByID() {
        UUID id = UUID.randomUUID();

        parkingSpotService.deleteById(id);

        verify(parkingSpotService, times(1)).deleteById(id);
    }

    @DisplayName("Exists by Parking Spot Number")
    @Test
    public void testExistsByParkingSpotNumber(){
        String parkingSpotNumber = "1";

        when(parkingSpotService.existsByParkingSpotNumber(parkingSpotNumber))
                .thenReturn(true);

        boolean existsByParkingSpotNumber = parkingSpotService.existsByParkingSpotNumber(parkingSpotNumber);

        verify(parkingSpotService, times(1))
                .existsByParkingSpotNumber(parkingSpotNumber);
        Assertions.assertTrue(existsByParkingSpotNumber);
    }

    @DisplayName("Exists by Apartment and Block")
    @Test
    public void textExitsByApartmentAndBlock(){
        String apartment = "16";
        String block = "A";

        when(parkingSpotService.existsByApartmentAndBlock(apartment, block))
                .thenReturn(true);

        boolean existsByApartmentAndBlock = parkingSpotService.existsByApartmentAndBlock(apartment, block);

        verify(parkingSpotService, times(1))
                .existsByApartmentAndBlock(apartment, block);
        Assertions.assertTrue(existsByApartmentAndBlock);
    }

    @DisplayName("Exists by License Plate Car")
    @Test
    public void testExitsByLicensePlateCar(){
        String licensePlateCar = "0000000";

        when(parkingSpotService.existsByLicensePlateCar(licensePlateCar))
                .thenReturn(true);

        boolean existsByLicensePlateCar = parkingSpotService.existsByLicensePlateCar(licensePlateCar);

        verify(parkingSpotService, times(1))
                .existsByLicensePlateCar(licensePlateCar);
        Assertions.assertTrue(existsByLicensePlateCar);
    }

}