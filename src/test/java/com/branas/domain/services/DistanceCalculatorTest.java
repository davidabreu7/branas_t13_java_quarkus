package com.branas.domain.services;

import com.branas.domain.valueObjects.Coordinate;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class DistanceCalculatorTest {

    Coordinate from;
    Coordinate to;

    @BeforeEach
    void setUp() {
        from = new Coordinate(38.8976, -77.0366);
        to = new Coordinate(39.9496, -75.1503);
    }

    @Test
    void ShouldReturnDistanceBetweenTwoPoints() {
        // Arrange
        double expectedDistanceLower = 199.0;
        double expectedDistanceUpper = 200.0;
        // Act
        double actualDistance = DistanceCalculator.calculate(from, to);
        // Assert
        assertThat(actualDistance).isBetween(expectedDistanceLower, expectedDistanceUpper);
    }

}