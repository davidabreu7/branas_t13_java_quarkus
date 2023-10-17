package com.branas.infrastructure.repositories.implementations;

import com.branas.api.ports.PositionRepository;
import com.branas.domain.entities.Position;
import com.branas.infrastructure.jpaEntities.PositionEntity;
import com.branas.infrastructure.repositories.PositionRepositoryJpa;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class PositionRepositoryImpl implements PositionRepository {
    @Inject
    PositionRepositoryJpa positionRepository;
    @Override
    public void save(Position position) {
        PositionEntity positionEntity = PositionEntity.create(position);
        positionRepository.persist(positionEntity);
    }

    @Override
    public List<Position> findByRideId(UUID rideId) {
        List<PositionEntity> positionEntity = positionRepository.findByRideId(rideId);
        List<Position> positions = new ArrayList<>();
        for (PositionEntity position : positionEntity) {
            positions.add(Position.restore(position));
        }
        return positions;
    }
}
