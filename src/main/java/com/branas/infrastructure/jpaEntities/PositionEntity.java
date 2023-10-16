package com.branas.infrastructure.jpaEntities;

import com.branas.domain.entities.Position;
import com.branas.domain.valueObjects.Coordinate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "position")
@ToString
public class PositionEntity {

    @Id
    @Column(name = "position_id")
    private UUID id;
    @Column(name = "ride_id")
    private final UUID rideId;
    private Coordinate coordinate;
    @Column(name = "date")
    private LocalDateTime timestamp;

    public PositionEntity() {
        this.rideId = null;
        this.coordinate = null;
    }

    private PositionEntity(UUID rideId, Coordinate coordinate) {
        this.rideId = rideId;
        this.coordinate = coordinate;
    }

    public static PositionEntity create(UUID rideId, Double latitude, Double longitude) {
        PositionEntity position = new PositionEntity(rideId, new Coordinate(latitude, longitude));
        position.id = UUID.randomUUID();
        position.timestamp = LocalDateTime.now();
        return position;
    }

    public static PositionEntity create(Position position) {
        PositionEntity positionEntity = new PositionEntity(position.getRideId(), position.getCoordinate());
        positionEntity.id = position.getId();
        positionEntity.timestamp = position.getTimestamp();
        return positionEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionEntity position = (PositionEntity) o;
        return Objects.equals(id, position.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
