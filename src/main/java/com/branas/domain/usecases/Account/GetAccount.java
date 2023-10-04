package com.branas.domain.usecases.Account;

import com.branas.api.ports.AccountDAO;
import com.branas.domain.entities.Account;
import com.branas.infrastructure.exceptions.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class GetAccount {

    @Inject
    AccountDAO accountDAO;

    public Account execute(UUID accountId)  {
        return accountDAO.getAccountById(accountId)
                .orElseThrow( () -> new ResourceNotFoundException("Account not found"));
    }
}