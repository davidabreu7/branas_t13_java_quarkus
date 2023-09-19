package com.branas.domain.ports;

import com.branas.domain.entities.Account;

import java.util.UUID;

public interface AccountDAO {

    public void save(Account account) throws Exception;
    public Account getAccountByEmail(String email) throws Exception;
    public Account getAccountById(UUID accountId) throws Exception;
}
