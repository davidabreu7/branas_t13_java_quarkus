package com.branas.api.ports;

import com.branas.domain.entities.Account;

import java.util.UUID;

public interface AccountRepository {

    public void save(Account account);
    public Account getAccountByEmail(String email);
    public Account getAccountById(UUID accountId);
    public Boolean existsByEmail(String email);
}
