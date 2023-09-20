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
    static final String STATUS = "REQUESTED";
    static final Double DISTANCE = 10.0;
    static final Coordinate FROM_COORDINATE = new Coordinate(38.7195426 ,-9.1595502);
    static final Coordinate TO_COORDINATE = new Coordinate(38.7116397 ,-9.1269583);
    static final BigDecimal PRICE = new BigDecimal("10.0");
    static final LocalDateTime TIMESTAMP = LocalDateTime.now();

    Ride validRide;

    @BeforeEach
    void setUp() {
        validRide = new Ride(
                null,
                PASSENGER_ID,
                null,
                STATUS,
                PRICE,
                DISTANCE,
                TIMESTAMP,
                FROM_COORDINATE,
                TO_COORDINATE
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldSaveRideAndGetRideById() throws Exception {
        UUID rideId = UUID.randomUUID();
        validRide.setRideId(rideId);
        rideDAO.save(validRide);
        Assertions.assertThat(rideDAO.getRideById(rideId))
                .isNotNull()
                .isInstanceOf(Ride.class)
                .hasFieldOrPropertyWithValue("rideId", rideId)
                .hasFieldOrPropertyWithValue("passengerId", PASSENGER_ID);
    }

    @Test
    void update() throws Exception {
        UUID rideId = UUID.randomUUID();
        validRide.setRideId(rideId);
        rideDAO.save(validRide);
        Ride updatedRide = validRide;;
        updatedRide.setStatus("ACCEPTED");
        updatedRide.setDriverId(DRIVER_ID);
        rideDAO.update(updatedRide);
        Assertions.assertThat(rideDAO.getRideById(rideId))
                .isNotNull()
                .isInstanceOf(Ride.class)
                .hasFieldOrPropertyWithValue("rideId", rideId)
                .hasFieldOrPropertyWithValue("passengerId", PASSENGER_ID)
                .hasFieldOrPropertyWithValue("driverId", DRIVER_ID)
                .hasFieldOrPropertyWithValue("status", "ACCEPTED");
    }
}