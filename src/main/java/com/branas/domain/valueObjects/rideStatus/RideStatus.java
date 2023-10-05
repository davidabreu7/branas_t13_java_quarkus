package com.branas.domain.valueObjects.rideStatus;

import com.branas.domain.enums.RideStateEnum;

public interface RideStatus {

    public RideStatus process(RideStatus rideStatus);
    public RideStatus cancel(RideStatus rideStatus);
    public RideStateEnum getRideStatus();
}
