package com.bootcamp.booking.services;

import com.bootcamp.booking.DTOS.*;
import com.bootcamp.booking.exceptions.*;

import java.util.List;

public interface IBookingService {

    List<HotelDTO> getHotels(HotelParamsDTO params) throws DatesAfterBeforeException, BadRequestException, NotAvailabilityException, NoDestinationException;

    HotelResponseDTO booking(HotelRequestDTO requestDTO) throws BadRequestException, DatesAfterBeforeException, NoDestinationException, EmailFormatException, TypeOfRoomException, PaymentException, NotAvailabilityException;

    List<FlightDTO> getFlights(FlightParamsDTO params) throws DatesAfterBeforeException, NoDestinationException, NotAvailabilityException, BadRequestException;

    FlightResponseDTO flightReservation(FlightRequestDTO requestDTO) throws BadRequestException, EmailFormatException, DatesAfterBeforeException, NoDestinationException, PaymentException, NotAvailabilityException;
}
