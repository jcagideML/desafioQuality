package com.bootcamp.booking.exceptions;

import org.springframework.http.HttpStatus;

public class PaymentException extends BookingException {

    public PaymentException(String message) {
        ErrorDTO error = new ErrorDTO();
        error.setName("Payment exception.");
        error.setDescription(message);
        this.setError(error);
        this.setStatus(HttpStatus.BAD_REQUEST);
    }
}
