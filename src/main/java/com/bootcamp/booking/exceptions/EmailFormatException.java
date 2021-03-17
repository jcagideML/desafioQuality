package com.bootcamp.booking.exceptions;

import org.springframework.http.HttpStatus;

public class EmailFormatException extends BookingException {

    public EmailFormatException() {
        ErrorDTO error = new ErrorDTO();
        error.setName("Formato de email incorrecto.");
        error.setDescription("El email ingresado no cumple con el formato requerido (example@domain.com)");
        this.setError(error);
        this.setStatus(HttpStatus.BAD_REQUEST);
    }
}
