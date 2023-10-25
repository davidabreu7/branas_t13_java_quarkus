package com.branas.domain.DTO;

import lombok.Builder;

@Builder
public record AccountInput(String name,
                           String email,
                           String cpf,
                           String carPlate,
                           String password,
                           boolean isPassenger,
                           boolean isDriver) {
}
