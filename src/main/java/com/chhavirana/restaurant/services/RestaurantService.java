package com.chhavirana.restaurant.services;

import com.chhavirana.restaurant.domain.RestaurantCreateUpdateRequest;
import com.chhavirana.restaurant.domain.entities.Restaurant;
import org.springframework.stereotype.Service;

@Service
public interface RestaurantService {
    Restaurant createRestaurant(RestaurantCreateUpdateRequest request);
}
