package com.branas.domain.valueObjects.rideStatus;

import com.branas.domain.enums.RideStateEnum;

public class RideStatusFactory {

    String status;
    RideStateEnum rideState;

    private RideStatusFactory(String status) {
        this.status = status;
        rideState = RideStateEnum.valueOf(status);
    }

    public static RideStatus createStatus(String status) {

        RideStatusFactory rideStatusFactory = new RideStatusFactory(status);
        return switch (rideStatusFactory.rideState) {
            case REQUESTED -> new RideStatusRequested();
            case ACCEPTED -> new RideStatusAccepted();
            case STARTED ->  new RideStatusStarted();
            case FINISHED -> new RideStatusFinished();
            case CANCELLED -> new RideStatusCancelled();
        };
    }
}
