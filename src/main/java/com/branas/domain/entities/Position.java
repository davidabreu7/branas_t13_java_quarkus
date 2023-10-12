package com.branas.domain.entities;

import com.branas.domain.valueObjects.Coordinate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Entity
@Table(name = "position")
@ToString
public class Position {

    @Id
    @Column(name = "position_id")
    private UUID id;
    @Column(name = "ride_id")
    private final UUID rideId;
    private final Coordinate coordinate;
    @Column(name = "date")
    private LocalDateTime timestamp;

    public Position() {
        this.rideId = null;
        this.coordinate = null;
    }

    private Position(UUID rideId, Coordinate coordinate) {
        this.rideId = rideId;
        this.coordinate = coordinate;
    }

    public static Position create(UUID rideId, Double latitude, Double longitude) {
        Position position = new Position(rideId, new Coordinate(latitude, longitude));
        position.id = UUID.randomUUID();
        position.timestamp = LocalDateTime.now();
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Objects.equals(id, position.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
