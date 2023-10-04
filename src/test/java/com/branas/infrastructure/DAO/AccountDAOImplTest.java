package com.branas.infrastructure.DAO;

import com.branas.domain.DTO.AccountInput;
import com.branas.domain.entities.Account;
import com.branas.api.ports.AccountDAO;
import com.branas.domain.valueObjects.Cpf;
import com.branas.infrastructure.exceptions.ResourceNotFoundException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.branas.domain.DTO.AccountInput.builder;
import static com.branas.utils.TestValues.*;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class AccountDAOImplTest {

    @Inject
    AccountDAO accountDAO;
    AccountInput validPassenger;
    String VALID_EMAIL;

    @BeforeEach
    void setUp() {

        validPassenger = builder()
                .isPassenger(true)
                .name(VALID_NAME.value())
                .email(ACCOUNT_EXISTS.value())
                .cpf(VALID_CPF.value())
                .build();

        VALID_EMAIL = "john.doe%d@gmail.com".formatted(System.currentTimeMillis());
    }

    @Test
    void getAccountByEmail() {
        Account account;
        account = accountDAO.getAccountByEmail(validPassenger.email());
        assertThat(account).isNotNull()
                .hasFieldOrPropertyWithValue("isPassenger", validPassenger.isPassenger())
                .hasFieldOrPropertyWithValue("email", validPassenger.email());
    }

    @Test
    void saveAccount() {
        Account account = Account.create(
                validPassenger.name(),
                VALID_EMAIL,
                new Cpf(validPassenger.cpf()),
                validPassenger.carPlate(),
                validPassenger.isPassenger(),
                validPassenger.isDriver()
        );
        accountDAO.save(account);
        Account accountSaved = accountDAO.getAccountByEmail(account.getEmail());
        assertThat(accountSaved).isNotNull()
                .hasFieldOrPropertyWithValue("accountId", account.getAccountId())
                .hasFieldOrPropertyWithValue("isPassenger", account.isPassenger())
                .hasFieldOrPropertyWithValue("email", account.getEmail());
    }

    @Test
    void ShouldGetAccountById() {
        Account account = Account.create(
                validPassenger.name(),
                validPassenger.email(),
                new Cpf(validPassenger.cpf()),
                validPassenger.carPlate(),
                validPassenger.isPassenger(),
                validPassenger.isDriver()
        );
        accountDAO.save(account);
        Account accountSaved = accountDAO.getAccountById(account.getAccountId())
                .orElseThrow( () -> new ResourceNotFoundException("Account not found"));
        assertThat(accountSaved).isNotNull()
                .hasFieldOrPropertyWithValue("accountId", account.getAccountId())
                .hasFieldOrPropertyWithValue("isPassenger", validPassenger.isPassenger())
                .hasFieldOrPropertyWithValue("email", validPassenger.email());
    }
}