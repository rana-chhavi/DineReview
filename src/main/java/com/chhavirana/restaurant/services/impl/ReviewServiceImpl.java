package com.chhavirana.restaurant.services.impl;

import com.chhavirana.restaurant.domain.ReviewCreateUpdateRequest;
import com.chhavirana.restaurant.domain.entities.Photo;
import com.chhavirana.restaurant.domain.entities.Restaurant;
import com.chhavirana.restaurant.domain.entities.Review;
import com.chhavirana.restaurant.domain.entities.User;
import com.chhavirana.restaurant.exceptions.RestaurantNotFoundException;
import com.chhavirana.restaurant.exceptions.ReviewNotAllowedException;
import com.chhavirana.restaurant.repositories.RestaurantRepository;
import com.chhavirana.restaurant.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public Review createReview(User author, String restaurantId, ReviewCreateUpdateRequest request) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id not found: " + restaurantId));


        boolean hasExistingReview = restaurant.getReviews().stream()
                .anyMatch(r -> r.getWrittenBy().getId().equals(author.getId()));

        if(hasExistingReview) {
            throw new ReviewNotAllowedException("User has already reviewed this restaurant");
        }

        List<Photo> photos = request.getPhotoIds().stream().map(photoUrl -> Photo.builder()
                                                                    .url(photoUrl)
                                                                    .uploadDate(LocalDateTime.now())
                                                                    .build()).toList();

        String reviewId = UUID.randomUUID().toString();
        Review reviewToCreate = Review.builder()
                .id(reviewId)
                .content(request.getContent())
                .rating(request.getRating())
                .writtenBy(author)
                .lastEdited(LocalDateTime.now())
                .datePosted(LocalDateTime.now())
                .photos(photos)
                .build();

        restaurant.getReviews().add(reviewToCreate);
        updateRestaurantAverageRating(restaurant);
        Restaurant savedRestaurants = restaurantRepository.save(restaurant);

        return savedRestaurants.getReviews().stream()
                .filter(r -> reviewId.equals(r.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(("Error retrieving created review")));
    }

    private void updateRestaurantAverageRating(Restaurant restaurant) {
        List<Review> reviews = restaurant.getReviews();
        if(reviews.isEmpty()) {
                restaurant.setAverageRating(0.0f);
        } else {
            double averageRating = reviews.stream().mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0);
            restaurant.setAverageRating((float) averageRating);
        }
    }
}
