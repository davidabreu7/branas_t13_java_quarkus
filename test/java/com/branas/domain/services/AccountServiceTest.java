package com.branas.domain.services;

import com.branas.domain.DTO.AccountInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Test
    void sendEmail() {
    }

    @Test
    void validateCpf() {
    }

    @Test
    void ShouldCreatePassanger() throws Exception {
        //given
        AccountInput input = new AccountInput(
                "John Doe",
                "john.doe@gmail.com",
                "95818705552",
                false);
        //when
        AccountService accountService = new AccountService();
        UUID accountId = accountService.signup(input);
        //then
        Assertions.assertNotNull(accountId);

    }

    @Test
    void getAccount() {
    }
}