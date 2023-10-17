package com.branas.infrastructure.repositories.implementations;

import com.branas.domain.DTO.AccountInput;
import com.branas.domain.entities.Account;
import com.branas.api.ports.AccountRepository;
import com.branas.domain.valueObjects.Cpf;
import com.branas.infrastructure.exceptions.ResourceNotFoundException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.branas.domain.DTO.AccountInput.builder;
import static com.branas.utils.TestValues.*;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class AccountRepositoryImplTest {

    @Inject
    AccountRepository accountRepository;
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
    @Transactional
    void getAccountByEmail() {
        Account account;
        account = accountRepository.getAccountByEmail(validPassenger.email());
        assertThat(account).isNotNull()
                .hasFieldOrPropertyWithValue("isPassenger", validPassenger.isPassenger())
                .hasFieldOrPropertyWithValue("email", validPassenger.email());
    }

    @Test
    @Transactional
    void saveAccount() {
        Account account = Account.create(
                validPassenger.name(),
                VALID_EMAIL,
                new Cpf(validPassenger.cpf()),
                validPassenger.carPlate(),
                validPassenger.isPassenger(),
                validPassenger.isDriver()
        );
        accountRepository.save(account);
        Account accountSaved = accountRepository.getAccountByEmail(account.getEmail());
        assertThat(accountSaved).isNotNull()
                .hasFieldOrPropertyWithValue("accountId", account.getAccountId())
                .hasFieldOrPropertyWithValue("isPassenger", account.isPassenger())
                .hasFieldOrPropertyWithValue("email", account.getEmail());
    }

    @Test
    @Transactional
    void ShouldGetAccountById() {
        Account account = Account.create(
                validPassenger.name(),
                validPassenger.email(),
                new Cpf(validPassenger.cpf()),
                validPassenger.carPlate(),
                validPassenger.isPassenger(),
                validPassenger.isDriver()
        );
        accountRepository.save(account);
        Account accountSaved = accountRepository.getAccountById(account.getAccountId());
        assertThat(accountSaved).isNotNull()
                .hasFieldOrPropertyWithValue("accountId", account.getAccountId())
                .hasFieldOrPropertyWithValue("isPassenger", validPassenger.isPassenger())
                .hasFieldOrPropertyWithValue("email", validPassenger.email());
    }

    @Test
    void ShouldReturnFalseWhenAccountDoesNotExist() {
        assertThat(accountRepository.existsByEmail(VALID_EMAIL)).isFalse();
    }
}