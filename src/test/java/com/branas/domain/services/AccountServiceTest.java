package com.branas.domain.services;

import com.branas.domain.DTO.AccountInput;
import com.branas.domain.entities.Account;
import com.branas.utils.CpfValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.branas.utils.TestValues.*;
import static org.assertj.core.api.Assertions.assertThat;

class AccountServiceTest {

    static String VALID_EMAIL;
    AccountService accountService;
    @BeforeEach
    void setup() {
        accountService = new AccountService();
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
        //when
        UUID accountId = accountService.signup(validPassenger);
        Account account = accountService.getAccount(accountId);
        //then
        assertThat(account).isNotNull()
                .hasFieldOrPropertyWithValue("accountId", accountId)
                .hasFieldOrPropertyWithValue("isPassenger", validPassenger.isPassenger())
                .hasFieldOrPropertyWithValue("email", validPassenger.email());
    }

    @Test
    void ShouldNotCreatePassengerWithInvalidCpf() {
        //given
        AccountInput input = AccountInput.builder()
                .isPassenger(true)
                .name(VALID_NAME.value())
                .email(VALID_EMAIL)
                .cpf(INVALID_CPF.value())
                .build();
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
    void ShouldNotCreatePassengerWithInvalidName() {
        //given
        AccountInput input = AccountInput.builder()
                .isPassenger(true)
                .name(INVALID_NAME.value())
                .email(VALID_EMAIL)
                .cpf(VALID_CPF.value())
                .build();
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
    void ShouldNotCreatePassengerWithInvalidEmail() {
        //given
        AccountInput input = AccountInput.builder()
                .isPassenger(true)
                .name(VALID_NAME.value())
                .email(INVALID_EMAIL.value())
                .cpf(VALID_CPF.value())
                .build();
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
    void ShouldNotCreatePassengerWithExistingAccount() {
        //given
        AccountInput input = AccountInput.builder()
                .isPassenger(true)
                .name(VALID_NAME.value())
                .email(ACCOUNT_EXISTS.value())
                .cpf(VALID_CPF.value())
                .build();
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
        //when
        UUID accountId = accountService.signup(validDriver);
        Account account = accountService.getAccount(accountId);
        //then
        assertThat(account)
                .isNotNull()
                .hasFieldOrPropertyWithValue("accountId", accountId)
                .hasFieldOrPropertyWithValue("carPlate", validDriver.carPlate())
                .hasFieldOrPropertyWithValue("isDriver", validDriver.isDriver())
                .hasFieldOrPropertyWithValue("email", validDriver.email());
    }
    @Test
    void ShouldNotCreateDriverWithInvalidPlate() throws Exception {
        //given
        AccountInput input = AccountInput.builder()
                .isDriver(true)
                .name(VALID_NAME.value())
                .email(VALID_EMAIL)
                .cpf(VALID_CPF.value())
                .carPlate(INVALID_PLATE.value())
                .build();
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