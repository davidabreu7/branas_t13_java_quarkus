package com.branas.domain.services;

import com.branas.domain.DTO.AccountInput;
import com.branas.domain.entities.Account;
import com.branas.infrastructure.DAO.AccountDAO;
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
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/app", "postgres", "123456")) {
            try {
                UUID accountId = UUID.randomUUID();
                SecureRandom random = new SecureRandom();
                UUID verificationCode = new UUID(random.nextLong(), random.nextLong());
                Date date = new Date();

                Account existingAccount = accountDAO.getAccountByEmail(input);
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
                accountDAO.saveAccount(input, accountId, date, verificationCode);
                sendEmail(input.email(), "Verification", "Please verify your code at first login " + verificationCode);
                return accountId;
            } catch (SQLException e) {
                System.out.println("Connection failure.");
                e.printStackTrace();
            }
        }
        return null;
    }

    public Account getAccount(UUID accountId) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/app", "postgres", "123456");
        PreparedStatement statement = connection.prepareStatement("select * from cccat13.account where account_id = ?");
        statement.setObject(1, accountId);
        ResultSet resultSet = statement.executeQuery();
        Account account = null;
        if (resultSet.next()) {
            account = new Account();
            account.setAccountId((UUID) resultSet.getObject("account_id"));
            account.setName(resultSet.getString("name"));
            account.setEmail(resultSet.getString("email"));
            account.setCpf(resultSet.getString("cpf"));
            account.setCarPlate(resultSet.getString("car_plate"));
            account.setPassenger(resultSet.getBoolean("is_passenger"));
            account.setDriver(resultSet.getBoolean("is_driver"));
            account.setDate(resultSet.getDate("date"));
            account.setVerified(resultSet.getBoolean("is_verified"));
            account.setVerificationCode((UUID) resultSet.getObject("verification_code"));
        }
        connection.close();
        return account;
    }
}