package com.branas.domain.usecases.position;

import com.branas.api.ports.PositionRepository;
import com.branas.api.ports.RideRepository;
import com.branas.domain.entities.Position;
import com.branas.domain.entities.Ride;
import com.branas.domain.valueObjects.Coordinate;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
@Transactional
class UpdatePositionTest {

    @Inject
    UpdatePosition updatePosition;
    @Inject
    PositionRepository positionRepository;
    @InjectMock
    RideRepository rideRepository;

    UUID passengerId;
    UUID driverId;
    Coordinate fromCoordinate;
    Coordinate toCoordinate;
    Ride ride;
    Position position1;
    Position position2;

    @BeforeEach
    void setUp() {
        passengerId = UUID.randomUUID();
        driverId = UUID.randomUUID();
        fromCoordinate = new Coordinate(1.0, 1.0);
        toCoordinate = new Coordinate(4.0, 4.0);
        ride = Ride.create(passengerId, fromCoordinate,toCoordinate);

        when(rideRepository.getRideById(any(UUID.class)))
                .thenReturn(Optional.of(ride));
    }

    @Test
    void shouldUpdatePosition() {
        Position toCoordinatePosition = Position.create(ride.getRideId(),
                toCoordinate.getLatitude(),
                toCoordinate.getLongitude());
        positionRepository.save(toCoordinatePosition);
        ride.accept(driverId);
        ride.start();
       Position position1 = updatePosition.execute(ride.getRideId().toString(),
                2.0,
                2.0);
        Position position2 = updatePosition.execute(ride.getRideId().toString(),
                3.0,
                3.0);
        List<Position> positions = positionRepository.findByRideId(ride.getRideId());
        assertThat(positions)
                .hasSize(3)
                .containsExactly(toCoordinatePosition, position1, position2);
    }
}