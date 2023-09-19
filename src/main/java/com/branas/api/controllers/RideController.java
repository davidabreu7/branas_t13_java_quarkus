package com.branas.api.controllers;

import com.branas.domain.DTO.RidePath;
import com.branas.domain.entities.Ride;
import com.branas.domain.services.RideService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RideController {

    @Inject
    RideService rideService;

    @POST
    @Path("/rides/request/{accountId}")
    public RestResponse<Ride> requestRide(
            @PathParam("accountId") String accountId,
            @RequestBody RidePath ridePath
            ) {
        return RestResponse.ok(rideService.requestRide(accountId, ridePath));
    }

    @PUT
    @Path("/rides/accept/{rideId}")
    public RestResponse<Ride> acceptRide(@PathParam("rideId") String rideId) {
        return RestResponse.ok(rideService.acceptRide(rideId));
    }

    @GET
    @Path("/rides/{rideId}")
    public RestResponse<Ride> getRideById(@PathParam("rideId") String rideId) {
        return RestResponse.ok(rideService.getRideById(rideId));
    }
}
