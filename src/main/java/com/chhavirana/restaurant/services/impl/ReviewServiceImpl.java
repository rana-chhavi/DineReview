package com.chhavirana.restaurant.services.impl;

import com.chhavirana.restaurant.domain.ReviewCreateUpdateRequest;
import com.chhavirana.restaurant.domain.entities.Photo;
import com.chhavirana.restaurant.domain.entities.Restaurant;
import com.chhavirana.restaurant.domain.entities.Review;
import com.chhavirana.restaurant.domain.entities.User;
import com.chhavirana.restaurant.exceptions.RestaurantNotFoundException;
import com.chhavirana.restaurant.exceptions.ReviewNotAllowedException;
import com.chhavirana.restaurant.repositories.RestaurantRepository;
import com.chhavirana.restaurant.services.RestaurantService;
import com.chhavirana.restaurant.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantService restaurantService;

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

    @Override
    public Page<Review> listReviews(String restaurantId, Pageable pageable) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);

        List<Review> reviews = restaurant.getReviews();
        Sort sort = pageable.getSort();

        if(sort.isSorted()) {
            Sort.Order order = sort.iterator().next();
            String property = order.getProperty();
            boolean isAscending = order.getDirection().isAscending();

            Comparator<Review> reviewComparator = switch (property) {
                case "datePosted" -> Comparator.comparing(Review::getDatePosted);
                case "rating" -> Comparator.comparing(Review::getRating);
                default -> Comparator.comparing(Review::getDatePosted);
            };

            reviews.sort(isAscending ? reviewComparator : reviewComparator.reversed());
        } else {
            reviews.sort(Comparator.comparing(Review::getDatePosted).reversed());
        }

        int start = (int) pageable.getOffset();

        if(start >= reviews.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, reviews.size());
        }

        int end = Math.min((start + pageable.getPageSize()), reviews.size());
        return new PageImpl<>(reviews.subList(start, end), pageable, reviews.size());
    }

    @Override
    public Optional<Review> getReview(String restaurantId, String reviewId) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);
        return getReviewFromRestaurant(reviewId, restaurant);
    }

    private static Optional<Review> getReviewFromRestaurant(String reviewId, Restaurant restaurant) {
        return restaurant.getReviews()
                         .stream()
                         .filter(r -> r.getId().equals(reviewId))
                         .findFirst();
    }

    @Override
    public Review updateReview(User author, String restaurantId, String reviewId, ReviewCreateUpdateRequest review) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);

        String authorId = author.getId();
        Review existingReview = getReviewFromRestaurant(reviewId, restaurant)
                .orElseThrow(() -> new ReviewNotAllowedException("Review does not exist"));

        if(!authorId.equals(existingReview.getWrittenBy().getId())){
            throw new ReviewNotAllowedException("Cannot update another user's review");
        }

        if(LocalDateTime.now().isAfter(existingReview.getDatePosted().plusHours(48))) {
            throw new ReviewNotAllowedException("Review can no longer be edited");
        }

        existingReview.setContent(review.getContent());
        existingReview.setRating(review.getRating());
        existingReview.setLastEdited(LocalDateTime.now());

        existingReview.setPhotos(review.getPhotoIds().stream()
                .map(photoId -> Photo.builder()
                            .url(photoId)
                            .uploadDate(LocalDateTime.now())
                            .build()).toList());

        updateRestaurantAverageRating(restaurant);

        List<Review> updatedReviews = restaurant.getReviews().stream()
                                         .filter(r -> !reviewId.equals(r.getId()))
                                         .collect(Collectors.toList());

        updatedReviews.add(existingReview);
        restaurant.setReviews(updatedReviews);
        restaurantRepository.save(restaurant);
        return existingReview;
    }

    private Restaurant getRestaurantOrThrow(String restaurantId) {
        return restaurantService.getRestaurant(restaurantId).orElseThrow(() ->
                new RestaurantNotFoundException("Restaurant not found for specified restaurantId")
        );
    }

    @Override
    public void deleteReview(String restaurantId, String reviewId) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);
        List<Review> filteredReviews = restaurant.getReviews().stream()
                                      .filter(r -> !reviewId.equals(r.getId()))
                                      .toList();
        restaurant.setReviews(filteredReviews);
        updateRestaurantAverageRating(restaurant);
        restaurantRepository.save(restaurant);
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
