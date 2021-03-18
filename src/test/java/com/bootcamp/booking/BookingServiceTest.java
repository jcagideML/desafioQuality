package com.bootcamp.booking;

import com.bootcamp.booking.DTOS.HotelDTO;
import com.bootcamp.booking.DTOS.HotelParamsDTO;
import com.bootcamp.booking.exceptions.BadRequestException;
import com.bootcamp.booking.exceptions.DatesAfterBeforeException;
import com.bootcamp.booking.exceptions.NoDestinationException;
import com.bootcamp.booking.exceptions.NotAvailabilityException;
import com.bootcamp.booking.repositories.IBookingRepository;
import com.bootcamp.booking.services.BookingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class BookingServiceTest {

    @Mock
    IBookingRepository repository;

    BookingService service;

    @BeforeEach
    void init() {
        openMocks(this);
        service = new BookingService(repository);
    }

    @Test
    public void getAllProducts() throws DatesAfterBeforeException, BadRequestException, NoDestinationException, NotAvailabilityException {
        List<HotelDTO> hotels = new ArrayList<>();

        when(repository.getHotels()).thenReturn(hotels);

        Assertions.assertEquals(hotels.size(), service.getHotels(new HotelParamsDTO()).size());
    }

}

