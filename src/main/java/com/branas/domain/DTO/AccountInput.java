package com.branas.domain.DTO;

public record AccountInput(String name,
                           String email,
                           String cpf,
                           String carPlate,
                           boolean isPassenger,
                           boolean isDriver) {
    public AccountInput(String name, String email, String cpf, boolean isPassenger) {
        this(name, email, cpf, null, isPassenger, false);
    }

    public AccountInput(String name, String email, String cpf, String carPlate, boolean isDriver) {
        this(name, email, cpf, carPlate, false, isDriver);
    }
}
