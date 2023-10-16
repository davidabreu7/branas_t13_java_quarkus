package com.branas.infrastructure.repositories;

import com.branas.infrastructure.jpaEntities.PositionEntity;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class PositionRepositoryJpaTest {

    @Inject
    PositionRepositoryJpa positionRepository;
    PositionEntity position1;
    PositionEntity position2;
    @BeforeEach
    void setUp() {
        UUID rideId = UUID.randomUUID();
        position1 = PositionEntity.create(rideId, 1.0, 1.0);
        position2 = PositionEntity.create(rideId, 2.0, 2.0);
    }

    @Test
    @Transactional
    void ShouldPersistPosition() {
        positionRepository.persist(position1);
        assertThat(positionRepository.findById(position1.getId())).isEqualTo(position1);
    }
    @Test
    @Transactional
    void ShouldReturnPositionList() {

        positionRepository.persist(position1);
        positionRepository.persist(position2);

        assertThat(positionRepository.findByRideId(position1.getRideId()))
                .hasSize(2)
                .containsExactly(position1, position2);
    }

    @Test
    void ShouldReturnEmptyList() {
        assertThat(positionRepository.findByRideId(UUID.randomUUID())).isEmpty();
    }

    @AfterEach
    @Transactional
    void tearDown() {
        positionRepository.deleteById(position1.getId());
        positionRepository.deleteById(position2.getId());
    }
}