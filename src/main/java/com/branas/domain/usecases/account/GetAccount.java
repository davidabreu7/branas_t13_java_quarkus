package com.branas.domain.usecases.account;

import com.branas.api.ports.AccountRepository;
import com.branas.domain.entities.Account;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class GetAccount {

    @Inject
    AccountRepository accountRepository;

    public Account execute(UUID accountId)  {
        return accountRepository.getAccountById(accountId);
    }
}