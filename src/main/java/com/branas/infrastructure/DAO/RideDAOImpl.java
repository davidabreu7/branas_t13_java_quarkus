package com.branas.infrastructure.DAO;

import com.branas.domain.entities.Ride;
import com.branas.domain.ports.RideDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;

@ApplicationScoped
public class RideDAOImpl implements RideDAO {

    @Inject
    DataSource dataSource;

    @Override
    public void save(Ride ride) throws Exception {

    }

    @Override
    public Ride getRideById(String rideId) throws Exception {
        return null;
    }

    @Override
    public void update(Ride ride) throws Exception {

    }
}
