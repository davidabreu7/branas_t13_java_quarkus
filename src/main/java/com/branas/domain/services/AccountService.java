package com.branas.domain.services;

import com.branas.domain.DTO.AccountInput;
import com.branas.domain.entities.Account;

import java.security.SecureRandom;
import java.sql.*;
import java.util.Date;
import java.util.UUID;

public class AccountService {

    public void sendEmail(String email, String subject, String message) {
        System.out.println(email + " " + subject + " " + message);
    }

    public boolean validateCpf(String str) {
        if (str != null) {
            if (str.length() >= 11 && str.length() <= 14) {
                str = str.replace(".", "").replace(".", "").replace("-", "").replace(" ", "");
                String finalStr = str;
                if (!str.chars().allMatch(c -> c == finalStr.charAt(0))) {
                    try {
                        int d1, d2;
                        int dg1, dg2, rest;
                        int digito;
                        int nDigResult;
                        d1 = d2 = 0;
                        dg1 = dg2 = rest = 0;
                        for (int nCount = 1; nCount < str.length() - 1; nCount++) {
                            digito = Integer.parseInt(str.substring(nCount - 1, nCount));
                            d1 = d1 + (11 - nCount) * digito;
                            d2 = d2 + (12 - nCount) * digito;
                        }
                        rest = (d1 % 11);
                        dg1 = (rest < 2) ? dg1 = 0 : 11 - rest;
                        d2 += 2 * dg1;
                        rest = (d2 % 11);
                        if (rest < 2)
                            dg2 = 0;
                        else
                            dg2 = 11 - rest;
                        String nDigVerific = str.substring(str.length() - 2);
                        nDigResult = Integer.parseInt("" + dg1 + "" + dg2);
                        return nDigVerific.equals(Integer.toString(nDigResult));
                    } catch (Exception e) {
                        System.err.println("Erro !" + e);
                        return false;
                    }
                } else return false;
            } else return false;
        } else return false;
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
                    if (!existingAccount.next()) {
                        if (input.name().matches("[a-zA-Z]+ [a-zA-Z]+")) {
                            if (input.email().matches("^(.+)@(.+)$")) {
                                if (validateCpf(input.cpf())) {
                                    if (input.isDriver()) {
                                        if (input.carPlate().matches("[A-Z]{3}[0-9]{4}")) {
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
                                        } else {
                                            throw new Exception("Invalid plate");
                                        }
                                    } else {
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
                                } else {
                                    throw new Exception("Invalid cpf");
                                }
                            } else {
                                throw new Exception("Invalid email");
                            }
                        } else {
                            throw new Exception("Invalid name");
                        }
                    }  else {
                        throw new Exception("Account already exists");
                    }
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