package com.branas.domain.services;

import com.branas.domain.DTO.RidePath;
import com.branas.domain.entities.Ride;
import com.branas.domain.ports.RideDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RideService {

    @Inject
    RideDAO rideDAO;

    public Ride requestRide(String accountId, RidePath ridePath) {
        return null;
    }

    public Ride acceptRide(String rideId) {
        return null;
    }

    public Ride getRideById(String rideId) {
        return null;
    }
}
