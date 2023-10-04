package com.branas.domain.usecases.Ride;

import com.branas.api.ports.RideDAO;
import com.branas.domain.entities.Ride;
import com.branas.infrastructure.exceptions.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class GetRide {

    @Inject
    RideDAO rideDAO;

    public Ride execute(String rideId) {
        return rideDAO.getRideById(UUID.fromString(rideId))
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));
    }
}
