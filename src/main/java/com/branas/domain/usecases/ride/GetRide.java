package com.branas.domain.usecases.ride;

import com.branas.api.ports.RideRepository;
import com.branas.domain.entities.Ride;
import com.branas.infrastructure.exceptions.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class GetRide {

    @Inject
    RideRepository rideRepository;

    public Ride execute(String rideId) {
        return rideRepository.getRideById(UUID.fromString(rideId))
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));
    }
}
