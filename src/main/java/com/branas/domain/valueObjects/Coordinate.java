package com.branas.domain.valueObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Embeddable
public class Coordinate {

    @Column(name = "lat")
    private Double latitude;
    @Column(name = "long")
    private Double longitude;

    public Coordinate(Double latitude, Double longitude) {
        validate(latitude, longitude);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private static void validate(Double latitude, Double longitude) {
        if(latitude == null || longitude == null)
            throw new IllegalArgumentException("Latitude and longitude must not be null");
        if(latitude < -90 || latitude > 90)
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        if(longitude < -180 || longitude > 180)
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
    }
}
