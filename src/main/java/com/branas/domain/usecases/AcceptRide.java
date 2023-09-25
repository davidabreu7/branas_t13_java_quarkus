package com.branas.domain.usecases;

import com.branas.api.ports.AccountDAO;
import com.branas.api.ports.RideDAO;
import com.branas.domain.entities.Account;
import com.branas.domain.entities.Ride;
import com.branas.infrastructure.exceptions.ResourceNotFoundException;
import com.branas.infrastructure.exceptions.ValidationErrorException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class AcceptRide {

    @Inject
    RideDAO rideDAO;
    @Inject
    AccountDAO accountDAO;

    public Ride exceute(String driverId, String rideId) {
        Account driver = accountDAO.getAccountById(UUID.fromString(driverId));
        Ride ride = rideDAO.getRideById(UUID.fromString(rideId))
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));
        if (driver == null) {
            throw new ResourceNotFoundException("Driver not found");
        }
        if (!driver.isDriver()) {
            throw new ValidationErrorException("Account is not a driver");
        }
        if (rideDAO.getRideByDriverId(driver.getAccountId()) != null) {
            throw new ValidationErrorException("Driver already has a ride");
        }
        ride.accept(driver.getAccountId());
        rideDAO.update(ride);
        return ride;
    }
}