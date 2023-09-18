package com.branas.infrastructure.DAO;

import com.branas.domain.DTO.AccountInput;
import com.branas.domain.entities.Account;
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
class AccountDAOTest {

    @Inject
    AccountDAO accountDAO;
    AccountInput validPassenger;

    @BeforeEach
    void setUp() {

        validPassenger = builder()
                .isPassenger(true)
                .name(VALID_NAME.value())
                .email(ACCOUNT_EXISTS.value())
                .cpf(VALID_CPF.value())
                .build();
    }

    @Test
    void getAccountByEmail() {
        Account account;
        try {
            account = accountDAO.getAccountByEmail(validPassenger);
            assertThat(account).isNotNull()
                    .hasFieldOrPropertyWithValue("isPassenger", validPassenger.isPassenger())
                    .hasFieldOrPropertyWithValue("email", validPassenger.email());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void saveAccount() {
        try {
            accountDAO.saveAccount(validPassenger, UUID.fromString("33ec3d7c-c395-49ad-845e-d72c46307505"), new Date(), UUID.fromString("33ec3d7c-c395-49ad-845e-d72c46307505"));
            Account account = accountDAO.getAccountByEmail(validPassenger);
            assertThat(account).isNotNull()
                    .hasFieldOrPropertyWithValue("isPassenger", validPassenger.isPassenger())
                    .hasFieldOrPropertyWithValue("email", validPassenger.email());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}