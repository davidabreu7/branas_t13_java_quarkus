package com.branas.domain.usecases.Ride;

import com.branas.api.ports.AccountDAO;
import com.branas.api.ports.RideDAO;
import com.branas.domain.DTO.RidePath;
import com.branas.domain.entities.Account;
import com.branas.domain.entities.Ride;
import com.branas.infrastructure.exceptions.ResourceNotFoundException;
import com.branas.infrastructure.exceptions.ValidationErrorException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class RequestRide {

    @Inject
    RideDAO rideDAO;
    @Inject
    AccountDAO accountDAO;

    public Ride excecute(String accountId, RidePath ridePath) {
        Account account = accountDAO.getAccountById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
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
}
