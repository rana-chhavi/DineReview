package com.chhavirana.restaurant.controllers;

import com.chhavirana.restaurant.domain.RestaurantCreateUpdateRequest;
import com.chhavirana.restaurant.domain.dtos.RestaurantCreateUpdateRequestDto;
import com.chhavirana.restaurant.domain.dtos.RestaurantDto;
import com.chhavirana.restaurant.domain.entities.Restaurant;
import com.chhavirana.restaurant.mappers.RestaurantMapper;
import com.chhavirana.restaurant.services.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;


    @PostMapping
    public ResponseEntity<RestaurantDto> createRestaurant(@Valid @RequestBody RestaurantCreateUpdateRequestDto request) {
        RestaurantCreateUpdateRequest restaurantCreateUpdateRequest = restaurantMapper.toRestaurantCreateUpdateRequest(request);

        Restaurant restaurant = restaurantService.createRestaurant(restaurantCreateUpdateRequest);
        return ResponseEntity.ok(restaurantMapper.toRestaurantDto(restaurant));
    }
}
