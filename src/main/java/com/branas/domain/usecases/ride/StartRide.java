package com.branas.domain.usecases.ride;

import com.branas.api.ports.RideDAO;
import com.branas.domain.entities.Position;
import com.branas.domain.entities.Ride;
import com.branas.infrastructure.exceptions.ResourceNotFoundException;
import com.branas.infrastructure.exceptions.ValidationErrorException;
import com.branas.infrastructure.repositories.PositionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class StartRide {

    @Inject
    RideDAO rideDAO;
    @Inject
    PositionRepository positionRepository;

    public void execute(String rideId) {
        Ride ride = rideDAO.getRideById(UUID.fromString(rideId))
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));
        if (!ride.getStatus().getValue().equals("ACCEPTED")) {
            throw new ValidationErrorException("Ride is not ACCEPTED");
        }
        Position position = Position.create(ride.getRideId(),
                ride.getFromCoordinate().getLatitude(),
                ride.getFromCoordinate().getLongitude());
        positionRepository.persist(position);
        ride.start();
        rideDAO.update(ride);
    }
}
