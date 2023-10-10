package com.branas.domain.usecases.ride;

import com.branas.api.ports.RideDAO;
import com.branas.domain.entities.Ride;
import com.branas.infrastructure.exceptions.ResourceNotFoundException;
import jakarta.inject.Inject;

import java.util.UUID;

public class CancelRide {

    @Inject
    RideDAO rideDAO;

    public void execute(String rideId) {
        Ride ride = rideDAO.getRideById(UUID.fromString(rideId))
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));
        ride.cancel();
        rideDAO.update(ride);
    }
}
