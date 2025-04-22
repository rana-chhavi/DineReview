package com.chhavirana.restaurant.services.impl;

import com.chhavirana.restaurant.domain.GeoLocation;
import com.chhavirana.restaurant.domain.entities.Address;
import com.chhavirana.restaurant.services.GeoLocationService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomBangloreGeoLocationService implements GeoLocationService {

    private static final float MIN_LATITUDE = 12.834f;
    private static final float MAX_LATITUDE = 13.139f;
    private static final float MIN_LONGITUDE = 77.460f;
    private static final float MAX_LONGITUDE = 77.772f;

    @Override
    public GeoLocation geoLocate(Address address) {
        Random random = new Random();
        double latitude = MIN_LATITUDE + random.nextDouble() *  (MAX_LATITUDE - MIN_LATITUDE);
        double longitude = MIN_LONGITUDE + random.nextDouble() *  (MAX_LONGITUDE - MIN_LONGITUDE);

        return GeoLocation.builder().latitude(latitude).longitude(longitude).build();
    }
}
