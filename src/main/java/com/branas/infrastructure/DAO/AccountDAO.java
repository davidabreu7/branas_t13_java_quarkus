package com.branas.infrastructure.DAO;

import com.branas.domain.DTO.AccountInput;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@ApplicationScoped
public class AccountDAO {

    @Inject
    DataSource dataSource;

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public ResultSet getAccountByEmail(AccountInput accountInput) throws SQLException {
        ResultSet existingAccount;
            PreparedStatement statement = getConnection().prepareStatement("select * from cccat13.account where email = ?");
                statement.setString(1, accountInput.email());
                existingAccount = statement.executeQuery();
                return existingAccount;
    }
}


