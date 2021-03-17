package com.bootcamp.booking.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BookingException {

    public BadRequestException(String message) {
        ErrorDTO error = new ErrorDTO();
        error.setName("Bad Request exception.");
        error.setDescription(message);
        this.setError(error);
        this.setStatus(HttpStatus.BAD_REQUEST);
    }
}
