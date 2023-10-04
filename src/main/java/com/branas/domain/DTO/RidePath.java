package com.branas.domain.DTO;

import com.branas.domain.valueObjects.Coordinate;
import lombok.Builder;

@Builder
public record RidePath(Coordinate fromCoordinate, Coordinate toCoordinate) {
}
