package com.branas.infrastructure.jpaEntities;

import com.branas.domain.entities.Ride;
import com.branas.domain.enums.RideStateEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "ride")
public class RideEntity {

    @Id
    @Column(name = "ride_id")
    private UUID rideId;
    @Column(name = "passenger_id")
    private UUID passengerId;
    @Column(name = "driver_id")
    private UUID driverId;
    private RideStateEnum status;
    private Double distance;
    @Column(name = "fare")
    private BigDecimal price;
    @Column(name = "date")
    private LocalDateTime timestamp;
    @Column(name = "from_lat")
    private Double fromLatitude;
    @Column(name = "to_lat")
    private Double toLatitude;
    @Column(name = "from_long")
    private Double fromLongitude;
    @Column(name = "to_long")
    private Double toLongitude;

    public RideEntity() {
        this.passengerId = null;
    }

    private RideEntity(
            UUID rideId,
            UUID passengerId,
            UUID driverId,
            RideStateEnum status,
            Double distance,
            BigDecimal price,
            LocalDateTime timestamp,
            Double fromLatitude,
            Double toLatitude,
            Double fromLongitude,
            Double toLongitude
    ) {
        this.rideId = rideId;
        this.passengerId = passengerId;
        this.driverId = driverId;
        this.status = status;
        this.distance = distance;
        this.price = price;
        this.timestamp = timestamp;
        this.fromLatitude = fromLatitude;
        this.toLatitude = toLatitude;
        this.fromLongitude = fromLongitude;
        this.toLongitude = toLongitude;
    }

    public static RideEntity create(Ride ride){
        return new RideEntity(
                ride.getRideId(),
                ride.getPassengerId(),
                ride.getDriverId(),
                ride.getStatus(),
                ride.getDistance(),
                ride.getPrice(),
                ride.getTimestamp(),
                ride.getFromCoordinate().getLatitude(),
                ride.getToCoordinate().getLatitude(),
                ride.getFromCoordinate().getLongitude(),
                ride.getToCoordinate().getLongitude()
        );
    }
}
