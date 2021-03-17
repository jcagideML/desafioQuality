package com.bootcamp.booking.repositories;

import com.bootcamp.booking.DTOS.HotelDTO;
import com.bootcamp.booking.DTOS.HotelParamsDTO;
import com.bootcamp.booking.DTOS.HotelResponseDTO;
import com.bootcamp.booking.exceptions.BadRequestException;
import com.bootcamp.booking.exceptions.NoDestinationException;
import com.bootcamp.booking.exceptions.NotAvailabilityException;

import java.util.List;

public interface IBookingRepository {

    List<HotelDTO> getHotels();

    List<HotelDTO> getHotelsByDatesAndDestination(HotelParamsDTO params) throws NotAvailabilityException, NoDestinationException;

    HotelDTO getHotelByCode(String hotelCode) throws BadRequestException;
}
