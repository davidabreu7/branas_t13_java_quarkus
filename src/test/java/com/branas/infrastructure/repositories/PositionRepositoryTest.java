package com.branas.infrastructure.repositories;

import com.branas.domain.entities.Position;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class PositionRepositoryTest {

    @Inject
    PositionRepository positionRepository;
    Position position1;
    Position position2;
    @BeforeEach
    void setUp() {
        UUID rideId = UUID.randomUUID();
        position1 = Position.create(rideId, 1.0, 1.0);
        position2 = Position.create(rideId, 2.0, 2.0);

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