package com.chhavirana.restaurant.mappers;

import com.chhavirana.restaurant.domain.ReviewCreateUpdateRequest;
import com.chhavirana.restaurant.domain.dtos.ReviewCreateUpdateRequestDto;
import com.chhavirana.restaurant.domain.dtos.ReviewDto;
import com.chhavirana.restaurant.domain.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {

    ReviewCreateUpdateRequest toReviewCreateUpdateRequest(ReviewCreateUpdateRequestDto reviewCreateUpdateRequestDto);

    ReviewDto toDto(Review review);

}
