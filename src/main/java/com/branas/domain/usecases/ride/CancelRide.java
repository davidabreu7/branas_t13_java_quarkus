package com.branas.domain.usecases.ride;

import com.branas.api.ports.RideRepository;
import com.branas.domain.entities.Ride;
import com.branas.infrastructure.exceptions.ResourceNotFoundException;
import jakarta.inject.Inject;

import java.util.UUID;

public class CancelRide {

    @Inject
    RideRepository rideRepository;

    public void execute(String rideId) {
        Ride ride = rideRepository.getRideById(UUID.fromString(rideId))
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));
        ride.cancel();
        rideRepository.update(ride);
    }
}
