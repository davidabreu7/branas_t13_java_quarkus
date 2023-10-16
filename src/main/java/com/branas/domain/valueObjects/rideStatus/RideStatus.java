package com.branas.domain.valueObjects.rideStatus;

import com.branas.domain.enums.RideStateEnum;

public sealed interface RideStatus permits RideStatusAccepted, RideStatusCancelled, RideStatusFinished, RideStatusRequested, RideStatusStarted {

    public RideStatus process(RideStatus rideStatus);
    public RideStatus cancel(RideStatus rideStatus);
    public RideStateEnum getRideStatus();
}
