package com.bootcamp.booking.exceptions;

import org.springframework.http.HttpStatus;

public class NotAvailabilityException extends BookingException {

    public NotAvailabilityException() {
        ErrorDTO error = new ErrorDTO();
        error.setName("No disponibilidad.");
        error.setDescription("No hay disponibilidad para las fechas ingresadas.");
        this.setError(error);
        this.setStatus(HttpStatus.NOT_FOUND);
    }
}
