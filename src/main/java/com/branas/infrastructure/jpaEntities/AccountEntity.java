package com.branas.infrastructure.jpaEntities;

import com.branas.domain.entities.Account;
import com.branas.domain.valueObjects.Cpf;
import com.branas.infrastructure.exceptions.ValidationErrorException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "account")
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
            UUID accountId,
            String name,
            String email,
            Cpf cpf,
            String carPlate,
            boolean isPassenger,
            boolean isDriver,
            LocalDate date,
            boolean isVerified,
            UUID verificationCode
    ) {
        this.accountId = accountId;
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.carPlate = carPlate;
        this.isPassenger = isPassenger;
        this.isDriver = isDriver;
        this.date = date;
        this.isVerified = isVerified;
        this.verificationCode = verificationCode;
    }

    public static AccountEntity create(Account account) {
        validateAccount(account.getName(), account.getEmail(), account.getCarPlate(), account.isDriver());
        return  new AccountEntity(
                account.getAccountId(),
                account.getName(),
                account.getEmail(),
                account.getCpf(),
                account.getCarPlate(),
                account.isPassenger(),
                account.isDriver(),
                account.getDate(),
                account.isVerified(),
                account.getVerificationCode()
        );
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