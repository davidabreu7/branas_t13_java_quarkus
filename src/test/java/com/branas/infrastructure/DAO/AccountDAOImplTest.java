package com.branas.infrastructure.DAO;

import com.branas.domain.DTO.AccountInput;
import com.branas.domain.entities.Account;
import com.branas.domain.ports.AccountDAO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

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
        try {
            account = accountDAO.getAccountByEmail(validPassenger.email());
            assertThat(account).isNotNull()
                    .hasFieldOrPropertyWithValue("isPassenger", validPassenger.isPassenger())
                    .hasFieldOrPropertyWithValue("email", validPassenger.email());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void saveAccount() {
        UUID accountId = UUID.randomUUID();
        Account account = Account.create(
                validPassenger.name(),
                VALID_EMAIL,
                validPassenger.cpf(),
                validPassenger.carPlate(),
                validPassenger.isPassenger(),
                validPassenger.isDriver()
        );
        try {
            accountDAO.save(account);
            Account accountSaved = accountDAO.getAccountByEmail(account.getEmail());
            assertThat(accountSaved).isNotNull()
                    .hasFieldOrPropertyWithValue("accountId", account.getAccountId())
                    .hasFieldOrPropertyWithValue("isPassenger", account.isPassenger())
                    .hasFieldOrPropertyWithValue("email", account.getEmail());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void ShouldGetAccountById() {
        Account account = Account.create(
                validPassenger.name(),
                validPassenger.email(),
                validPassenger.cpf(),
                validPassenger.carPlate(),
                validPassenger.isPassenger(),
                validPassenger.isDriver()
        );
        try {
            accountDAO.save(account);
            Account accountSaved = accountDAO.getAccountById(account.getAccountId());
            assertThat(accountSaved).isNotNull()
                    .hasFieldOrPropertyWithValue("accountId", account.getAccountId())
                    .hasFieldOrPropertyWithValue("isPassenger", validPassenger.isPassenger())
                    .hasFieldOrPropertyWithValue("email", validPassenger.email());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}