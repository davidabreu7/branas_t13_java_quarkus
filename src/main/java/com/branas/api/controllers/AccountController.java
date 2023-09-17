package com.branas.api.controllers;

import com.branas.domain.DTO.AccountInput;
import com.branas.domain.entities.Account;
import com.branas.domain.services.AccountService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.UUID;

@Path("/api")
public class AccountController {

    @Inject
    AccountService accountService;

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public RestResponse<UUID> signup(@RequestBody AccountInput account) throws Exception {
        return RestResponse.ok(accountService.signup(account));
    }

    @GET
    @Path("/account/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<Account> getAccount(@PathParam("accountId") UUID accountId) throws Exception {
        return RestResponse.ok(accountService.getAccount(accountId));
    }
}
