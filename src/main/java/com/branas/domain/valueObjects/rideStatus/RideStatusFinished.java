package com.branas.domain.valueObjects.rideStatus;

import com.branas.domain.enums.RideStateEnum;

public final class RideStatusFinished implements RideStatus {

    private final RideStateEnum value;
    public RideStatusFinished() {
        value = RideStateEnum.FINISHED;
    }

    @Override
    public RideStatus process(RideStatus rideStatus) {
        throw new IllegalArgumentException("Ride is already finished");
    }

    @Override
    public RideStatus cancel(RideStatus rideStatus) {
        throw new IllegalArgumentException("Ride is already finished");
    }

    @Override
    public RideStateEnum getRideStatus() {
        return value;
    }
}
