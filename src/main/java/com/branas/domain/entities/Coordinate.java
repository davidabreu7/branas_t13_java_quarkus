package com.branas.domain.entities;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Coordinate {

    private Double latitude;
    private Double longitude;

}
