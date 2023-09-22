package com.branas.domain.services;

import com.branas.domain.DTO.RidePath;
import com.branas.domain.entities.Account;
import com.branas.domain.entities.Ride;
import com.branas.domain.ports.AccountDAO;
import com.branas.domain.ports.RideDAO;
import com.branas.infrastructure.exceptions.ResourceNotFoundException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static com.branas.utils.RideTestValues.*;
import static com.branas.utils.TestValues.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class RideServiceTest {

    @Inject
    RideService rideService;
    @InjectMock
    RideDAO rideDAO;
    @InjectMock
    AccountDAO accountDAO;
    String VALID_EMAIL;
    Account account;

    @BeforeEach
    void setUp() throws Exception {
        UUID accountId = UUID.randomUUID();
        VALID_EMAIL = "john.doe%d@gmail.com".formatted(System.currentTimeMillis());
        account = Account.create(
                VALID_NAME.value(),
                VALID_EMAIL,
                VALID_CPF.value(),
                VALID_PLATE.value(),
                true,
                false
        );
        Ride ride = new Ride(
                        null,
                        PASSENGER_ID,
                        DRIVER_ID,
                        STATUS,
                        PRICE,
                        DISTANCE,
                        TIMESTAMP,
                        FROM_COORDINATE,
                        TO_COORDINATE
                );
        doNothing()
                .when(rideDAO)
                .save(any(Ride.class));
        when(rideDAO.getRideById(any(UUID.class)))
                .thenReturn(ride);
    }

    @Test
    void shouldRequestRide() throws Exception {;
        when(accountDAO.getAccountById(any(UUID.class)))
                .thenReturn(account);
        Ride ride = rideService.requestRide(account.getAccountId().toString(),
                new RidePath(FROM_COORDINATE, TO_COORDINATE));
        assertThat(ride)
                .isNotNull()
                .isInstanceOf(Ride.class)
                .hasFieldOrPropertyWithValue("passengerId", account.getAccountId())
                .hasFieldOrPropertyWithValue("status", STATUS)
                .hasFieldOrPropertyWithValue("fromCoordinate", FROM_COORDINATE)
                .hasFieldOrPropertyWithValue("toCoordinate", TO_COORDINATE);
        assertThat(ride.getRideId()).isNotNull();
    }

    @Test
    void shouldNoteCreateRideWhenAccountNotFound() throws Exception {
        when(accountDAO.getAccountById(any(UUID.class)))
                .thenReturn(null);
        assertThatThrownBy(() -> rideService.requestRide(account.getAccountId().toString(),
                new RidePath(FROM_COORDINATE, TO_COORDINATE)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Account not found");
    }

    @Test
    void shouldNotCreateRideWhenAccountIsNotPassenger() throws Exception {

        Ride ride = new Ride();
        UUID accountId = UUID.randomUUID();
        Account account = Account.create(
                VALID_NAME.value(),
                VALID_EMAIL,
                VALID_CPF.value(),
                VALID_PLATE.value(),
                false,
                false
        );
        when(accountDAO.getAccountById(any(UUID.class)))
                .thenReturn(account);
        assertThatThrownBy(() -> rideService.requestRide(account.getAccountId().toString(),
                new RidePath(FROM_COORDINATE, TO_COORDINATE)))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Driver cannot request a ride");
    }

    @Test
    void ShouldNotCreateRideWhenExistsRideWithStatusDifferentFromCompleted() throws Exception {
        Ride ride = new Ride(
                UUID.randomUUID(),
                PASSENGER_ID,
                DRIVER_ID,
                "REQUESTED",
                PRICE,
                DISTANCE,
                TIMESTAMP,
                FROM_COORDINATE,
                TO_COORDINATE
        );
        when(rideDAO.getRideByPassengerId(any(UUID.class)))
                .thenReturn(ride);
        when(accountDAO.getAccountById(any(UUID.class)))
                .thenReturn(account);
        assertThatThrownBy(() -> rideService.requestRide(account.getAccountId().toString(),
                new RidePath(FROM_COORDINATE, TO_COORDINATE)))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Passenger already has a requested ride");
    }

    @Test
    void shouldAccepRide() throws Exception {
        Ride ride = new Ride(
                UUID.randomUUID(),
                PASSENGER_ID,
                DRIVER_ID,
                "REQUESTED",
                PRICE,
                DISTANCE,
                TIMESTAMP,
                FROM_COORDINATE,
                TO_COORDINATE
        );
        UUID accountId = UUID.randomUUID();
        VALID_EMAIL = "john.doe%d@gmail.com".formatted(System.currentTimeMillis());
        Account driver =  Account.create(
                VALID_NAME.value(),
                VALID_EMAIL,
                VALID_CPF.value(),
                VALID_PLATE.value(),
                false,
                true
        );
        when(rideDAO.getRideById(any(UUID.class)))
                .thenReturn(ride);
when(accountDAO.getAccountById(any(UUID.class)))
                .thenReturn(driver);
        Ride acceptedRide = rideService.acceptRide(ride.getRideId().toString(), driver.getAccountId().toString());
        assertThat(acceptedRide)
                .isNotNull()
                .isInstanceOf(Ride.class)
                .hasFieldOrPropertyWithValue("status", "ACCEPTED")
                .hasFieldOrPropertyWithValue("driverId", driver.getAccountId());
    }

    @Test
    void shouldNotAcceptRideWhenRideNotFound() throws Exception {
        UUID accountId = UUID.randomUUID();
        Account driver = Account.create(
                VALID_NAME.value(),
                VALID_EMAIL,
                VALID_CPF.value(),
                VALID_PLATE.value(),
                false,
                true
        );

        when(rideDAO.getRideById(any(UUID.class)))
                .thenReturn(null);
        when(accountDAO.getAccountById(any(UUID.class)))
                .thenReturn(driver);
        assertThatThrownBy(() -> rideService.acceptRide(UUID.randomUUID().toString(), UUID.randomUUID().toString()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ride not found");
    }

    @Test
    void shouldNotAcceptRideWhenDriverIsNotAvailable() throws Exception {
        UUID driverId = UUID.randomUUID();
        Ride ride = new Ride(
                UUID.randomUUID(),
                PASSENGER_ID,
                driverId,
                "REQUESTED",
                PRICE,
                DISTANCE,
                TIMESTAMP,
                FROM_COORDINATE,
                TO_COORDINATE
        );
        Ride newRide = new Ride(
                UUID.randomUUID(),
                PASSENGER_ID,
                null,
                "REQUESTED",
                PRICE,
                DISTANCE,
                TIMESTAMP,
                FROM_COORDINATE,
                TO_COORDINATE
        );
        when(rideDAO.getRideByPassengerId(any(UUID.class)))
                .thenReturn(newRide);
        when(rideDAO.getRideById(any(UUID.class)))
                .thenReturn(ride);
        VALID_EMAIL = "john.doe%d@gmail.com".formatted(System.currentTimeMillis());
        Account driver = Account.create(
                VALID_NAME.value(),
                VALID_EMAIL,
                VALID_CPF.value(),
                VALID_PLATE.value(),
                false,
                true
        );
        driver.setAccountId(driverId);
        when(rideDAO.getRideById(any(UUID.class)))
                .thenReturn(ride);
        when(accountDAO.getAccountById(any(UUID.class)))
                .thenReturn(driver);
        when(rideDAO.getRideByDriverId(any(UUID.class)))
                .thenReturn(ride);
        assertThatThrownBy(() -> rideService.acceptRide(newRide.getRideId().toString(), driver.getAccountId().toString()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Driver already has a ride");
    }
}
