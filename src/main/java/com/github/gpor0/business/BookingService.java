package com.github.gpor0.business;

import io.narayana.lra.filter.ClientLRARequestFilter;
import io.narayana.lra.filter.ClientLRAResponseFilter;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class BookingService {

    private Client client = ClientBuilder.newBuilder()
            .register(ClientLRARequestFilter.class)
            .register(ClientLRAResponseFilter.class)
            .build();

    public void book(String customer, String flightCode, String returnFlightCode, String hotelName) {

        if (flightCode != null) {
            reserveFlight(flightCode, customer);
        }

        if (hotelName != null) {
            reserveHotel(hotelName, customer);
        }

        if (returnFlightCode != null) {
            reserveFlight(returnFlightCode, customer);
        }
    }

    private void reserveFlight(String flightCode, String customer) {
        String url = "http://localhost:5002/flights/" + flightCode + "?customer=" + customer;

        Response response = client.target(url).request().get();
        if (response.getStatus() >= 400) {
            throw new RuntimeException(response.readEntity(String.class));
        }
    }

    private void reserveHotel(String hotelName, String customer) {
        String url = "http://localhost:5001/hotels/" + hotelName + "?customer=" + customer;

        Response response = client.target(url).request().get();
        if (response.getStatus() >= 400) {
            throw new RuntimeException(response.readEntity(String.class));
        }
    }

}
