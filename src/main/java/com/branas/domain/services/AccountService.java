package com.branas.domain.services;

import com.branas.domain.DTO.AccountInput;
import com.branas.domain.entities.Account;
import com.branas.domain.ports.AccountDAO;
import com.branas.infrastructure.exceptions.AlreadyExistsException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.*;
import java.util.UUID;

@ApplicationScoped
public class AccountService {

    @Inject
    AccountDAO accountDAO;

    public void sendEmail(String email, String subject, String message) {
        System.out.println(email + " " + subject + " " + message);
    }

    public UUID signup(AccountInput input) throws Exception {
                Account existingAccount = accountDAO.getAccountByEmail(input.email());
                if (existingAccount != null)
                    throw new AlreadyExistsException("Account already exists");
                Account account = Account.create(
                        input.name(),
                        input.email(),
                        input.cpf(),
                        input.carPlate(),
                        input.isPassenger(),
                        input.isDriver()
                );
                try {
                    accountDAO.save(account);
                    sendEmail(account.getEmail(), "Verification", "Please verify your code at first login " + account.getVerificationCode());
                    return account.getAccountId();
                }
                catch (SQLException e) {
                    throw new SQLException("Error while saving account");
                }
    }

    public Account getAccount(UUID accountId) throws SQLException {
      try {
          return accountDAO.getAccountById(accountId);
        } catch (Exception e) {
            throw new SQLException("Error while getting account by id");
        }
    }
}