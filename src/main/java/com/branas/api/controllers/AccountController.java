package com.branas.api.controllers;

import com.branas.domain.DTO.AccountInput;
import com.branas.domain.entities.Account;
import com.branas.domain.usecases.Account.AccountSignup;
import com.branas.domain.usecases.Account.GetAccount;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.UUID;

@Path("/api")
public class AccountController {

    @Inject
    AccountSignup accountSignup;
    @Inject
    GetAccount getAccount;

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public RestResponse<UUID> signup(@RequestBody AccountInput account) {
        return RestResponse.ok(accountSignup.execute(account));
    }

    @GET
    @Path("/account/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<Account> getAccount(@PathParam("accountId") UUID accountId) {
        return RestResponse.ok(getAccount.execute(accountId));
    }
}
