package com.branas.domain.usecases;

import com.branas.api.ports.AccountDAO;
import com.branas.api.ports.RideDAO;
import com.branas.domain.DTO.RidePath;
import com.branas.domain.entities.Account;
import com.branas.domain.entities.Ride;
import com.branas.infrastructure.exceptions.ResourceNotFoundException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static com.branas.utils.RideTestValues.*;
import static com.branas.utils.TestValues.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@QuarkusTest
class RequestRideTest {

    @Inject
    RequestRide requestRide;
    @InjectMock
    RideDAO rideDAO;
    @InjectMock
    AccountDAO accountDAO;
    String VALID_EMAIL;
    Account account;

    @BeforeEach
    void setUp() {
        VALID_EMAIL = "john.doe%d@gmail.com".formatted(System.currentTimeMillis());
        account = Account.create(
                VALID_NAME.value(),
                VALID_EMAIL,
                VALID_CPF.value(),
                VALID_PLATE.value(),
                true,
                false
        );
        Ride ride = Ride.create(
                PASSENGER_ID,
                FROM_COORDINATE,
                TO_COORDINATE
        );
        doNothing()
                .when(rideDAO)
                .save(any(Ride.class));
        when(rideDAO.getRideById(any(UUID.class)))
                .thenReturn(Optional.of(ride));
    }

    @Test
    void shouldRequestRide() {
        when(accountDAO.getAccountById(any(UUID.class)))
                .thenReturn(account);
        Ride ride = requestRide.excecute(account.getAccountId().toString(),
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
    void shouldNoteCreateRideWhenAccountNotFound() {
        when(accountDAO.getAccountById(any(UUID.class)))
                .thenReturn(null);
        String existingAccount = account.getAccountId().toString();
        RidePath path =  new RidePath(FROM_COORDINATE, TO_COORDINATE);
        assertThatThrownBy(() -> requestRide.excecute(existingAccount, path))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Account not found");
    }

    @Test
    void shouldNotCreateRideWhenAccountIsNotPassenger() {
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
        String existingAccount = account.getAccountId().toString();
        RidePath path =  new RidePath(FROM_COORDINATE, TO_COORDINATE);
        assertThatThrownBy(() -> requestRide.excecute(existingAccount, path))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Driver cannot request a ride");
    }

    @Test
    void ShouldNotCreateRideWhenExistsRideWithStatusDifferentFromCompleted() {
        Ride ride = Ride.create(
                PASSENGER_ID,
                FROM_COORDINATE,
                TO_COORDINATE
        );
        when(rideDAO.getRideByPassengerId(any(UUID.class)))
                .thenReturn(ride);
        when(accountDAO.getAccountById(any(UUID.class)))
                .thenReturn(account);
        String existingAccount = account.getAccountId().toString();
        RidePath path =  new RidePath(FROM_COORDINATE, TO_COORDINATE);
        assertThatThrownBy(() -> requestRide.excecute(existingAccount, path))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Passenger already has a requested ride");
    }
}