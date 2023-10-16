package com.branas.domain.usecases.ride;

import com.branas.api.ports.AccountDAO;
import com.branas.api.ports.RideRepository;
import com.branas.domain.DTO.RidePath;
import com.branas.domain.entities.Account;
import com.branas.domain.entities.Ride;
import com.branas.domain.valueObjects.Cpf;
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
    RideRepository rideRepository;
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
                new Cpf(VALID_CPF.value()),
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
                .when(rideRepository)
                .save(any(Ride.class));
        when(rideRepository.getRideById(any(UUID.class)))
                .thenReturn(Optional.of(ride));
    }

    @Test
    void shouldRequestRide() {
        when(accountDAO.getAccountById(any(UUID.class)))
                .thenReturn(Optional.ofNullable(account));
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
                .thenReturn(Optional.empty());
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
                new Cpf(VALID_CPF.value()),
                VALID_PLATE.value(),
                false,
                false
        );
        when(accountDAO.getAccountById(any(UUID.class)))
                .thenReturn(Optional.of(account));
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
        when(rideRepository.getRideByPassengerId(any(UUID.class)))
                .thenReturn(ride);
        when(accountDAO.getAccountById(any(UUID.class)))
                .thenReturn(Optional.of(account));
        String existingAccount = account.getAccountId().toString();
        RidePath path =  new RidePath(FROM_COORDINATE, TO_COORDINATE);
        assertThatThrownBy(() -> requestRide.excecute(existingAccount, path))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Passenger already has a requested ride");
    }
}