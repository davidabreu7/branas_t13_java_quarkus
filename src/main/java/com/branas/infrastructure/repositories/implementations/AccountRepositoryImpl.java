package com.branas.infrastructure.repositories.implementations;

import com.branas.api.ports.AccountRepository;
import com.branas.domain.entities.Account;
import com.branas.infrastructure.exceptions.DataBaseException;
import com.branas.infrastructure.jpaEntities.AccountEntity;
import com.branas.infrastructure.repositories.AccountRepositoryJpa;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.UUID;

@ApplicationScoped
@Transactional
public class AccountRepositoryImpl implements AccountRepository {

    @Inject
    AccountRepositoryJpa accountRepository;

    public Account getAccountByEmail(String email) {
        AccountEntity accountEntity = accountRepository.findByEmail(email)
                .orElseThrow( () -> new DataBaseException("Error while getting account by email"));
        return Account.restore(accountEntity);
    }

    public void save(Account account){
        AccountEntity accountEntity = AccountEntity.create(account);
        accountRepository.persist(accountEntity);
    }

    public Account getAccountById(UUID accountId){
        AccountEntity accountEntity = accountRepository.findByIdOptional(accountId)
                .orElseThrow( () -> new DataBaseException("Error while getting account by id"));
        return Account.restore(accountEntity);
    }

    public Boolean existsByEmail(String email) {
        return accountRepository.findByEmail(email).isPresent();
    }
}


