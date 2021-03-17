package com.bootcamp.booking.exceptions;

import org.springframework.http.HttpStatus;

public class TypeOfRoomException extends BookingException {

    public TypeOfRoomException() {
        ErrorDTO error = new ErrorDTO();
        error.setName("Tamaño de habitación excedido.");
        error.setDescription("La habitación no posee capacidad para la cantidad de personas ingresadas.");
        this.setError(error);
        this.setStatus(HttpStatus.BAD_REQUEST);
    }
}
