package com.branas.infrastructure.repositories;

import com.branas.infrastructure.jpaEntities.RideEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class RideRepositoryJpa implements PanacheRepositoryBase<RideEntity, UUID> {

    public Optional<RideEntity> findByPassengerId(UUID passengerId) {
        return Optional.of(find("passengerId", Sort.by("date").descending(), passengerId).firstResult());
    }

    public Optional<RideEntity> findByDriverId(UUID driverId) {
        return Optional.of(find("driverId", Sort.by("date").descending(), driverId).firstResult());
    }

}
