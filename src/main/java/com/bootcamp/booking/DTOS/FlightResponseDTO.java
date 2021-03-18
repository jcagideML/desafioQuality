package com.bootcamp.booking.DTOS;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class FlightResponseDTO {

    private String userName;
    private Double amount;
    private String interest;
    private Double total;
    private FlightReservationDTO flightReservationDTO;
    private StatusCodeDTO statusCode;
}
