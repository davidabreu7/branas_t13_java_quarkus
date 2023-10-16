package com.branas.infrastructure.jpaEntities;

import com.branas.domain.valueObjects.Coordinate;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
//@Entity
//@Table(name = "position")
@ToString
public class PositionEntity {

    @Id
    @Column(name = "position_id")
    private UUID id;
    @Column(name = "ride_id")
    private final UUID rideId;
    private final Coordinate coordinate;
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
