package com.branas.domain.usecases.account;

import com.branas.api.ports.AccountRepository;
import com.branas.domain.DTO.AccountInput;
import com.branas.domain.entities.Account;
import com.branas.domain.valueObjects.Cpf;
import com.branas.infrastructure.exceptions.AlreadyExistsException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class AccountSignup {

    @Inject
    AccountRepository accountRepository;

    public void sendEmail(String email, String subject, String message) {
        System.out.println(email + " " + subject + " " + message);
    }

    public UUID execute(AccountInput input) {
                if (accountRepository.existsByEmail(input.email()))
                    throw new AlreadyExistsException("Account already exists");
                Account account = Account.create(
                        input.name(),
                        input.email(),
                        new Cpf(input.cpf()),
                        input.carPlate(),
                        input.isPassenger(),
                        input.isDriver()
                );
                    accountRepository.save(account);
                    sendEmail(account.getEmail(), "Verification", "Please verify your code at first login " + account.getVerificationCode());
                    return account.getAccountId();
    }
}