package com.branas.domain.usecases.account;

import com.branas.api.ports.AccountDAO;
import com.branas.domain.DTO.AccountInput;
import com.branas.domain.entities.Account;
import com.branas.domain.usecases.account.AccountSignup;
import com.branas.domain.valueObjects.Cpf;
import com.branas.infrastructure.exceptions.ResourceNotFoundException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static com.branas.utils.TestValues.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@QuarkusTest
class AccountSignupTest {

    static String VALID_EMAIL;
    @InjectMock
    AccountDAO accountDAO;
    @Inject
    AccountSignup accountSignup;

    @BeforeEach
    void setup() {
        VALID_EMAIL = "john.doe%d@gmail.com".formatted(System.currentTimeMillis());

    }

    @Test
    void ShouldCreatePassanger() {
       AccountInput validPassenger = AccountInput.builder()
                .isPassenger(true)
                .name(VALID_NAME.value())
                .email(VALID_EMAIL)
                .cpf(VALID_CPF.value())
                .build();

        Account account =  Account.create(
                validPassenger.name(),
                validPassenger.email(),
                new Cpf(validPassenger.cpf()),
                validPassenger.carPlate(),
                validPassenger.isPassenger(),
                validPassenger.isDriver()
        );

       when(accountDAO.getAccountByEmail(validPassenger.email()))
               .thenReturn(null);
        UUID accountId = accountSignup.execute(validPassenger);
        account.setAccountId(accountId);
        when(accountDAO.getAccountById(accountId))
                .thenReturn(Optional.of(account));
        Account accountSaved = accountDAO.getAccountById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        assertThat(accountSaved).isNotNull()
                .hasFieldOrPropertyWithValue("accountId", accountId)
                .hasFieldOrPropertyWithValue("isPassenger", validPassenger.isPassenger())
                .hasFieldOrPropertyWithValue("email", validPassenger.email());
    }

    @Test
    void ShouldNotCreatePassengerWithInvalidCpf() {
        AccountInput input = AccountInput.builder()
                .isPassenger(true)
                .name(VALID_NAME.value())
                .email(VALID_EMAIL)
                .cpf(INVALID_CPF.value())
                .build();
        when(accountDAO.getAccountByEmail(input.email()))
                .thenReturn(null);
        try {
           accountSignup.execute(input);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(Exception.class)
                    .hasMessageContaining("Invalid CPF");
        }
    }
    @Test
    void ShouldNotCreatePassengerWithInvalidName() {
        AccountInput input = AccountInput.builder()
                .isPassenger(true)
                .name(INVALID_NAME.value())
                .email(VALID_EMAIL)
                .cpf(VALID_CPF.value())
                .build();
        when(accountDAO.getAccountByEmail(input.email()))
                .thenReturn(null);
        try {
            accountSignup.execute(input);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(Exception.class)
                    .hasMessageContaining("Invalid name");
        }
    }
    @Test
    void ShouldNotCreatePassengerWithInvalidEmail() {
        AccountInput input = AccountInput.builder()
                .isPassenger(true)
                .name(VALID_NAME.value())
                .email(INVALID_EMAIL.value())
                .cpf(VALID_CPF.value())
                .build();
        when(accountDAO.getAccountByEmail(input.email()))
                .thenReturn(null);
        try {
            accountSignup.execute(input);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(Exception.class)
                    .hasMessageContaining("Invalid email");
        }
    }
    @Test
    void ShouldNotCreatePassengerWithExistingAccount() {
        AccountInput input = AccountInput.builder()
                .isPassenger(true)
                .name(VALID_NAME.value())
                .email(ACCOUNT_EXISTS.value())
                .cpf(VALID_CPF.value())
                .build();
        Account account = Account.create(
                input.name(),
                input.email(),
                new Cpf(input.cpf()),
                input.carPlate(),
                input.isPassenger(),
                input.isDriver()
        );
        when(accountDAO.getAccountByEmail(input.email()))
                .thenReturn(account);
        try {
            accountSignup.execute(input);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(Exception.class)
                    .hasMessageContaining("Account already exists");
        }
    }
    @Test
    void ShouldCreateDriver() {
       AccountInput validDriver = AccountInput.builder()
                .isDriver(true)
                .name(VALID_NAME.value())
                .email(VALID_EMAIL)
                .cpf(VALID_CPF.value())
                .carPlate(VALID_PLATE.value())
                .build();
       Account account =  Account.create(
                validDriver.name(),
                validDriver.email(),
                new Cpf(validDriver.cpf()),
                validDriver.carPlate(),
                validDriver.isPassenger(),
                validDriver.isDriver()
        );
         when(accountDAO.getAccountByEmail(validDriver.email()))
                 .thenReturn(null);
        UUID accountIdSaved = accountSignup.execute(validDriver);
        account.setAccountId(accountIdSaved);
        when(accountDAO.getAccountById(accountIdSaved))
                .thenReturn(Optional.of(account));
        Account accountSaved = accountDAO.getAccountById(accountIdSaved).orElseThrow( () -> new ResourceNotFoundException("Account not found"));
        assertThat(accountSaved)
                .isNotNull()
                .hasFieldOrPropertyWithValue("accountId", accountIdSaved)
                .hasFieldOrPropertyWithValue("carPlate", validDriver.carPlate())
                .hasFieldOrPropertyWithValue("isDriver", validDriver.isDriver())
                .hasFieldOrPropertyWithValue("email", validDriver.email());
    }
    @Test
    void ShouldNotCreateDriverWithInvalidPlate() {
        AccountInput input = AccountInput.builder()
                .isDriver(true)
                .name(VALID_NAME.value())
                .email(VALID_EMAIL)
                .cpf(VALID_CPF.value())
                .carPlate(INVALID_PLATE.value())
                .build();
        when(accountDAO.getAccountByEmail(input.email()))
                .thenReturn(null);

       try {
           accountSignup.execute(input);
         } catch (Exception e) {
           assertThat(e).isInstanceOf(Exception.class)
                   .hasMessageContaining("Invalid plate");
       }
    }
}