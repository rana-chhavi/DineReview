package com.chhavirana.restaurant.mappers;

import com.chhavirana.restaurant.domain.dtos.PhotoDto;
import com.chhavirana.restaurant.domain.entities.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhotoMapper {
    PhotoDto toDto(Photo photo);
}
