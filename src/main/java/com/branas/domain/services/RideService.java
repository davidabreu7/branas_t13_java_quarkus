package com.branas.domain.services;

import com.branas.domain.DTO.RidePath;
import com.branas.domain.entities.Account;
import com.branas.domain.entities.Ride;
import com.branas.domain.ports.AccountDAO;
import com.branas.domain.ports.RideDAO;
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
                throw new RuntimeException("Account not found");
            }
            if (account.isDriver() || !account.isPassenger()) {
                throw new RuntimeException("Driver cannot request a ride");
            }
           Ride latestRide = rideDAO.getRideByPassengerId(account.getAccountId());
            if (latestRide != null && !latestRide.getStatus().equals("COMPLETED")) {
                throw new RuntimeException("Passenger already has a requested ride");
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

        public Ride acceptRide(String rideId) {
        return null;
    }

    public Ride getRideById(String rideId) {
        return null;
    }
}
