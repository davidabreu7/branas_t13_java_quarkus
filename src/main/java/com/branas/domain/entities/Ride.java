package com.branas.domain.entities;

import com.branas.infrastructure.exceptions.ValidationErrorException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@ToString
public class Ride {

    private UUID rideId;
    private UUID passengerId;
    private UUID driverId;
    private String status;
    private Double distance;
    private BigDecimal price;
    private LocalDateTime timestamp;
    private Coordinate fromCoordinate;
    private Coordinate toCoordinate;

    private Ride(
            UUID passengerId,
            Coordinate fromCoordinate,
            Coordinate toCoordinate
    ) {
        this.passengerId = passengerId;
        this.fromCoordinate = fromCoordinate;
        this.toCoordinate = toCoordinate;
    }

    public static Ride create(
            UUID passengerId,
            Coordinate fromCoordinate,
            Coordinate toCoordinate) {
    Ride ride = new Ride(passengerId, fromCoordinate, toCoordinate);
        ride.rideId = UUID.randomUUID();
        ride.status = "REQUESTED";
        ride.timestamp = LocalDateTime.now();
        ride.distance= 0.0;
        return ride;
    }

    public static Ride restore(
            UUID rideId,
            UUID passengerId,
            UUID driverId,
            String status,
            Double distance,
            BigDecimal price,
            LocalDateTime timestamp,
            Coordinate fromCoordinate,
            Coordinate toCoordinate
    ) {
        Ride ride = new Ride(passengerId, fromCoordinate, toCoordinate);
        ride.rideId = rideId;
        ride.driverId = driverId;
        ride.status = status;
        ride.distance = distance;
        ride.price = price;
        ride.timestamp = timestamp;
        return ride;
    }

    public void accept(UUID driverId) {
        if (!status.equals("REQUESTED")) {
            throw new ValidationErrorException("Ride is not requested");
        }
        this.driverId = driverId;
        this.status = "ACCEPTED";
    }
}
