package com.branas.domain.DTO;

import com.branas.domain.entities.Coordinate;
import lombok.Builder;

@Builder
public record RidePath(Coordinate fromCoordinate, Coordinate toCoordinate) {
}
