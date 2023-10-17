package com.branas.domain.usecases.account;

import com.branas.api.ports.AccountRepository;
import com.branas.domain.DTO.AccountInput;
import com.branas.domain.entities.Account;
import com.branas.domain.valueObjects.Cpf;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static com.branas.utils.TestValues.VALID_CPF;
import static com.branas.utils.TestValues.VALID_NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class GetAccountTest {

    @Inject
    GetAccount getAccount;
    @InjectMock
    AccountRepository accountRepository;

    String VALID_EMAIL;
    Account account;
    @BeforeEach
    void setup() {
        VALID_EMAIL = "john.doe%d@gmail.com".formatted(System.currentTimeMillis());
        AccountInput validPassenger = AccountInput.builder()
                .isPassenger(true)
                .name(VALID_NAME.value())
                .email(VALID_EMAIL)
                .cpf(VALID_CPF.value())
                .build();

        account =  Account.create(
                validPassenger.name(),
                validPassenger.email(),
                new Cpf(validPassenger.cpf()),
                validPassenger.carPlate(),
                validPassenger.isPassenger(),
                validPassenger.isDriver()
        );
    }

    @Test
    void shouldGetAccount() {
        UUID accountId = UUID.randomUUID();
        when(accountRepository.getAccountById(any(UUID.class)))
                .thenReturn(account);
        account = getAccount.execute(accountId);
        account.setAccountId(accountId);
        Assertions.assertThat(account)
                .isNotNull()
                .hasFieldOrPropertyWithValue("accountId", accountId);
    }
}