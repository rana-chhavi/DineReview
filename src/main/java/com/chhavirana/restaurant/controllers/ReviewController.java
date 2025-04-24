package com.chhavirana.restaurant.controllers;

import com.chhavirana.restaurant.domain.ReviewCreateUpdateRequest;
import com.chhavirana.restaurant.domain.dtos.ReviewCreateUpdateRequestDto;
import com.chhavirana.restaurant.domain.dtos.ReviewDto;
import com.chhavirana.restaurant.domain.entities.Review;
import com.chhavirana.restaurant.domain.entities.User;
import com.chhavirana.restaurant.mappers.ReviewMapper;
import com.chhavirana.restaurant.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/restaurants/{restaurantId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(
            @PathVariable String restaurantId,
            @Valid @RequestBody ReviewCreateUpdateRequestDto reviewCreateUpdateRequestDto,
            @AuthenticationPrincipal Jwt jwt
        ) {
        User user = User.builder()
                                  .id(jwt.getSubject())
                                  .username(jwt.getClaimAsString("preferred_username"))
                                  .givenName(jwt.getClaimAsString("given_name"))
                                  .familyName(jwt.getClaimAsString("family_name"))
                                  .build();
        ReviewCreateUpdateRequest request = reviewMapper.toReviewCreateUpdateRequest(reviewCreateUpdateRequestDto);
        Review review = reviewService.createReview(user, restaurantId, request);
        return ResponseEntity.ok(reviewMapper.toDto(review));
    }
}
