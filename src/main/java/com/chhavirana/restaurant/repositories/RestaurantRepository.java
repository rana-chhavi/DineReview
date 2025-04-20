package com.chhavirana.restaurant.repositories;

import com.chhavirana.restaurant.domain.entities.Restaurant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends ElasticsearchRepository<Restaurant, String> {
    // TODO: Custom queries as in when needed
}
