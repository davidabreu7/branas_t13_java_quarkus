package com.branas.domain.services;

import com.branas.domain.DTO.RidePath;
import com.branas.domain.entities.Account;
import com.branas.domain.entities.Ride;
import com.branas.domain.ports.AccountDAO;
import com.branas.domain.ports.RideDAO;
import com.branas.infrastructure.exceptions.ResourceNotFoundException;
import com.branas.infrastructure.exceptions.ValidationErrorException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
public class RideService {

    @Inject
    RideDAO rideDAO;
    @Inject
    AccountDAO accountDAO;

    public Ride requestRide(String accountId, RidePath ridePath) throws Exception {
        Account account = null;
        Ride ride;

            account = accountDAO.getAccountById(UUID.fromString(accountId));
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
                ride = new Ride(
                        UUID.randomUUID(),
                        account.getAccountId(),
                        null,
                        "REQUESTED",
                        null,
                        null,
                        LocalDateTime.now(),
                        ridePath.fromCoordinate(),
                        ridePath.toCoordinate()
                );
                rideDAO.save(ride);
        return ride;
    }

        public Ride acceptRide(String driverId, String rideId) throws Exception {
        Account driver = accountDAO.getAccountById(UUID.fromString(driverId));
        Ride ride = rideDAO.getRideById(UUID.fromString(rideId));
        if (driver == null) {
            throw new ValidationErrorException("Driver not found");
        }
        if (!driver.isDriver()) {
            throw new ValidationErrorException("Account is not a driver");
        }
        if (ride == null) {
            throw new ValidationErrorException("Ride not found");
        }
        if (!ride.getStatus().equals("REQUESTED")) {
            throw new ValidationErrorException("Ride is not requested");
        }
        if(rideDAO.getRideByDriverId(driver.getAccountId()) != null) {
            throw new ValidationErrorException("Driver already has a ride");
        }
        ride.setDriverId(driver.getAccountId());
        ride.setStatus("ACCEPTED");
        rideDAO.update(ride);
        return ride;
    }

    public Ride getRideById(String rideId) {
        return null;
    }
}
