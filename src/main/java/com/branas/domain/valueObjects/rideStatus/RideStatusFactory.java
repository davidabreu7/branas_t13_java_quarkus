package com.branas.domain.valueObjects.rideStatus;

import com.branas.domain.entities.Ride;
import com.branas.domain.enums.RideStateEnum;

public class RideStatusFactory {

    String status;
    RideStateEnum rideState;

    private RideStatusFactory(String status) {
        this.status = status;
        rideState = RideStateEnum.valueOf(status);
    }

    public static RideStatus createStatus(Ride ride, String status) {

        RideStatusFactory rideStatusFactory = new RideStatusFactory(status);
        switch (rideStatusFactory.rideState) {
            case REQUESTED -> {
                return new RideStatusRequested(ride);
            }
            case ACCEPTED -> {
                return new RideStatusAccepted(ride);
            }
            case STARTED -> {
                return new RideStatusStarted(ride);
            }
            case FINISHED -> {
                return new RideStatusFinished(ride);
            }
            case CANCELLED -> {
                return new RideStatusCancelled(ride);
            }
            default -> throw new IllegalArgumentException("Invalid status");
        }

    }

}
