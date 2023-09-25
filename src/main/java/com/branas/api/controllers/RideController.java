package com.branas.api.controllers;

import com.branas.domain.DTO.RidePath;
import com.branas.domain.entities.Ride;
import com.branas.domain.usecases.AcceptRide;
import com.branas.domain.usecases.GetRide;
import com.branas.domain.usecases.RequestRide;
import com.branas.domain.usecases.RideService;
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
    RequestRide requestRide;
    @Inject
    AcceptRide acceptRide;
    @Inject
    GetRide getRide;

    @POST
    @Path("/rides/request/{accountId}")
    public RestResponse<Ride> requestRide(
            @PathParam("accountId") String accountId,
            @RequestBody RidePath ridePath
            ) throws Exception {
        return RestResponse.ok(requestRide.excecute(accountId, ridePath));
    }

    @PUT
    @Path("/rides/accept/{rideId}/{driverId}")
    public RestResponse<Ride> acceptRide(@PathParam("rideId") String rideId, @PathParam("driverId") String driverId) throws Exception {
        return RestResponse.ok(acceptRide.exceute(rideId, driverId));
    }

    @GET
    @Path("/rides/{rideId}")
    public RestResponse<Ride> getRideById(@PathParam("rideId") String rideId) {
        return RestResponse.ok(getRide.execute(rideId));
    }
}
