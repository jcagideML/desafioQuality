package com.bootcamp.booking.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BookingException extends Exception {

    private ErrorDTO error;
    private HttpStatus status;
}
