package com.branas.domain.valueObjects;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class CpfTest {

    @ParameterizedTest
    @ValueSource(strings = {"95818705552", "958.187.055-52", "958 187 055 52", "01234567890", "565.486.780-60", "147.864.110-00"})
    void ShouldValidateCpf(String cpf) {
        Cpf cpfObject = new Cpf(cpf);
        assertThat(cpfObject.getValue())
                .isNotNull()
                .isEqualTo(cpf);
    }

    @ParameterizedTest
    @ValueSource(strings = {"95818705500", "958.187.055-00", "958 187 055 00"})
    void ShouldNotValidateCpf(String cpf) {
        Assertions.assertThatThrownBy(() -> new Cpf(cpf))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid CPF");
    }
}
