package com.branas.domain.usecases.ride;

import com.branas.api.ports.AccountDAO;
import com.branas.api.ports.RideRepository;
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
    RideRepository rideRepository;
    @Inject
    AccountDAO accountDAO;

    public Ride execute(String driverId, String rideId) {
        Account driver = accountDAO.getAccountById(UUID.fromString(driverId))
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        Ride ride = rideRepository.getRideById(UUID.fromString(rideId))
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));
        if (driver == null) {
            throw new ResourceNotFoundException("Driver not found");
        }
        if (!driver.isDriver()) {
            throw new ValidationErrorException("Account is not a driver");
        }
        if (rideRepository.getRideByDriverId(driver.getAccountId()) != null) {
            throw new ValidationErrorException("Driver already has a ride");
        }
        ride.accept(driver.getAccountId());
        rideRepository.update(ride);
        return ride;
    }
}