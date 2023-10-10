package com.branas.domain.entities;

import com.branas.domain.enums.RideStateEnum;
import com.branas.domain.valueObjects.Coordinate;
import com.branas.domain.valueObjects.rideStatus.RideStatus;
import com.branas.domain.valueObjects.rideStatus.RideStatusFactory;
import com.branas.domain.valueObjects.rideStatus.RideStatusRequested;
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
    private final UUID passengerId;
    private UUID driverId;
    private RideStatus status;
    private Double distance;
    private BigDecimal price;
    private LocalDateTime timestamp;
    private final Coordinate fromCoordinate;
    private final Coordinate toCoordinate;

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
        ride.status = new RideStatusRequested();
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
        ride.status = RideStatusFactory.createStatus(status);
        ride.distance = distance;
        ride.price = price;
        ride.timestamp = timestamp;
        return ride;
    }

    public void accept(UUID driverId) {
        this.driverId = driverId;
        this.status = status.process(this.status);
    }

    public void start() {
        this.status = status.process(this.status);
    }

    public void cancel() {
        this.status = status.cancel(this.status);
    }

    public RideStateEnum getStatus() {
        return status.getRideStatus();
    }

    public void finish(Double distance, BigDecimal price) {
        this.distance = distance;
        this.price = price;
        this.status = status.process(this.status);
    }
}
