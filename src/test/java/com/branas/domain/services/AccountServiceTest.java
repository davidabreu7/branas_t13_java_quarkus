package com.branas.domain.services;

import com.branas.domain.DTO.AccountInput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Test
    void ShouldCreatePassanger() throws Exception {
        //given
        AccountInput input = AccountInput.builder()
                .isPassenger(true)
                .name("John Doe")
                .email("john.doe%d@gmail.com".formatted(System.currentTimeMillis()))
                .cpf("95818705552")
                .build();
        //when
        AccountService accountService = new AccountService();
        UUID accountId = accountService.signup(input);
        //then
        assertThat(accountId).isNotNull();
    }

    @Test
    void ShouldCreateDriver() throws Exception {
        //given
        AccountInput input = AccountInput.builder()
                .isDriver(true)
                .name("John Doe")
                .email("john.doe%d@gmail.com".formatted(System.currentTimeMillis()))
                .cpf("95818705552")
                .carPlate("ABC1234")
                .build();
        //when
        AccountService accountService = new AccountService();
        UUID accountId = accountService.signup(input);
        //then
        assertThat(accountId).isNotNull();
    }


}