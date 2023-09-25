package com.branas.domain.usecases;

import com.branas.api.ports.AccountDAO;
import com.branas.domain.DTO.AccountInput;
import com.branas.domain.entities.Account;
import com.branas.infrastructure.exceptions.AlreadyExistsException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class AccountSignup {

    @Inject
    AccountDAO accountDAO;

    public void sendEmail(String email, String subject, String message) {
        System.out.println(email + " " + subject + " " + message);
    }

    public UUID execute(AccountInput input) {
                Account existingAccount = accountDAO.getAccountByEmail(input.email());
                if (existingAccount != null)
                    throw new AlreadyExistsException("Account already exists");
                Account account = Account.create(
                        input.name(),
                        input.email(),
                        input.cpf(),
                        input.carPlate(),
                        input.isPassenger(),
                        input.isDriver()
                );
                    accountDAO.save(account);
                    sendEmail(account.getEmail(), "Verification", "Please verify your code at first login " + account.getVerificationCode());
                    return account.getAccountId();
    }
}