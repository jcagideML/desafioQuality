package com.bootcamp.booking.DTOS;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HotelRequestDTO {
    private String userName;
    private BookingDTO booking;
}
