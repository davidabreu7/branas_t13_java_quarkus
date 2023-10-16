package com.branas.domain.usecases.ride;

import com.branas.api.ports.AccountDAO;
import com.branas.api.ports.RideRepository;
import com.branas.domain.entities.Account;
import com.branas.domain.entities.Ride;
import com.branas.domain.enums.RideStateEnum;
import com.branas.domain.valueObjects.Cpf;
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
class AcceptRideTest {

    @Inject
    AcceptRide acceptRide;
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
    void shouldAccepRide() {
        Ride ride = Ride.create(
                PASSENGER_ID,
                FROM_COORDINATE,
                TO_COORDINATE
        );
        VALID_EMAIL = "john.doe%d@gmail.com".formatted(System.currentTimeMillis());
        Account driver =  Account.create(
                VALID_NAME.value(),
                VALID_EMAIL,
                new Cpf(VALID_CPF.value()),
                VALID_PLATE.value(),
                false,
                true
        );
        when(rideRepository.getRideById(any(UUID.class)))
                .thenReturn(Optional.of(ride));
        when(accountDAO.getAccountById(any(UUID.class)))
                .thenReturn(Optional.of(driver));
        Ride acceptedRide = acceptRide.execute(ride.getRideId().toString(), driver.getAccountId().toString());
        assertThat(acceptedRide)
                .isNotNull()
                .isInstanceOf(Ride.class)
                .hasFieldOrPropertyWithValue("status", RideStateEnum.ACCEPTED)
                .hasFieldOrPropertyWithValue("driverId", driver.getAccountId());
    }

    @Test
    void shouldNotAcceptRideWhenRideNotFound() {
        Account driver = Account.create(
                VALID_NAME.value(),
                VALID_EMAIL,
                new Cpf(VALID_CPF.value()),
                VALID_PLATE.value(),
                false,
                true
        );

        when(rideRepository.getRideById(any(UUID.class)))
                .thenReturn(Optional.empty());
        when(accountDAO.getAccountById(any(UUID.class)))
                .thenReturn(Optional.of(driver));
        String driverId = UUID.randomUUID().toString();
        String rideId = UUID.randomUUID().toString();
        assertThatThrownBy(() -> acceptRide.execute(driverId, rideId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ride not found");
    }

    @Test
    void shouldNotAcceptRideWhenDriverIsNotAvailable() {
        Ride ride = Ride.create(
                PASSENGER_ID,
                FROM_COORDINATE,
                TO_COORDINATE
        );
        Ride newRide = Ride.create(
                PASSENGER_ID,
                FROM_COORDINATE,
                TO_COORDINATE
        );
        when(rideRepository.getRideByPassengerId(any(UUID.class)))
                .thenReturn(newRide);
        when(rideRepository.getRideById(any(UUID.class)))
                .thenReturn(Optional.of(ride));
        VALID_EMAIL = "john.doe%d@gmail.com".formatted(System.currentTimeMillis());
        Account driver = Account.create(
                VALID_NAME.value(),
                VALID_EMAIL,
                new Cpf(VALID_CPF.value()),
                VALID_PLATE.value(),
                false,
                true
        );
        driver.setAccountId(UUID.randomUUID());
        when(rideRepository.getRideById(any(UUID.class)))
                .thenReturn(Optional.of(ride));
        when(accountDAO.getAccountById(any(UUID.class)))
                .thenReturn(Optional.of(driver));
        when(rideRepository.getRideByDriverId(any(UUID.class)))
                .thenReturn(ride);
        String driverId = newRide.getRideId().toString();
        String rideId = driver.getAccountId().toString();
        assertThatThrownBy(() -> acceptRide.execute(driverId, rideId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Driver already has a ride");
    }

}