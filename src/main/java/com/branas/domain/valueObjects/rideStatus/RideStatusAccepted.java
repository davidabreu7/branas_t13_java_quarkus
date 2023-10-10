package com.branas.domain.valueObjects.rideStatus;

import com.branas.domain.entities.Ride;
import com.branas.domain.enums.RideStateEnum;
import com.branas.infrastructure.exceptions.ValidationErrorException;

public class RideStatusAccepted implements RideStatus {

    private final RideStateEnum value;
    public RideStatusAccepted() {
        value = RideStateEnum.ACCEPTED;
    }

    @Override
    public RideStatus process(RideStatus rideStatus) {
        if (!rideStatus.getRideStatus().equals(RideStateEnum.ACCEPTED)){
            throw new ValidationErrorException("Ride is not accepted");
        }
        return new RideStatusStarted();
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
