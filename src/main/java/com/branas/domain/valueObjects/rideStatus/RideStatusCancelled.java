package com.branas.domain.valueObjects.rideStatus;

import com.branas.domain.entities.Ride;
import com.branas.domain.enums.RideStateEnum;

public class RideStatusCancelled implements RideStatus {

    private final RideStateEnum value;

    public RideStatusCancelled(Ride ride) {
        value = RideStateEnum.CANCELLED;
        ride.changeStatus(this);
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
