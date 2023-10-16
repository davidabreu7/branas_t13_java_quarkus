package com.branas.domain.usecases.ride;

import com.branas.api.ports.RideRepository;
import com.branas.domain.valueObjects.Coordinate;
import com.branas.domain.entities.Ride;
import com.branas.infrastructure.exceptions.ResourceNotFoundException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class GetRideTest {

    @Inject
    GetRide getRide;

    @InjectMock
    RideRepository rideRepository;

    Coordinate from;
    Coordinate to;

    @Test
    void shouldGetRide() {
        UUID passengerId = UUID.randomUUID();
        from = new Coordinate(1.0, 1.0);
        to = new Coordinate(2.0, 2.0);
        Ride ride = Ride.create(
                passengerId,
                from,
                to);
        when(rideRepository.getRideById(any(UUID.class)))
                .thenReturn(Optional.of(ride));
        Assertions.assertThat(getRide.execute(String.valueOf(passengerId)))
                .isNotNull()
                .hasFieldOrPropertyWithValue("passengerId", passengerId)
                .hasFieldOrPropertyWithValue("fromCoordinate", from)
                .hasFieldOrPropertyWithValue("toCoordinate", to);
    }

    @Test
    void shouldThrowResourceNotFoundException() {
        UUID passengerId = UUID.randomUUID();
        when(rideRepository.getRideById(any(UUID.class)))
                .thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> getRide.execute(String.valueOf(passengerId)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Ride not found");
    }

}