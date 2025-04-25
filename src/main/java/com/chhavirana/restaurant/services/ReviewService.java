package com.chhavirana.restaurant.services;

import com.chhavirana.restaurant.domain.ReviewCreateUpdateRequest;
import com.chhavirana.restaurant.domain.entities.Review;
import com.chhavirana.restaurant.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReviewService {

    Review createReview(User author, String restaurantId, ReviewCreateUpdateRequest review);

    Page<Review> listReviews(String restaurantId, Pageable pageable);

    Optional<Review> getReview(String restaurantId, String reviewId);

    Review updateReview(User author, String restaurantId, String reviewId, ReviewCreateUpdateRequest review);

    void deleteReview(String restaurantId, String reviewId);
}
