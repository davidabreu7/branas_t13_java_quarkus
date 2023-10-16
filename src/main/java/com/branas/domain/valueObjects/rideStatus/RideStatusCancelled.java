package com.branas.domain.valueObjects.rideStatus;

import com.branas.domain.enums.RideStateEnum;

public final class RideStatusCancelled implements RideStatus {

    private final RideStateEnum value;

    public RideStatusCancelled() {
        value = RideStateEnum.CANCELLED;
    }

    @Override
    public RideStatus process(RideStatus rideStatus) {
        throw new IllegalArgumentException("Ride is already cancelled");
    }

    @Override
    public RideStatus cancel(RideStatus rideStatus) {
            throw new IllegalArgumentException("Ride is already cancelled");
    }
    @Override
    public RideStateEnum getRideStatus() {
        return value;
    }
}
