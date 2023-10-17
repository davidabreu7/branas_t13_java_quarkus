package com.branas.infrastructure.repositories;

import com.branas.infrastructure.jpaEntities.AccountEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class AccountRepositoryJpa implements PanacheRepositoryBase<AccountEntity, UUID> {

    public Optional<AccountEntity> findByEmail(String email) {
        return Optional.ofNullable(find("email", email).firstResult());
    }
}
