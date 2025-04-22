package com.chhavirana.restaurant.services;

import com.chhavirana.restaurant.domain.GeoLocation;
import com.chhavirana.restaurant.domain.entities.Address;

public interface GeoLocationService {
    GeoLocation geoLocate(Address address);
}
