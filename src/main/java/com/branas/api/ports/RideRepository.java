package com.branas.api.ports;

import com.branas.domain.entities.Ride;

import java.util.Optional;
import java.util.UUID;

public interface RideRepository {

    public void save(Ride ride);
    public Optional<Ride> getRideById(UUID rideId);
    public void update(Ride ride);
    public Ride getRideByPassengerId(UUID passengerId);
    public Ride getRideByDriverId(UUID driverId);
}
