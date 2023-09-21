package com.branas.utils;

import com.branas.domain.entities.Coordinate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class RideTestValues {

    public static final UUID PASSENGER_ID = UUID.fromString("3a1f5e71-cdf6-40e0-963c-db3bc3d48f00");
    public static final UUID DRIVER_ID = UUID.fromString("1f410ba9-b2ad-41d3-9c04-f7ffd22d774b");
    public static final String STATUS = "REQUESTED";
    public static final Double DISTANCE = 10.0;
    public static final Coordinate FROM_COORDINATE = new Coordinate(38.7195426 ,-9.1595502);
    public static final Coordinate TO_COORDINATE = new Coordinate(38.7116397 ,-9.1269583);
    public static final BigDecimal PRICE = new BigDecimal("10.0");
    public static final LocalDateTime TIMESTAMP = LocalDateTime.now();
}
