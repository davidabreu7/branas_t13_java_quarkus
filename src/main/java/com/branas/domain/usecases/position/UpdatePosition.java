package com.branas.domain.usecases.position;

import com.branas.api.ports.PositionRepository;
import com.branas.api.ports.RideRepository;
import com.branas.domain.entities.Position;
import com.branas.domain.entities.Ride;
import com.branas.infrastructure.repositories.PositionRepositoryJpa;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class UpdatePosition {

    @Inject
    PositionRepository positionRepository;
    @Inject
    RideRepository rideRepository;

    public Position execute(String rideId, Double latitude, Double longitude) {
        Ride ride = rideRepository.getRideById(UUID.fromString(rideId))
                .orElseThrow(() -> new IllegalArgumentException("Ride not found"));
        if (!ride.getStatus().getValue().equals("STARTED")) {
            throw new IllegalArgumentException("Ride is not STARTED");
        }
        Position position = Position.create(ride.getRideId(),
                latitude,
                longitude);
        positionRepository.save(position);
        return position;
    }
}
