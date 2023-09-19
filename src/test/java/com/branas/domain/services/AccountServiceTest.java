package com.branas.domain.services;

import com.branas.domain.DTO.AccountInput;
import com.branas.domain.entities.Account;
import com.branas.domain.ports.AccountDAO;
import com.branas.utils.CpfValidator;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static com.branas.utils.TestValues.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@QuarkusTest
class AccountServiceTest {

    static String VALID_EMAIL;
    @InjectMock
    AccountDAO accountDAO;
    @Inject
    AccountService accountService;

    @BeforeEach
    void setup() throws Exception {
        VALID_EMAIL = "john.doe%d@gmail.com".formatted(System.currentTimeMillis());

    }

    @Test
    void ShouldCreatePassanger() throws Exception {
        //given
       AccountInput validPassenger = AccountInput.builder()
                .isPassenger(true)
                .name(VALID_NAME.value())
                .email(VALID_EMAIL)
                .cpf(VALID_CPF.value())
                .build();

        Account account =  new Account(
                null,
                validPassenger.name(),
                validPassenger.email(),
                validPassenger.cpf(),
                validPassenger.carPlate(),
                validPassenger.isPassenger(),
                validPassenger.isDriver(),
                new Date(),
                false,
                UUID.randomUUID()
        );

       when(accountDAO.getAccountByEmail(validPassenger.email()))
               .thenReturn(null);
        //when
        UUID accountId = accountService.signup(validPassenger);
        account.setAccountId(accountId);
        when(accountDAO.getAccountById(accountId))
                .thenReturn(account);
        Account accountSaved = accountService.getAccount(accountId);
        //then
        assertThat(accountSaved).isNotNull()
                .hasFieldOrPropertyWithValue("accountId", accountId)
                .hasFieldOrPropertyWithValue("isPassenger", validPassenger.isPassenger())
                .hasFieldOrPropertyWithValue("email", validPassenger.email());
    }

    @Test
    void ShouldNotCreatePassengerWithInvalidCpf() throws Exception {
        //given
        AccountInput input = AccountInput.builder()
                .isPassenger(true)
                .name(VALID_NAME.value())
                .email(VALID_EMAIL)
                .cpf(INVALID_CPF.value())
                .build();
        when(accountDAO.getAccountByEmail(input.email()))
                .thenReturn(null);
        //when
        try {
           accountService.signup(input);
        } catch (Exception e) {
            //then
            assertThat(CpfValidator.validateCpf(input.cpf())).isFalse();
            assertThat(e).isInstanceOf(Exception.class)
                    .hasMessageContaining("Invalid cpf");
        }
    }
    @Test
    void ShouldNotCreatePassengerWithInvalidName() throws Exception {
        //given
        AccountInput input = AccountInput.builder()
                .isPassenger(true)
                .name(INVALID_NAME.value())
                .email(VALID_EMAIL)
                .cpf(VALID_CPF.value())
                .build();
        when(accountDAO.getAccountByEmail(input.email()))
                .thenReturn(null);
        //when
        try {
            accountService.signup(input);
        } catch (Exception e) {
            //then
            assertThat(e).isInstanceOf(Exception.class)
                    .hasMessageContaining("Invalid name");
        }
    }
    @Test
    void ShouldNotCreatePassengerWithInvalidEmail() throws Exception {
        //given
        AccountInput input = AccountInput.builder()
                .isPassenger(true)
                .name(VALID_NAME.value())
                .email(INVALID_EMAIL.value())
                .cpf(VALID_CPF.value())
                .build();
        when(accountDAO.getAccountByEmail(input.email()))
                .thenReturn(null);
        //when
        try {
            accountService.signup(input);
        } catch (Exception e) {
            //then
            assertThat(e).isInstanceOf(Exception.class)
                    .hasMessageContaining("Invalid email");
        }
    }
    @Test
    void ShouldNotCreatePassengerWithExistingAccount() throws Exception {
        //given
        AccountInput input = AccountInput.builder()
                .isPassenger(true)
                .name(VALID_NAME.value())
                .email(ACCOUNT_EXISTS.value())
                .cpf(VALID_CPF.value())
                .build();
        when(accountDAO.getAccountByEmail(input.email()))
                .thenReturn(new Account());
        //when
        try {
            accountService.signup(input);
        } catch (Exception e) {
            //then
            assertThat(e).isInstanceOf(Exception.class)
                    .hasMessageContaining("Account already exists");
        }
    }
    @Test
    void ShouldCreateDriver() throws Exception {
        //given
       AccountInput validDriver = AccountInput.builder()
                .isDriver(true)
                .name(VALID_NAME.value())
                .email(VALID_EMAIL)
                .cpf(VALID_CPF.value())
                .carPlate(VALID_PLATE.value())
                .build();
       Account account =  new Account(
               null,
                validDriver.name(),
                validDriver.email(),
                validDriver.cpf(),
                validDriver.carPlate(),
                validDriver.isPassenger(),
                validDriver.isDriver(),
                new Date(),
                false,
                UUID.randomUUID()
        );
         when(accountDAO.getAccountByEmail(validDriver.email()))
                 .thenReturn(null);
        //when
        UUID accountIdSaved = accountService.signup(validDriver);
        account.setAccountId(accountIdSaved);
        when(accountDAO.getAccountById(accountIdSaved))
                .thenReturn(account);
        Account accountSaved = accountService.getAccount(accountIdSaved);
        //then
        assertThat(accountSaved)
                .isNotNull()
                .hasFieldOrPropertyWithValue("accountId", accountIdSaved)
                .hasFieldOrPropertyWithValue("carPlate", validDriver.carPlate())
                .hasFieldOrPropertyWithValue("isDriver", validDriver.isDriver())
                .hasFieldOrPropertyWithValue("email", validDriver.email());
    }
    @Test
    void ShouldNotCreateDriverWithInvalidPlate() throws Exception {
        //given
        UUID accountId = UUID.randomUUID();
        AccountInput input = AccountInput.builder()
                .isDriver(true)
                .name(VALID_NAME.value())
                .email(VALID_EMAIL)
                .cpf(VALID_CPF.value())
                .carPlate(INVALID_PLATE.value())
                .build();
        when(accountDAO.getAccountByEmail(input.email()))
                .thenReturn(null);

        //when
       try {
           accountService.signup(input);
         } catch (Exception e) {
           //then
           assertThat(e).isInstanceOf(Exception.class)
                   .hasMessageContaining("Invalid plate");
       }
    }
}