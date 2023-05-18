package com.api.parkingcontrol.services;

import com.api.parkingcontrol.dtos.ParkingSpotRequestDto;
import com.api.parkingcontrol.dtos.ParkingSpotResponseDto;
import com.api.parkingcontrol.mapper.ModelMapper;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;
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
        parkingSpotModel = ParkingSpotModel.builder()
                .id(UUID.randomUUID())
                .parkingSpotNumber("16A")
                .licensePlateCar("0000000")
                .brandCar("Hyundai")
                .modelCar("Veloster")
                .registrationDate(LocalDateTime.now())
                .responsibleName("Anyone")
                .apartment("16")
                .block("A")
                .colorCar("Black")
                .build();
    }

    @DisplayName("Test: Find All Parking Spots")
    @Test
    public void testFindAllParkingSpots() {
        ParkingSpotModel model1 = parkingSpotModel;
        model1.setParkingSpotNumber("1");

        ParkingSpotModel model2 = parkingSpotModel;
        model2.setParkingSpotNumber("2");
        List<ParkingSpotModel> parkingSpotModelList = List.of(model1, model2);

        Pageable pageable = PageRequest.of(0, 10);

        List<ParkingSpotResponseDto> responseDtoList = parkingSpotModelList.stream()
                .map(modelMapper::toResponseDto)
                .toList();

        Page<ParkingSpotModel> page = new PageImpl<>(parkingSpotModelList, pageable, parkingSpotModelList.size());

        when(parkingSpotRepository.findAll(pageable))
                .thenReturn(page);

        Page<ParkingSpotResponseDto> expectedPage = new PageImpl<>(responseDtoList, pageable, responseDtoList.size());
        Page<ParkingSpotResponseDto> actualPage = parkingSpotService.findAll(pageable);

        assertNotNull(actualPage);
        assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements());
        assertEquals(expectedPage.getTotalPages(), actualPage.getTotalPages());
        assertEquals(expectedPage.getNumber(), actualPage.getNumber());
        assertEquals(expectedPage.getSize(), actualPage.getSize());

        for (int i = 0; i < expectedPage.getContent().size(); i++){
            ParkingSpotResponseDto expectedResponse = expectedPage.getContent().get(i);
            ParkingSpotResponseDto actualResponse = actualPage.getContent().get(i);

            assertEquals(expectedResponse, actualResponse);
        }
    }


    @DisplayName("Test: Find Parking Spot By ID (Existent ID)")
    @Test
    public void testFindParkingSpotByIdWithExistentId() {
        UUID id = parkingSpotModel.getId();

        when(parkingSpotRepository.findById(id))
                .thenReturn(Optional.of(parkingSpotModel));

        ParkingSpotResponseDto expectedResponse = modelMapper.toResponseDto(parkingSpotModel);
        ParkingSpotResponseDto actualResponse = parkingSpotService.findById(id);

        assertNotNull(actualResponse);
        assertEquals(actualResponse, expectedResponse);

        verify(parkingSpotRepository).findById(id);
    }

    @DisplayName("Test: Find Parking Spot By ID (Non-Existent ID)")
    @Test
    public void testFindParkingSpotByIdWithNonExistentId() {
        UUID noExistentId = UUID.randomUUID();

        when(parkingSpotRepository.findById(noExistentId))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> parkingSpotService.findById(noExistentId));

        verify(parkingSpotRepository).findById(noExistentId);
    }

    @DisplayName("Test: Save Parking Spot (New Parking Spot)")
    @Test
    public void testSaveNewParkingSpot() {

        when(parkingSpotRepository.save(any(ParkingSpotModel.class)))
                .thenReturn(parkingSpotModel);

        ParkingSpotRequestDto requestDto = modelMapper.toRequestDto(parkingSpotModel);
        ParkingSpotResponseDto responseDto = modelMapper.toResponseDto(parkingSpotModel);

        ParkingSpotResponseDto savedParkingSpot = parkingSpotService.save(requestDto);

        verify(parkingSpotRepository, times(1))
                .save(any(ParkingSpotModel.class));

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

    @DisplayName("Test: Save Parking Spot (Null Fields)")
    @Test
    public void testSaveParkingSpotWithNullFields() {
        ParkingSpotRequestDto requestDto = ParkingSpotRequestDto.builder()
                .parkingSpotNumber(null)
                .licensePlateCar(null)
                .brandCar(null)
                .modelCar(null)
                .responsibleName(null)
                .apartment(null)
                .block(null)
                .build();

        ParkingSpotResponseDto savedParkingSpot = parkingSpotService.save(requestDto);

        assertNull(savedParkingSpot);
    }

    @DisplayName("Test: Save Parking Spot (Exists By Parking Spot Number)")
    @Test
    public void testSaveParkingSpotWithExistingParkingSpotNumber() {
        String parkingSpotNumber = "85";

        ParkingSpotRequestDto requestDto = modelMapper.toRequestDto(parkingSpotModel);
        requestDto.setParkingSpotNumber(parkingSpotNumber);

        when(parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber))
                .thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> parkingSpotService.save(requestDto));
    }

    @DisplayName("Test: Save Parking Spot (Exists By License Plate Car)")
    @Test
    public void testSaveParkingSpotWithExistingLicensePlateCar() {
        String licensePlateCar = "0000000";

        ParkingSpotRequestDto requestDto = modelMapper.toRequestDto(parkingSpotModel);
        requestDto.setLicensePlateCar(licensePlateCar);

        when(parkingSpotRepository.existsByLicensePlateCar(licensePlateCar))
                .thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> parkingSpotService.save(requestDto));
    }

    @DisplayName("Test: Save - Exists by Apartment and Block")
    @Test
    public void testSaveExistsByApartmentAndBlock() {
        String apartment = "16";
        String block = "A";

        ParkingSpotRequestDto requestDto = modelMapper.toRequestDto(parkingSpotModel);
        requestDto.setApartment(apartment);
        requestDto.setBlock(block);

        when(parkingSpotRepository.existsByApartmentAndBlock(apartment, block))
                .thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> parkingSpotService.save(requestDto));
    }

    @DisplayName("Test: Delete - Existing ID")
    @Test
    public void testDeleteParkingSpotByID() {
        UUID id = parkingSpotModel.getId();

        when(parkingSpotRepository.findById(id))
                .thenReturn(Optional.of(parkingSpotModel));

        parkingSpotService.deleteById(id);

        verify(parkingSpotRepository, times(1)).deleteById(id);
    }

    @DisplayName("Test: Delete - Non-Existing ID")
    @Test
    public void testDeleteParkingSpotByNonExistingID() {
        UUID id = UUID.randomUUID();

        when(parkingSpotRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> parkingSpotService.deleteById(id));
    }

    @DisplayName("Test: Exists by Apartment and Block")
    @Test
    public void testExistsByApartmentAndBlock() {
        String apartment = "16";
        String block = "A";

        when(parkingSpotRepository.existsByApartmentAndBlock(apartment, block))
                .thenReturn(true);

        boolean existsByApartmentAndBlock = parkingSpotService.existsByApartmentAndBlock(apartment, block);

        verify(parkingSpotRepository, times(1))
                .existsByApartmentAndBlock(apartment, block);
        assertTrue(existsByApartmentAndBlock);
    }

    @DisplayName("Test: Exists by License Plate Car")
    @Test
    public void testExistsByLicensePlateCar() {
        String licensePlateCar = "0000000";

        when(parkingSpotRepository.existsByLicensePlateCar(licensePlateCar))
                .thenReturn(true);

        boolean existsByLicensePlateCar = parkingSpotService.existsByLicensePlateCar(licensePlateCar);

        verify(parkingSpotRepository, times(1))
                .existsByLicensePlateCar(licensePlateCar);
        assertTrue(existsByLicensePlateCar);
    }

    @DisplayName("Test: Exists by Parking Spot Number")
    @Test
    public void testExistsByParkingSpotNumber() {
        String parkingSpotNumber = "85";

        when(parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber))
                .thenReturn(true);

        boolean existsByParkingSpotNumber = parkingSpotService.existsByParkingSpotNumber(parkingSpotNumber);

        verify(parkingSpotRepository, times(1))
                .existsByParkingSpotNumber(parkingSpotNumber);
        assertTrue(existsByParkingSpotNumber);
    }

    @DisplayName("Test: Update Parking Spot")
    @Test
    public void testUpdateParkingSpot() {
        UUID id = parkingSpotModel.getId();
        parkingSpotModel.setApartment("85");

        when(parkingSpotRepository.findById(id))
                .thenReturn(Optional.of(parkingSpotModel));

        ParkingSpotRequestDto requestDto = modelMapper.toRequestDto(parkingSpotModel);
        ParkingSpotResponseDto responseDto = parkingSpotService.update(id, requestDto);

        verify(parkingSpotRepository, times(1))
                .save(any(ParkingSpotModel.class));

        assertEquals(responseDto.apartment(), requestDto.getApartment());
        assertEquals(responseDto.parkingSpotNumber(), requestDto.getParkingSpotNumber());
        assertEquals(responseDto.licensePlateCar(), requestDto.getLicensePlateCar());
        assertEquals(responseDto.brandCar(), requestDto.getBrandCar());
        assertEquals(responseDto.modelCar(), requestDto.getModelCar());
        assertEquals(responseDto.colorCar(), requestDto.getColorCar());
        assertEquals(responseDto.responsibleName(), requestDto.getResponsibleName());
        assertEquals(responseDto.block(), requestDto.getBlock());
        assertNotNull(responseDto.registrationDate());
    }

}