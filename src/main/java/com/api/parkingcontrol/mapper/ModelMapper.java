package com.api.parkingcontrol.mapper;

import com.api.parkingcontrol.dtos.ParkingSpotRequestDto;
import com.api.parkingcontrol.dtos.ParkingSpotResponseDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Mapper (imports = {LocalDateTime.class, ZoneId.class}, componentModel = "spring")
public interface ModelMapper {

    ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);

    @Mapping(target = "registrationDate", expression = "java(LocalDateTime.now(ZoneId.of(\"UTC\")))")
    @Mapping(source = "licensePlateCar", target = "licensePlateCar")
    ParkingSpotModel toModel(ParkingSpotRequestDto parkingSpotRequestDto);

    @Mapping(source = "registrationDate", target = "registrationDate")
    ParkingSpotResponseDto toResponseDto(ParkingSpotModel parkingSpotModel);

    ParkingSpotRequestDto toRequestDto(ParkingSpotModel parkingSpotModel);

    List<ParkingSpotResponseDto> toListResponseDto(List<ParkingSpotModel> parkingSpotModelList);

    default Page<ParkingSpotResponseDto> toPageResponseDto(Page<ParkingSpotModel> parkingSpotModelPage) {
        List<ParkingSpotResponseDto> responseDtoList = toListResponseDto(parkingSpotModelPage.getContent());
        return new PageImpl<>(responseDtoList, parkingSpotModelPage.getPageable(), parkingSpotModelPage.getTotalElements());
    }
}