package com.bootcamp.booking.services;

import com.bootcamp.booking.DTOS.HotelDTO;
import com.bootcamp.booking.DTOS.HotelParamsDTO;
import com.bootcamp.booking.DTOS.HotelRequestDTO;
import com.bootcamp.booking.DTOS.HotelResponseDTO;
import com.bootcamp.booking.exceptions.*;

import java.util.List;

public interface IBookingService {

    List<HotelDTO> getHotels(HotelParamsDTO params) throws DatesAfterBeforeException, BadRequestException, NotAvailabilityException, NoDestinationException;

    HotelResponseDTO booking(HotelRequestDTO requestDTO) throws BadRequestException, DatesAfterBeforeException, NoDestinationException, EmailFormatException, TypeOfRoomException, PaymentException;
}
