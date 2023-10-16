package com.branas.api.ports;

import com.branas.domain.entities.Position;

import java.util.List;
import java.util.UUID;

public interface PositionRepository {

    void save(Position position);
    List<Position> findByRideId(UUID rideId);
}
