package com.branas.infrastructure.jpaEntities;

import com.branas.domain.valueObjects.Cpf;
import com.branas.infrastructure.exceptions.ValidationErrorException;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Getter
@ToString
//@Entity
//@Table(name = "account")
public class AccountEntity {

    @Id
    @Column(name = "account_id")
    private UUID accountId;
    private String name;
    private String email;
    private Cpf cpf;
    @Column(name = "car_plate")
    private String carPlate;
    @Column(name = "is_passenger")
    private boolean isPassenger;
    @Column(name = "is_driver")
    private boolean isDriver;
    private LocalDate date;
    @Column(name = "is_verified")
    private boolean isVerified;
    @Column(name = "verification_code")
    private UUID verificationCode;

    public AccountEntity() {
    }

    private AccountEntity(
            String name,
            String email,
            Cpf cpf,
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

    public static AccountEntity create(String name, String email, Cpf cpf,
                                       String carPlate, boolean isPassenger, boolean isDriver) {
        validateAccount(name, email, carPlate, isDriver);
        AccountEntity account = new AccountEntity(name, email, cpf, carPlate, isPassenger, isDriver);
        account.accountId = UUID.randomUUID();
        account.date = LocalDate.now();
        account.isVerified = false;
        account.verificationCode = UUID.randomUUID();
        return account;
    }

    public static AccountEntity restore(UUID accountId, String name, String email, String cpf,
                                        String carPlate, boolean isPassenger, boolean isDriver,
                                        LocalDate date, boolean isVerified, UUID verificationCode) {
        validateAccount(name, email, carPlate, isDriver);
        AccountEntity account = new AccountEntity(name, email, new Cpf(cpf), carPlate, isPassenger, isDriver);
        account.accountId = accountId;
        account.date = date;
        account.isVerified = isVerified;
        account.verificationCode = verificationCode;
        return account;
    }

    private static void validateAccount(String name, String email, String carPlate, boolean isDriver) {
        if (!name.matches("[a-zA-Z]+ [a-zA-Z]+"))
            throw new ValidationErrorException("Invalid name");
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
            throw new ValidationErrorException("Invalid email");
        if (isDriver && (!carPlate.matches("[A-Z]{3}[0-9]{4}")))
            throw new ValidationErrorException("Invalid plate");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountEntity account = (AccountEntity) o;
        return Objects.equals(accountId, account.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }
}