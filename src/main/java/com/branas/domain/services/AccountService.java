package com.branas.domain.services;

import com.branas.domain.DTO.AccountInput;
import com.branas.domain.entities.Account;
import com.branas.domain.ports.AccountDAO;
import com.branas.utils.CpfValidator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.security.SecureRandom;
import java.sql.*;
import java.util.Date;
import java.util.UUID;

@ApplicationScoped
public class AccountService {

    @Inject
    AccountDAO accountDAO;

    public void sendEmail(String email, String subject, String message) {
        System.out.println(email + " " + subject + " " + message);
    }

    public UUID signup(AccountInput input) throws Exception {
                UUID accountId = UUID.randomUUID();
                SecureRandom random = new SecureRandom();
                UUID verificationCode = new UUID(random.nextLong(), random.nextLong());
                Date date = new Date();

                Account existingAccount = accountDAO.getAccountByEmail(input.email());
                if (existingAccount != null)
                    throw new Exception("Account already exists");
                if (!input.name().matches("[a-zA-Z]+ [a-zA-Z]+"))
                    throw new Exception("Invalid name");
                if (!input.email().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
                    throw new Exception("Invalid email");
                if (!CpfValidator.validateCpf(input.cpf()))
                    throw new Exception("Invalid cpf");
                if (input.isDriver() && (!input.carPlate().matches("[A-Z]{3}[0-9]{4}")))
                    throw new Exception("Invalid plate");
                Account account = new Account(
                        accountId,
                        input.name(),
                        input.email(),
                        input.cpf(),
                        input.carPlate(),
                        input.isPassenger(),
                        input.isDriver(),
                        date,
                        false,
                        verificationCode
                );
                try {
                    accountDAO.save(account);
                    sendEmail(input.email(), "Verification", "Please verify your code at first login " + verificationCode);
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