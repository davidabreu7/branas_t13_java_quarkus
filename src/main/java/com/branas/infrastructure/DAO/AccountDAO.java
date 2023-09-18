package com.branas.infrastructure.DAO;

import com.branas.domain.DTO.AccountInput;
import com.branas.domain.entities.Account;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

@ApplicationScoped
public class AccountDAO {

    @Inject
    DataSource dataSource;

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public Account getAccountByEmail(AccountInput accountInput) throws SQLException {
        Account existingAccount;
        ResultSet result;
        try (PreparedStatement statement = getConnection().prepareStatement("select * from cccat13.account where email = ?")) {
            statement.setString(1, accountInput.email());
            result = statement.executeQuery();
            if (result.next()) {
                existingAccount = new Account(
                        result.getObject("account_id", java.util.UUID.class),
                        result.getString("name"),
                        result.getString("email"),
                        result.getString("cpf"),
                        result.getString("car_plate"),
                        result.getBoolean("is_passenger"),
                        result.getBoolean("is_driver"),
                        result.getDate("date"),
                        result.getBoolean("is_verified"),
                        result.getObject("verification_code", java.util.UUID.class)
                );
            } else {
                existingAccount = null;
            }
        } catch (SQLException e) {
            throw new SQLException("Error while getting account by email");
        }
        return existingAccount;
    }

    public void saveAccount(AccountInput input, UUID accountId, Date date, UUID verificationCode) throws SQLException {
        try (PreparedStatement insertStatement = getConnection().prepareStatement("insert into cccat13.account (account_id, name, email, cpf, car_plate, is_passenger, is_driver, date, is_verified, verification_code) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
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
        } catch (SQLException e) {
            throw new SQLException("Error while saving account");
        }
    }



}


