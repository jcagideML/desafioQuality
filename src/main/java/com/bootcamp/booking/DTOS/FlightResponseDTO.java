package com.bootcamp.booking.DTOS;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightResponseDTO {

    private String userName;
    private Double amount;
    private String interest;
    private Double total;
    private FlightReservation flightReservation;
    private StatusCodeDTO statusCode;
}
