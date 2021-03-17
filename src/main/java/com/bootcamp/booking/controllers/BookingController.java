package com.bootcamp.booking.controllers;

import com.bootcamp.booking.DTOS.*;
import com.bootcamp.booking.exceptions.*;
import com.bootcamp.booking.services.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class BookingController {

    @Autowired
    private IBookingService bookingService;

    @GetMapping(value = "/api/v1/hotels")
    public List<HotelDTO> getHotels(HotelParamsDTO params) throws DatesAfterBeforeException, BadRequestException, NoDestinationException, NotAvailabilityException {
        return bookingService.getHotels(params);
    }

    @PostMapping(value = "/api/v1/booking")
    public HotelResponseDTO booking(@RequestBody HotelRequestDTO request) throws DatesAfterBeforeException, BadRequestException, NoDestinationException, TypeOfRoomException, EmailFormatException, PaymentException {
        return bookingService.booking(request);

    }

    @GetMapping(value = "/api/v1/flights")
    public List<FlightDTO> getFlights(FlightParamsDTO params) throws DatesAfterBeforeException, BadRequestException, NoDestinationException, NotAvailabilityException {
        return bookingService.getFlights(params);
    }

    @PostMapping(value = "/api/v1/flight-reservation")
    public FlightResponseDTO booking(@RequestBody FlightRequestDTO request) throws DatesAfterBeforeException, BadRequestException, NoDestinationException, TypeOfRoomException, EmailFormatException, PaymentException {
        return bookingService.flightReservation(request);

    }

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<ErrorDTO> handleException(BookingException exception) {
        exception.printStackTrace();
        return new ResponseEntity<>(exception.getError(), exception.getStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDTO> handleRunTimeException(RuntimeException exception) {
        ErrorDTO error = new ErrorDTO();
        error.setName("Internal server error.");
        error.setDescription("Un error en el servidor provocó que se detuviera la ejecución. Contactese con soporte.");
        exception.printStackTrace();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}