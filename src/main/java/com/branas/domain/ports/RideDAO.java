package com.branas.domain.ports;

import com.branas.domain.entities.Ride;

import java.util.UUID;

public interface RideDAO {

    public void save(Ride ride);
    public Ride getRideById(UUID rideId);
    public void update(Ride ride);
    public Ride getRideByPassengerId(UUID passengerId);
    public Ride getRideByDriverId(UUID driverId);
}
