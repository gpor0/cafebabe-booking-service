package com.github.gpor0.resources;

import com.github.gpor0.business.BookingService;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import org.eclipse.microprofile.lra.annotation.Compensate;
import org.eclipse.microprofile.lra.annotation.ParticipantStatus;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;

import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_CONTEXT_HEADER;

@Path("/bookings")
@RequestScoped
public class BookingResource {

    private static final Logger LOG = LogManager.getLogger("BookingService");

    @Inject
    private BookingService bookingService;

    @POST
    @Path("/book")
    @LRA(value = LRA.Type.REQUIRES_NEW, cancelOnFamily = Response.Status.Family.CLIENT_ERROR)
    public Response reserve(@HeaderParam(LRA.LRA_HTTP_CONTEXT_HEADER) URI lraId,
                            @QueryParam("flightCode") String flightCode,
                            @QueryParam("returnFlightCode") String returnFlightCode,
                            @QueryParam("hotelName") String hotelName,
                            @QueryParam("customer") String customer) {
        LOG.info("Received booking for:\n" +
                "flight {}\n" +
                "hotel {}\n" +
                "return flight {}\n" +
                "customer {}", flightCode, hotelName, returnFlightCode, customer);

        try {
            bookingService.book(customer, flightCode, returnFlightCode, hotelName);
            LOG.info("Booking successful");
            return Response.ok("success").build();
        } catch (Exception e) {
            LOG.error("Booking failed with error {}", e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/compensate")
    @Compensate
    public Response compensate(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId) {
        System.out.println("COMPENSATION FOR " + lraId);

        return Response.ok(ParticipantStatus.Compensated.name()).build();
    }

}
