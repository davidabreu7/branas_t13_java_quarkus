package com.branas.domain.services;

import com.branas.domain.DTO.AccountInput;
import com.branas.domain.entities.Account;
import com.branas.utils.CpfValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    static final String VALID_NAME = "John Doe";
    static final String INVALID_NAME = "John";
    static final String VALID_CPF = "95818705552";
    static final String INVALID_CPF = "95818705500";
    static String VALID_EMAIL;
    static final String INVALID_EMAIL = "john.doe@gmail";
    static final String ACCOUNT_EXISTS = "jonh.doe@gmail.com";
    static final String VALID_PLATE = "ABC1234";
    static final String INVALID_PLATE = "ABC12345";
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
                .name(VALID_NAME)
                .email(VALID_EMAIL)
                .cpf(VALID_CPF)
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
                .name(VALID_NAME)
                .email(VALID_EMAIL)
                .cpf(INVALID_CPF)
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
                .name(INVALID_NAME)
                .email(VALID_EMAIL)
                .cpf(VALID_CPF)
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
                .name(VALID_NAME)
                .email(INVALID_EMAIL)
                .cpf(VALID_CPF)
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
                .name(VALID_NAME)
                .email(ACCOUNT_EXISTS)
                .cpf(VALID_CPF)
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
                .name(VALID_NAME)
                .email(VALID_EMAIL)
                .cpf(VALID_CPF)
                .carPlate(VALID_PLATE)
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
                .name(VALID_NAME)
                .email(VALID_EMAIL)
                .cpf(VALID_CPF)
                .carPlate(INVALID_PLATE)
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