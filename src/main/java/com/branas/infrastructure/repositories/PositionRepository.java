package com.branas.infrastructure.repositories;

import com.branas.domain.entities.Position;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PositionRepository implements PanacheRepository<Position> {

    public List<Position> findByRideId(String rideId) {
        return list("rideId", Sort.by("date").ascending(), rideId);
    }
}
