package com.branas.domain.valueObjects.rideStatus;

import com.branas.domain.entities.Ride;
import com.branas.domain.enums.RideStateEnum;

public class RideStatusStarted implements RideStatus {

    private RideStateEnum value;
    private Ride ride;
    public RideStatusStarted(Ride ride) {
        value = RideStateEnum.STARTED;
        ride.changeStatus(this);
        this.ride = ride;
    }

    @Override
    public RideStatus process(RideStatus rideStatus) {
        if(!rideStatus.getRideStatus().equals(RideStateEnum.STARTED))
            throw new IllegalArgumentException("Ride is not started");
        return new RideStatusFinished(ride);
    }

    @Override
    public RideStatus cancel(RideStatus rideStatus) {
        return new RideStatusCancelled(ride);
    }

    @Override
    public RideStateEnum getRideStatus() {
        return value;
    }
}
