package com.bootcamp.booking.DTOS;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@EqualsAndHashCode
public class HotelResponseDTO {
    private String userName;
    private Double amount;
    private String interest;
    private Double total;
    private BookingDTO booking;
    private StatusCodeDTO statusCode;
}
