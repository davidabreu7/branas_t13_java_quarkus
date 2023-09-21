package com.branas.domain.ports;

import com.branas.domain.entities.Ride;

import java.util.UUID;

public interface RideDAO {

    public void save(Ride ride) throws Exception;
    public Ride getRideById(UUID rideId) throws Exception;
    public void update(Ride ride) throws Exception;
    public Ride getRideByPassengerId(UUID passengerId) throws Exception;
}
