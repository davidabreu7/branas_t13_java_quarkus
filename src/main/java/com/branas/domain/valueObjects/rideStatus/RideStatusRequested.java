package com.branas.domain.valueObjects.rideStatus;

import com.branas.domain.entities.Ride;
import com.branas.domain.enums.RideStateEnum;
import com.branas.infrastructure.exceptions.ValidationErrorException;

public class RideStatusRequested implements RideStatus {

    private RideStateEnum value;
    private Ride ride;

    public RideStatusRequested(Ride ride) {
        value = RideStateEnum.REQUESTED;
        ride.changeStatus(this);
        this.ride = ride;
    }

    @Override
    public RideStatus process(RideStatus rideStatus) {
        if (!rideStatus.getRideStatus().equals(RideStateEnum.REQUESTED))
            throw new ValidationErrorException("Ride is not requested");
        return new RideStatusAccepted(ride);
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
