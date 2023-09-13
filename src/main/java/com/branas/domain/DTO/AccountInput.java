package com.branas.domain.DTO;

public record AccountInput(String name,
                           String email,
                           String cpf,
                           String carPlate,
                           boolean isPassenger,
                           boolean isDriver) {}
