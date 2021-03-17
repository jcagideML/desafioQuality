package com.bootcamp.booking.exceptions;

import org.springframework.http.HttpStatus;

public class DatesAfterBeforeException extends BookingException {

    public DatesAfterBeforeException() {
        ErrorDTO error = new ErrorDTO();
        error.setName("Dates after before exception.");
        error.setDescription("La fecha desde es posterior a la fecha hasta.");
        this.setError(error);
        this.setStatus(HttpStatus.BAD_REQUEST);
    }
}
