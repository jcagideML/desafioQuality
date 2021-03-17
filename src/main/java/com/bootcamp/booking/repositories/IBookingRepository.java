package com.bootcamp.booking.repositories;

import com.bootcamp.booking.DTOS.*;
import com.bootcamp.booking.exceptions.BadRequestException;
import com.bootcamp.booking.exceptions.NoDestinationException;
import com.bootcamp.booking.exceptions.NotAvailabilityException;

import java.util.List;

public interface IBookingRepository {

    List<HotelDTO> getHotels();

    List<HotelDTO> getHotelsByDatesAndDestination(HotelParamsDTO params) throws NotAvailabilityException, NoDestinationException;

    HotelDTO getHotelByCode(String hotelCode) throws BadRequestException;

    List<FlightDTO> getFlights();

    List<FlightDTO> getFlightsByDatesAndDestination(FlightParamsDTO params) throws NoDestinationException, NotAvailabilityException;

    FlightDTO getFlightByCode(String flightCode) throws BadRequestException;
}
