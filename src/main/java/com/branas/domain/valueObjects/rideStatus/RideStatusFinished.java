package com.branas.domain.valueObjects.rideStatus;

import com.branas.domain.entities.Ride;
import com.branas.domain.enums.RideStateEnum;

public class RideStatusFinished implements RideStatus {

    private RideStateEnum value;
    public RideStatusFinished(Ride ride) {
        value = RideStateEnum.FINISHED;
        ride.changeStatus(this);
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
