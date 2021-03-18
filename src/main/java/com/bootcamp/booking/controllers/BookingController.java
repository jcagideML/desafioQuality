package com.bootcamp.booking.controllers;

import com.bootcamp.booking.DTOS.*;
import com.bootcamp.booking.exceptions.*;
import com.bootcamp.booking.services.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
    public HotelResponseDTO booking(@RequestBody HotelRequestDTO request) throws DatesAfterBeforeException, BadRequestException, NoDestinationException, TypeOfRoomException, EmailFormatException, PaymentException, NotAvailabilityException {
        if (notNullBookingParams(request))
            return bookingService.booking(request);
        else
            throw new BadRequestException("Algunos de los datos ingresados esta vacío o erroneo.");
    }

    @GetMapping(value = "/api/v1/flights")
    public List<FlightDTO> getFlights(FlightParamsDTO params) throws DatesAfterBeforeException, BadRequestException, NoDestinationException, NotAvailabilityException {
        return bookingService.getFlights(params);
    }

    @PostMapping(value = "/api/v1/flight-reservation")
    public FlightResponseDTO flightReservation(@RequestBody FlightRequestDTO request) throws DatesAfterBeforeException, BadRequestException, NoDestinationException, TypeOfRoomException, EmailFormatException, PaymentException, NotAvailabilityException {
        if (notNullReservationParams(request))
            return bookingService.flightReservation(request);
        else
            throw new BadRequestException("Algunos de los datos ingresados esta vacío o erroneo.");
    }

    private boolean notNullBookingParams(HotelRequestDTO r) {
        if (r.getUserName() != null & r.getBooking() != null) {
            if (r.getBooking().getDateFrom() != null & r.getBooking().getDateTo() != null & r.getBooking().getDestination() != null
                    & r.getBooking().getHotelCode() != null & r.getBooking().getPeopleAmount() != null & r.getBooking().getPeople() != null
                    & r.getBooking().getRoomType() != null & r.getBooking().getPaymentMethod() != null) {
                return r.getBooking().getPaymentMethod().getNumber() != null & r.getBooking().getPaymentMethod().getType() != null
                        & r.getBooking().getPaymentMethod().getDues() != null;
            }
        }
        return false;
    }

    private boolean notNullReservationParams(FlightRequestDTO r) {
        if (r.getUserName() != null & r.getFlightReservation() != null) {
            if (r.getFlightReservation().getDateFrom() != null & r.getFlightReservation().getDateTo() != null & r.getFlightReservation().getFlightNumber() != null &
                    r.getFlightReservation().getOrigin() != null & r.getFlightReservation().getDestination() != null & r.getFlightReservation().getSeats() != null &
                    r.getFlightReservation().getSeatType() != null & r.getFlightReservation().getPeople() != null & r.getFlightReservation().getPaymentMethod() != null) {
                return r.getFlightReservation().getPaymentMethod().getNumber() != null & r.getFlightReservation().getPaymentMethod().getType() != null
                        & r.getFlightReservation().getPaymentMethod().getDues() != null;
            }
        }
        return false;
    }

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<ErrorDTO> handleException(BookingException exception) {
        return new ResponseEntity<>(exception.getError(), exception.getStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDTO> handleRunTimeException(RuntimeException exception) {
        exception.printStackTrace();
        ErrorDTO error = new ErrorDTO();
        error.setName("Internal server error.");
        error.setDescription("Un error en el servidor provocó que se detuviera la ejecución. Contactese con soporte.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDTO> handleFormatException(HttpMessageNotReadableException exception) {
        exception.printStackTrace();
        ErrorDTO error = new ErrorDTO();
        error.setName("Format Exception.");
        error.setDescription("El formato de la fecha debe respetar la forma dd/mm/aaaa.");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}