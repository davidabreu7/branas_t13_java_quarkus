package com.branas.domain.services;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class FareCalculatorTest {

    @Test
    void Should_ReturnMinimumFare_When_DistanceIsLessThanMinimumDistance() {
        Double distance = 0.5;
        Double expected = 4.5;
        Double actual = FareCalculator.calculate(distance);
        assertEquals(expected, actual);
    }
}