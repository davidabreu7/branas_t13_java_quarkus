package com.branas.domain.valueObjects.rideStatus;

import com.branas.domain.enums.RideStateEnum;

public final class RideStatusStarted implements RideStatus {

    private final RideStateEnum value;
    public RideStatusStarted() {
        value = RideStateEnum.STARTED;
    }

    @Override
    public RideStatus process(RideStatus rideStatus) {
        if(!rideStatus.getRideStatus().equals(RideStateEnum.STARTED))
            throw new IllegalArgumentException("Ride is not STARTED");
        return new RideStatusFinished();
    }

    @Override
    public RideStatus cancel(RideStatus rideStatus) {
        return new RideStatusCancelled();
    }

    @Override
    public RideStateEnum getRideStatus() {
        return value;
    }
}
