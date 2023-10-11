package com.branas.domain.usecases.ride;

import com.branas.api.ports.RideDAO;
import com.branas.domain.entities.Position;
import com.branas.domain.entities.Ride;
import com.branas.infrastructure.exceptions.ResourceNotFoundException;
import com.branas.infrastructure.exceptions.ValidationErrorException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class StartRide {

    @Inject
    RideDAO rideDAO;

    public void execute(String rideId) {
        Ride ride = rideDAO.getRideById(UUID.fromString(rideId))
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));
        if (!ride.getStatus().getValue().equals("ACCEPTED")) {
            throw new ValidationErrorException("Ride is not ACCEPTED");
        }
        Position.create(ride.getRideId(),
                ride.getFromCoordinate().getLatitude(),
                ride.getFromCoordinate().getLongitude());
        ride.start();
        rideDAO.update(ride);
    }
}
