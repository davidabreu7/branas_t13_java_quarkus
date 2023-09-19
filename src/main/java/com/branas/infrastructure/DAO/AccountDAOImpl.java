package com.branas.infrastructure.DAO;

import com.branas.domain.entities.Account;
import com.branas.domain.ports.AccountDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@ApplicationScoped
public class AccountDAOImpl implements AccountDAO {

    @Inject
    DataSource dataSource;

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public Account getAccountByEmail(String email) throws SQLException {
        Account existingAccount = null;
        ResultSet result;
        try (PreparedStatement statement = getConnection().prepareStatement("select * from cccat13.account where email = ?")) {
            statement.setString(1, email);
            result = statement.executeQuery();
            existingAccount = getAccount(result);
        } catch (SQLException e) {
            throw new SQLException("Error while getting account by email");
        }
        return existingAccount;
    }

    public void save(Account input) throws SQLException{
        try (PreparedStatement insertStatement = getConnection().prepareStatement("insert into cccat13.account (account_id, name, email, cpf, car_plate, is_passenger, is_driver, date, is_verified, verification_code) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            insertStatement.setObject(1, input.getAccountId());
            insertStatement.setString(2, input.getName());
            insertStatement.setString(3, input.getEmail());
            insertStatement.setString(4, input.getCpf());
            insertStatement.setString(5, input.getCarPlate());
            insertStatement.setBoolean(6, input.isPassenger());
            insertStatement.setBoolean(7, input.isDriver());
            insertStatement.setDate(8, new java.sql.Date(input.getDate().getTime()));
            insertStatement.setBoolean(9, false);
            insertStatement.setObject(10, input.getVerificationCode());
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error while saving account");
        }
    }


    public Account getAccountById(UUID accountId) throws SQLException {
        ResultSet result;
        Account account = null;
        try (PreparedStatement statement = getConnection().prepareStatement("select * from cccat13.account where account_id = ?")) {
            statement.setObject(1, accountId);
            result = statement.executeQuery();
            account = getAccount(result);
        } catch (SQLException e) {
            throw new SQLException("Error while getting account by email");
        }
        return account;
    }

    private static Account getAccount(ResultSet result) throws SQLException {
        Account account = null;
        if (result.next()) {
            account = new Account(
                    result.getObject("account_id", UUID.class),
                    result.getString("name"),
                    result.getString("email"),
                    result.getString("cpf"),
                    result.getString("car_plate"),
                    result.getBoolean("is_passenger"),
                    result.getBoolean("is_driver"),
                    result.getDate("date"),
                    result.getBoolean("is_verified"),
                    result.getObject("verification_code", UUID.class)
            );
        }
        return account;
    }
}


