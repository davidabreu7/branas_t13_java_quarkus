package com.branas.infrastructure.DAO;

import com.branas.domain.entities.Coordinate;
import com.branas.domain.entities.Ride;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@QuarkusTest
class RideDAOImplTest {

    @Inject
    RideDAOImpl rideDAO;
    static final UUID PASSENGER_ID = UUID.fromString("3a1f5e71-cdf6-40e0-963c-db3bc3d48f00");
    static final UUID DRIVER_ID = UUID.fromString("1f410ba9-b2ad-41d3-9c04-f7ffd22d774b");
    static final Coordinate FROM_COORDINATE = new Coordinate(38.7195426 ,-9.1595502);
    static final Coordinate TO_COORDINATE = new Coordinate(38.7116397 ,-9.1269583);

    Ride validRide;

    @BeforeEach
    void setUp() {
        validRide = Ride.create(
                PASSENGER_ID,
                FROM_COORDINATE,
                TO_COORDINATE
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldSaveRideAndGetRideById() {
        Ride ride = validRide;
        UUID rideId = ride.getRideId();
        rideDAO.save(ride);
        Assertions.assertThat(rideDAO.getRideById(rideId))
                .isNotNull()
                .isInstanceOf(Ride.class)
                .hasFieldOrPropertyWithValue("rideId", rideId)
                .hasFieldOrPropertyWithValue("passengerId", PASSENGER_ID);
    }

    @Test
    void update()  {
        Ride ride = validRide;
        rideDAO.save(validRide);
        Ride updatedRide = validRide;
        ride.accept(DRIVER_ID);
        rideDAO.update(updatedRide);
        Assertions.assertThat(rideDAO.getRideById(ride.getRideId()))
                .isNotNull()
                .isInstanceOf(Ride.class)
                .hasFieldOrPropertyWithValue("rideId", ride.getRideId())
                .hasFieldOrPropertyWithValue("passengerId", PASSENGER_ID)
                .hasFieldOrPropertyWithValue("driverId", DRIVER_ID)
                .hasFieldOrPropertyWithValue("status", "ACCEPTED");
    }
}