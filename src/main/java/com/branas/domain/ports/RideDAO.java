package com.branas.domain.ports;

import com.branas.domain.entities.Ride;

public interface RideDAO {

    public void save(Ride ride) throws Exception;
    public Ride getRideById(String rideId) throws Exception;
    public void update(Ride ride) throws Exception;
}
