package com.branas.domain.usecases.ride;

import com.branas.api.ports.PositionRepository;
import com.branas.api.ports.RideRepository;
import com.branas.domain.entities.Position;
import com.branas.domain.entities.Ride;
import com.branas.domain.services.DistanceCalculator;
import com.branas.domain.services.FareCalculator;
import com.branas.infrastructure.exceptions.ResourceNotFoundException;
import com.branas.infrastructure.repositories.PositionRepositoryJpa;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FinishRide {

    @Inject
    public RideRepository rideRepository;
    @Inject
    PositionRepository positionRepository;


    public void execute(String rideId) {
        Ride ride = rideRepository.getRideById(UUID.fromString(rideId))
                .orElseThrow( () -> new ResourceNotFoundException("Ride not found"));
        if (!ride.getStatus().getValue().equals("STARTED")) {
            throw new IllegalArgumentException("Ride is not STARTED");
        }
        Position toPosition = Position.create(ride.getRideId(),
                ride.getToCoordinate().getLatitude(),
                ride.getToCoordinate().getLongitude());
        positionRepository.save(toPosition);
        List<Position> positions = positionRepository.findByRideId(UUID.fromString(rideId));
        Double totalDistance = calculateTotalDistance(positions);
        BigDecimal price = BigDecimal.valueOf(FareCalculator.calculate(totalDistance));
        ride.finish(totalDistance, price);
        rideRepository.update(ride);
    }

    private Double calculateTotalDistance(List<Position> positions) {
        double totalDistance = 0.0;
        for (int i = 0; i < positions.size(); i++) {
            if (i == 0) {
                continue;
            }
            Position previousPosition = positions.get(i - 1);
            Position currentPosition = positions.get(i);
            totalDistance += DistanceCalculator.calculate(previousPosition.getCoordinate(), currentPosition.getCoordinate());
        }
        return totalDistance;
    }
}
