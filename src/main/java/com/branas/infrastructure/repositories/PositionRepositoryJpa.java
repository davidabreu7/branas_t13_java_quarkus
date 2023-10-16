package com.branas.infrastructure.repositories;

import com.branas.infrastructure.jpaEntities.PositionEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PositionRepositoryJpa implements PanacheRepositoryBase<PositionEntity, UUID> {

    public List<PositionEntity> findByRideId(UUID rideId) {
        return list("rideId",Sort.by("timestamp").ascending(), rideId);
    }
}
