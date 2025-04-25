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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
        User user = jwtToUser(jwt);
        ReviewCreateUpdateRequest request = reviewMapper.toReviewCreateUpdateRequest(reviewCreateUpdateRequestDto);
        Review review = reviewService.createReview(user, restaurantId, request);
        return ResponseEntity.ok(reviewMapper.toDto(review));
    }

    private static User jwtToUser(Jwt jwt) {
        return User.builder()
                   .id(jwt.getSubject())
                   .username(jwt.getClaimAsString("preferred_username"))
                   .givenName(jwt.getClaimAsString("given_name"))
                   .familyName(jwt.getClaimAsString("family_name"))
                   .build();
    }

    @GetMapping
    public Page<ReviewDto> listReviews(
            @PathVariable String restaurantId,
            @PageableDefault(
                    size = 20,
                    page = 0,
                    sort = "datePosted",
                    direction = Sort.Direction.DESC) Pageable pageable) {

        return reviewService.listReviews(restaurantId, pageable)
                .map(reviewMapper::toDto);
    }

    @GetMapping(path= "/{reviewId}")
    public ResponseEntity<ReviewDto> getReview(
            @PathVariable String reviewId,
            @PathVariable String restaurantId
    ) {
        return reviewService.getReview(restaurantId, reviewId)
                .map(reviewMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PutMapping(path = "/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable String reviewId,
            @PathVariable String restaurantId,
            @Valid @RequestBody ReviewCreateUpdateRequestDto review,
            @AuthenticationPrincipal Jwt jwt
    ) {
        ReviewCreateUpdateRequest reviewCreateUpdateRequest = reviewMapper.toReviewCreateUpdateRequest(review);
        User user = jwtToUser(jwt);
        Review updatedReview = reviewService.updateReview(user, restaurantId, reviewId, reviewCreateUpdateRequest);
        return ResponseEntity.ok(reviewMapper.toDto(updatedReview));
    }

    @DeleteMapping(path = "/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable String reviewId,
            @PathVariable String restaurantId
    ) {
        reviewService.deleteReview(restaurantId, reviewId);
        return ResponseEntity.noContent().build();
    }
}
