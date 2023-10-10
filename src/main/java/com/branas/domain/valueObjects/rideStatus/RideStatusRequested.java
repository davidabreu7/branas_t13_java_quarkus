package com.branas.domain.valueObjects.rideStatus;

import com.branas.domain.entities.Ride;
import com.branas.domain.enums.RideStateEnum;
import com.branas.infrastructure.exceptions.ValidationErrorException;

public class RideStatusRequested implements RideStatus {

    private RideStateEnum value;

    public RideStatusRequested() {
        value = RideStateEnum.REQUESTED;
    }

    @Override
    public RideStatus process(RideStatus rideStatus) {
        if (!rideStatus.getRideStatus().equals(RideStateEnum.REQUESTED))
            throw new ValidationErrorException("Ride is not requested");
        return new RideStatusAccepted();
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
