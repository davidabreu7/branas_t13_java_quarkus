package com.branas.domain.entities;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Ride {

    UUID rideId;
    UUID passengerId;
    UUID driverId;
    String status;
    BigDecimal price;
    Float distance;
    LocalDateTime timestamp;
    Coordinate fromCoordinate;
    Coordinate toCoordinate;
}
