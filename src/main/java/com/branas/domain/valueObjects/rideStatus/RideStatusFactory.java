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
        switch (rideStatusFactory.rideState) {
            case REQUESTED -> {
                return new RideStatusRequested();
            }
            case ACCEPTED -> {
                return new RideStatusAccepted();
            }
            case STARTED -> {
                return new RideStatusStarted();
            }
            case FINISHED -> {
                return new RideStatusFinished();
            }
            case CANCELLED -> {
                return new RideStatusCancelled();
            }
            default -> throw new IllegalArgumentException("Invalid status");
        }

    }

}
