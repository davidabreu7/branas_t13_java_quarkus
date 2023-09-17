package com.branas.domain.services;

import com.branas.domain.DTO.AccountInput;
import com.branas.domain.entities.Account;
import com.branas.utils.CpfValidator;

import java.security.SecureRandom;
import java.sql.*;
import java.util.Date;
import java.util.UUID;

public class AccountService {

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

                ResultSet existingAccount;
                try (PreparedStatement statement = connection.prepareStatement("select * from cccat13.account where email = ?")) {
                    statement.setString(1, input.email());
                    existingAccount = statement.executeQuery();
                    if (existingAccount.next())
                        throw new Exception("Account already exists");
                    if (!input.name().matches("[a-zA-Z]+ [a-zA-Z]+"))
                        throw new Exception("Invalid name");
                    if (!input.email().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
                        throw new Exception("Invalid email");
                    if (!CpfValidator.validateCpf(input.cpf()))
                        throw new Exception("Invalid cpf");
                    if (input.isDriver() && (!input.carPlate().matches("[A-Z]{3}[0-9]{4}")))
                        throw new Exception("Invalid plate");
                    try (PreparedStatement insertStatement = connection.prepareStatement("insert into cccat13.account (account_id, name, email, cpf, car_plate, is_passenger, is_driver, date, is_verified, verification_code) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                        insertStatement.setObject(1, accountId);
                        insertStatement.setString(2, input.name());
                        insertStatement.setString(3, input.email());
                        insertStatement.setString(4, input.cpf());
                        insertStatement.setString(5, input.carPlate());
                        insertStatement.setBoolean(6, input.isPassenger());
                        insertStatement.setBoolean(7, input.isDriver());
                        insertStatement.setDate(8, new java.sql.Date(date.getTime()));
                        insertStatement.setBoolean(9, false);
                        insertStatement.setObject(10, verificationCode);
                        insertStatement.executeUpdate();
                    }
                    sendEmail(input.email(), "Verification", "Please verify your code at first login " + verificationCode);
                    return accountId;
                }
            } finally {
                connection.close();
            }
        }
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