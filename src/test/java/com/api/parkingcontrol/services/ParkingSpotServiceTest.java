package com.api.parkingcontrol.services;

import com.api.parkingcontrol.dtos.ParkingSpotRequestDto;
import com.api.parkingcontrol.dtos.ParkingSpotResponseDto;
import com.api.parkingcontrol.mapper.ModelMapper;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingSpotServiceTest {

    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @InjectMocks
    private ParkingSpotServiceImpl parkingSpotService;

    @Spy
    private ModelMapper modelMapper = Mappers.getMapper(ModelMapper.class);

    private ParkingSpotModel parkingSpotModel;

    @BeforeEach
    public void setUp() {
        parkingSpotModel = ParkingSpotModel.builder().id(UUID.randomUUID()).parkingSpotNumber("16A").licensePlateCar("0000000").brandCar("Hyundai").modelCar("Veloster").registrationDate(LocalDateTime.now()).responsibleName("Anyone").apartment("16").block("A").build();
    }

    @DisplayName("Find all Parking Spots")
    @Test
    public void testFindAllParkingSpots() {
        ParkingSpotModel model1 = parkingSpotModel;
        model1.setParkingSpotNumber("1");

        ParkingSpotModel model2 = parkingSpotModel;
        model2.setParkingSpotNumber("2");
        List<ParkingSpotModel> parkingSpotModelList = List.of(model1, model2);

        Pageable pageable = PageRequest.of(0, 10);

        List<ParkingSpotResponseDto> responseDtoList = parkingSpotModelList.stream().map(modelMapper::toResponseDto).toList();

        Page<ParkingSpotModel> page = new PageImpl<>(parkingSpotModelList, pageable, parkingSpotModelList.size());

        when(parkingSpotRepository.findAll(pageable)).thenReturn(page);

        Page<ParkingSpotResponseDto> expectedPage = new PageImpl<>(responseDtoList, pageable, responseDtoList.size());
        Page<ParkingSpotResponseDto> actualPage = parkingSpotService.findAll(pageable);

        assertNotNull(actualPage);
        assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements());
        assertEquals(expectedPage.getTotalPages(), actualPage.getTotalPages());
        assertEquals(expectedPage.getNumber(), actualPage.getNumber());
        assertEquals(expectedPage.getSize(), actualPage.getSize());
        assertEquals(expectedPage.getContent(), actualPage.getContent());
    }


    @DisplayName("Find Parking Spot By ID -- Existent ID")
    @Test
    public void testFindParkingSpotById() {
        UUID id = parkingSpotModel.getId();

        when(parkingSpotRepository.findById(id)).thenReturn(Optional.of(parkingSpotModel));

        ParkingSpotResponseDto expectedResponse = modelMapper.toResponseDto(parkingSpotModel);
        ParkingSpotResponseDto actualResponse = parkingSpotService.findById(id);

        assertNotNull(actualResponse);
        assertEquals(actualResponse, expectedResponse);

        verify(parkingSpotRepository).findById(id);
    }

    @DisplayName("Find Parking Spot By ID -- Non Existent ID")
    @Test
    public void testFindParkingSpotByNonExistentId() {
        UUID noExistentId = UUID.randomUUID();

        when(parkingSpotRepository.findById(noExistentId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> parkingSpotService.findById(noExistentId));

        verify(parkingSpotRepository).findById(noExistentId);
    }

    @DisplayName("Save new Parking Spot")
    @Test
    public void testSaveParkingSpot() {

        when(parkingSpotRepository.save(any(ParkingSpotModel.class))).thenReturn(parkingSpotModel);

        ParkingSpotRequestDto requestDto = modelMapper.toRequestDto(parkingSpotModel);
        ParkingSpotResponseDto responseDto = modelMapper.toResponseDto(parkingSpotModel);

        ParkingSpotResponseDto savedParkingSpot = parkingSpotService.save(requestDto);

        verify(parkingSpotRepository, times(1)).save(any(ParkingSpotModel.class));

        assertEquals(responseDto.apartment(), savedParkingSpot.apartment());
        assertEquals(responseDto.parkingSpotNumber(), savedParkingSpot.parkingSpotNumber());
        assertEquals(responseDto.block(), savedParkingSpot.block());
        assertEquals(responseDto.responsibleName(), savedParkingSpot.responsibleName());
        assertEquals(responseDto.colorCar(), savedParkingSpot.colorCar());
        assertEquals(responseDto.brandCar(), savedParkingSpot.brandCar());
        assertEquals(responseDto.licensePlateCar(), savedParkingSpot.licensePlateCar());
        assertEquals(responseDto.modelCar(), savedParkingSpot.modelCar());
        assertEquals(responseDto.registrationDate(), savedParkingSpot.registrationDate());
    }

    @DisplayName("Delete Parking Spot By Existing ID")
    @Test
    public void testDeleteParkingSpotByID() {
        UUID id = parkingSpotModel.getId();

        when(parkingSpotRepository.findById(id)).thenReturn(Optional.of(parkingSpotModel));

        parkingSpotService.deleteById(id);

        verify(parkingSpotRepository, times(1)).deleteById(id);
    }

    @DisplayName("Delete Parking Spot By Non Existing ID")
    @Test
    public void testDeleteParkingSpotByNonExistingID() {
        UUID id = UUID.randomUUID();

        when(parkingSpotRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> parkingSpotService.deleteById(id));
    }

    @DisplayName("Exists by Parking Spot Number")
    @Test
    public void testExistsByParkingSpotNumber() {
        String parkingSpotNumber = "1";

        when(parkingSpotService.existsByParkingSpotNumber(parkingSpotNumber)).thenReturn(true);

        boolean existsByParkingSpotNumber = parkingSpotService.existsByParkingSpotNumber(parkingSpotNumber);

        verify(parkingSpotRepository, times(1)).existsByParkingSpotNumber(parkingSpotNumber);
        Assertions.assertTrue(existsByParkingSpotNumber);
    }

    @DisplayName("Exists by Apartment and Block")
    @Test
    public void textExitsByApartmentAndBlock() {
        String apartment = "16";
        String block = "A";

        when(parkingSpotRepository.existsByApartmentAndBlock(apartment, block)).thenReturn(true);

        boolean existsByApartmentAndBlock = parkingSpotService.existsByApartmentAndBlock(apartment, block);

        verify(parkingSpotRepository, times(1)).existsByApartmentAndBlock(apartment, block);
        Assertions.assertTrue(existsByApartmentAndBlock);
    }

    @DisplayName("Exists by License Plate Car")
    @Test
    public void testExitsByLicensePlateCar() {
        String licensePlateCar = "0000000";

        when(parkingSpotRepository.existsByLicensePlateCar(licensePlateCar)).thenReturn(true);

        boolean existsByLicensePlateCar = parkingSpotService.existsByLicensePlateCar(licensePlateCar);

        verify(parkingSpotRepository, times(1)).existsByLicensePlateCar(licensePlateCar);
        Assertions.assertTrue(existsByLicensePlateCar);
    }
}