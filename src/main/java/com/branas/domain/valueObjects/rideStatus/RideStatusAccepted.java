package com.branas.domain.valueObjects.rideStatus;

import com.branas.domain.entities.Ride;
import com.branas.domain.enums.RideStateEnum;
import com.branas.infrastructure.exceptions.ValidationErrorException;

public class RideStatusAccepted implements RideStatus {

    private RideStateEnum value;
    private Ride ride;
    public RideStatusAccepted(Ride ride) {
        value = RideStateEnum.ACCEPTED;
        ride.changeStatus(this);
        this.ride = ride;
    }

    @Override
    public RideStatus process(RideStatus rideStatus) {
        if (!rideStatus.getRideStatus().equals(RideStateEnum.ACCEPTED)){
            throw new ValidationErrorException("Ride is not accepted");
        }
        return new RideStatusStarted(ride);
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
