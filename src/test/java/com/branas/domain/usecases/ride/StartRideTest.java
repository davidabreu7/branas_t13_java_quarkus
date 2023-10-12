package com.branas.domain.usecases.ride;

import com.branas.api.ports.RideDAO;
import com.branas.domain.entities.Ride;
import com.branas.domain.valueObjects.Coordinate;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@QuarkusTest
class StartRideTest {

    @Inject
    StartRide startRide;
    @InjectMock
    RideDAO rideDAO;
    Ride ride;
    UUID passengerId;
    Coordinate fromCoordinate;
    Coordinate toCoordinate;
    UUID driverId;

    @BeforeEach
    void setUp() {
        passengerId = UUID.randomUUID();
        driverId = UUID.randomUUID();
        fromCoordinate = new Coordinate(38.8976, -77.0366);
        toCoordinate = new Coordinate(39.9496, -75.1503);
        ride = Ride.create(passengerId, fromCoordinate, toCoordinate);

        when(rideDAO.getRideById(ride.getRideId()))
                .thenReturn(java.util.Optional.of(ride));
    }

    @Test
    @Transactional
    void ShouldStartRide() {
        // Arrange
        String rideId = ride.getRideId().toString();
        ride.accept(driverId);
        // Act
        startRide.execute(rideId);
        // Assert
        verify(rideDAO, times(1)).update(ride);
        assertThat(ride.getStatus().getValue())
                .isEqualTo("STARTED");
    }

    @Test
    void ShouldThrowResourceNotFoundException() {
        // Arrange
        String rideId = UUID.randomUUID().toString();
        // Act
        // Assert
        assertThatThrownBy(() -> startRide.execute(rideId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ride not found");
    }

    @Test
    void ShouldThrowValidationErrorException() {
        // Arrange
        String rideId = ride.getRideId().toString();
        // Act
        // Assert
        assertThatThrownBy(() -> startRide.execute(rideId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ride is not ACCEPTED");
    }
}