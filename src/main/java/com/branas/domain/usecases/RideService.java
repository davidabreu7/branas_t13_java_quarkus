package com.branas.domain.usecases;

import com.branas.domain.DTO.RidePath;
import com.branas.domain.entities.Account;
import com.branas.domain.entities.Ride;
import com.branas.api.ports.AccountDAO;
import com.branas.api.ports.RideDAO;
import com.branas.infrastructure.exceptions.ResourceNotFoundException;
import com.branas.infrastructure.exceptions.ValidationErrorException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class RideService {

    @Inject
    RideDAO rideDAO;
    @Inject
    AccountDAO accountDAO;

    public Ride requestRide(String accountId, RidePath ridePath) {
        Account account = accountDAO.getAccountById(UUID.fromString(accountId));
        validateAccount(account);
        Ride ride = Ride.create(
                account.getAccountId(),
                ridePath.fromCoordinate(),
                ridePath.toCoordinate()
        );
        rideDAO.save(ride);
    return ride;
    }

    private void validateAccount(Account account) {
        if (account == null) {
            throw new ResourceNotFoundException("Account not found");
        }
        if (account.isDriver() || !account.isPassenger()) {
            throw new ValidationErrorException("Driver cannot request a ride");
        }
        Ride latestRide = rideDAO.getRideByPassengerId(account.getAccountId());
        if (latestRide != null && !latestRide.getStatus().equals("COMPLETED")) {
            throw new ValidationErrorException("Passenger already has a requested ride");
        }
    }

    public Ride acceptRide(String driverId, String rideId) {
        Account driver = accountDAO.getAccountById(UUID.fromString(driverId));
        Ride ride = rideDAO.getRideById(UUID.fromString(rideId))
                .orElseThrow( () -> new ResourceNotFoundException("Ride not found"));
        if (driver == null) {
            throw new ResourceNotFoundException("Driver not found");
        }
        if (!driver.isDriver()) {
            throw new ValidationErrorException("Account is not a driver");
        }
        if(rideDAO.getRideByDriverId(driver.getAccountId()) != null) {
            throw new ValidationErrorException("Driver already has a ride");
        }
        ride.accept(driver.getAccountId());
        rideDAO.update(ride);
    return ride;
}

    public Ride getRideById(String rideId) {
        return rideDAO.getRideById(UUID.fromString(rideId))
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));
    }
}
