package com.bootcamp.booking.exceptions;

import org.springframework.http.HttpStatus;

public class NoDestinationException extends BookingException {

    public NoDestinationException() {
        ErrorDTO error = new ErrorDTO();
        error.setName("No existe el destino.");
        error.setDescription("El destino ingresado no existe en la base de datos.");
        this.setError(error);
        this.setStatus(HttpStatus.NOT_FOUND);
    }
}
