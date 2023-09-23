package com.branas.domain.entities;

import com.branas.infrastructure.exceptions.ValidationErrorException;
import com.branas.utils.CpfValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Account {

    private UUID accountId;
    private String name;
    private String email;
    private String cpf;
    private String carPlate;
    private boolean isPassenger;
    private boolean isDriver;
    private LocalDate date;
    private boolean isVerified;
    private UUID verificationCode;

    private Account(
            String name,
            String email,
            String cpf,
            String carPlate,
            boolean isPassenger,
            boolean isDriver
    ) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.carPlate = carPlate;
        this.isPassenger = isPassenger;
        this.isDriver = isDriver;
    }

    public static Account create(String name, String email, String cpf,
                                 String carPlate, boolean isPassenger, boolean isDriver) {
        validateAccount(name, email, cpf, carPlate, isDriver);
        Account account = new Account(name, email, cpf, carPlate, isPassenger, isDriver);
        account.accountId = UUID.randomUUID();
        account.date = LocalDate.now();
        account.isVerified = false;
        account.verificationCode = UUID.randomUUID();
        return account;
    }

    public static Account restore(UUID accountId, String name, String email, String cpf,
                                  String carPlate, boolean isPassenger, boolean isDriver,
                                  LocalDate date, boolean isVerified, UUID verificationCode) {
        validateAccount(name, email, cpf, carPlate, isDriver);
        Account account = new Account(name, email, cpf, carPlate, isPassenger, isDriver);
        account.accountId = accountId;
        account.date = date;
        account.isVerified = isVerified;
        account.verificationCode = verificationCode;
        return account;
    }

    private static void validateAccount(String name, String email, String cpf, String carPlate, boolean isDriver) {
        if (!name.matches("[a-zA-Z]+ [a-zA-Z]+"))
            throw new ValidationErrorException("Invalid name");
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
            throw new ValidationErrorException("Invalid email");
        if (!CpfValidator.validateCpf(cpf))
            throw new ValidationErrorException("Invalid cpf");
        if (isDriver && (!carPlate.matches("[A-Z]{3}[0-9]{4}")))
            throw new ValidationErrorException("Invalid plate");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }
}