package com.branas.infrastructure.repositories.implementations;

import com.branas.domain.enums.RideStateEnum;
import com.branas.domain.valueObjects.Coordinate;
import com.branas.domain.entities.Ride;
import com.branas.infrastructure.repositories.implementations.RideRepositoryImpl;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

@QuarkusTest
class RideRepositoryImplTest {

    @Inject
    RideRepositoryImpl rideRepository;
    static final UUID PASSENGER_ID = UUID.fromString("3a1f5e71-cdf6-40e0-963c-db3bc3d48f00");
    static final UUID DRIVER_ID = UUID.fromString("1f410ba9-b2ad-41d3-9c04-f7ffd22d774b");
    static final Coordinate FROM_COORDINATE = new Coordinate(38.7195426 ,-9.1595502);
    static final Coordinate TO_COORDINATE = new Coordinate(38.7116397 ,-9.1269583);

    Ride validRide;

    @BeforeEach
    void setUp() {
        validRide = Ride.create(PASSENGER_ID, FROM_COORDINATE,TO_COORDINATE);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Transactional
    void shouldSaveRideAndGetRideById() {
        Ride ride = validRide;
        UUID rideId = ride.getRideId();
        rideRepository.save(ride);
        Ride savedRide = rideRepository.getRideById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));
        Assertions.assertThat(savedRide)
                .isNotNull()
                .isInstanceOf(Ride.class)
                .hasFieldOrPropertyWithValue("rideId", rideId)
                .hasFieldOrPropertyWithValue("passengerId", PASSENGER_ID);
    }

    @Test
    @Transactional
    void update()  {
        Ride ride = Ride.create(
                PASSENGER_ID,
                FROM_COORDINATE,
                TO_COORDINATE
        );
        rideRepository.save(ride);
        ride.accept(DRIVER_ID);
        rideRepository.update(ride);
        Ride savedRide = rideRepository.getRideById(ride.getRideId())
                .orElseThrow(() -> new RuntimeException("Ride not found"));
        Assertions.assertThat(savedRide)
                .isNotNull()
                .isInstanceOf(Ride.class)
                .hasFieldOrPropertyWithValue("rideId", ride.getRideId())
                .hasFieldOrPropertyWithValue("passengerId", PASSENGER_ID)
                .hasFieldOrPropertyWithValue("driverId", DRIVER_ID)
                .hasFieldOrPropertyWithValue("status", RideStateEnum.ACCEPTED);
    }
}