package com.branas.api.ports;

import com.branas.domain.entities.Account;

import java.util.UUID;

public interface AccountDAO {

    public void save(Account account);
    public Account getAccountByEmail(String email);
    public Account getAccountById(UUID accountId);
}
