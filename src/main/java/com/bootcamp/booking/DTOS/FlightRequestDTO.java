package com.bootcamp.booking.DTOS;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightRequestDTO {

    private String userName;
    private FlightReservation flightReservation;
}
