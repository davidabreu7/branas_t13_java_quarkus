package com.branas.domain.usecases.ride;

import com.branas.api.ports.PositionRepository;
import com.branas.api.ports.RideRepository;
import com.branas.domain.entities.Position;
import com.branas.domain.entities.Ride;
import com.branas.domain.valueObjects.Coordinate;
import com.branas.infrastructure.repositories.PositionRepositoryJpa;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@QuarkusTest
class FinishRideTest {

    @Inject
    FinishRide finishRide;
    @InjectMock
    RideRepository rideRepository;
    @InjectMock
    PositionRepository positionRepository;
    Ride ride;
    UUID passengerId;
    Coordinate fromCoordinate;
    Coordinate toCoordinate;
    UUID driverId;
    List<Position> positions;
    @BeforeEach
    void setUp() {
        passengerId = UUID.randomUUID();
        driverId = UUID.randomUUID();
        fromCoordinate = new Coordinate(38.8976, -77.0366);
        toCoordinate = new Coordinate(39.9496, -75.1503);
        ride = Ride.create(passengerId, fromCoordinate, toCoordinate);
        positions = List.of(
                Position.create(ride.getRideId(), 38.8976, -77.0366),
                Position.create(ride.getRideId(), 39.9496, -75.1503)
        );

        when(rideRepository.getRideById(ride.getRideId()))
                .thenReturn(java.util.Optional.of(ride));
        when(positionRepository.findByRideId(ride.getRideId()))
                .thenReturn(positions);
    }

    @Test
    void ShouldFinishRide() {
        // Arrange
        String rideId = ride.getRideId().toString();
        ride.accept(driverId);
        ride.start();
        Double lowerDistance = 199.0;
        Double upperDistance = 200.0;
        Double totalFare = 302.75;
        // Act
        finishRide.execute(rideId);
        // Assert
        Mockito.verify(rideRepository, times(1)).update(ride);
        assertThat(ride.getStatus().getValue())
                .isEqualTo("FINISHED");
        assertThat(ride.getDistance())
                .isBetween(lowerDistance, upperDistance);
        assertThat(ride.getPrice())
                .isEqualTo(BigDecimal.valueOf(totalFare));
    }

    @Test
    void ShouldThrowResourceNotFoundException() {
        // Arrange
        String rideId = UUID.randomUUID().toString();
        // Act
        // Assert
        assertThatThrownBy(() -> finishRide.execute(rideId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ride not found");
    }

    @Test
    void ShouldThrowValidationErrorException() {
        // Arrange
        String rideId = ride.getRideId().toString();
        // Act
        // Assert
        assertThatThrownBy(() -> finishRide.execute(rideId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ride is not STARTED");
    }

    @Test
    void ShouldThrowValidationErrorExceptionWhenRideIsNotStarted() {
        // Arrange
        String rideId = ride.getRideId().toString();
        ride.accept(driverId);
        // Act
        // Assert
        assertThatThrownBy(() -> finishRide.execute(rideId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ride is not STARTED");
    }

    @Test
    void ShouldThrowValidationErrorExceptionWhenRideIsAlreadyFinished() {
        // Arrange
        String rideId = ride.getRideId().toString();
        Ride ride = Ride.create(passengerId, fromCoordinate, toCoordinate);
        ride.accept(driverId);
        ride.start();
        // Act
        when(rideRepository.getRideById(UUID.fromString(rideId)))
                .thenReturn(java.util.Optional.of(ride));
        finishRide.execute(rideId);
        // Assert
        assertThat(ride.getStatus().getValue())
                .isEqualTo("FINISHED");
        assertThatThrownBy(() -> finishRide.execute(rideId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ride is not STARTED");
    }

    @Test
    void ShouldThrowValidationErrorExceptionWhenRideIsAlreadyCanceled() {
        // Arrange
        String rideId = ride.getRideId().toString();
        ride.cancel();
        // Act
        // Assert
        assertThat(ride.getStatus().getValue())
                .isEqualTo("CANCELLED");
        assertThatThrownBy(() -> finishRide.execute(rideId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ride is not STARTED");
    }

}