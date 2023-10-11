package com.branas.infrastructure.repositories;

import com.branas.domain.entities.Position;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PositionRepository implements PanacheRepositoryBase<Position, UUID> {

    public List<Position> findByRideId(UUID rideId) {
        return list("rideId",Sort.by("timestamp").ascending(), rideId);
    }
}
