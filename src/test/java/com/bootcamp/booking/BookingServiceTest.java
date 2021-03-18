package com.bootcamp.booking;

import com.bootcamp.booking.DTOS.*;
import com.bootcamp.booking.exceptions.*;
import com.bootcamp.booking.repositories.IBookingRepository;
import com.bootcamp.booking.services.BookingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    @Test
    public void bookingTest() throws ParseException, BadRequestException, PaymentException, TypeOfRoomException, EmailFormatException, NotAvailabilityException, NoDestinationException, DatesAfterBeforeException {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        HotelDTO h1 = new HotelDTO();
        h1.setName("Cataratas Hotel");
        h1.setHotelCode("CH-0002");
        h1.setDestination("Puerto Iguazú");
        h1.setRoomType("DOUBLE");
        h1.setPrice(6300.0);
        AvailableDTO ad1 = new AvailableDTO();
        ad1.setDateFrom(dateFormat.parse("Wed Feb 10 00:00:00 ART 2021"));
        ad1.setDateTo(dateFormat.parse("Sat Mar 20 00:00:00 ART 2021"));
        List<AvailableDTO> adl1 = new ArrayList<>();
        adl1.add(ad1);
        h1.setAvailableDates(adl1);
        h1.setAvailable(true);

        HotelRequestDTO request = new HotelRequestDTO();
        request.setUserName("arjonamiguel@gmail.com");
        BookingDTO booking = new BookingDTO();
        booking.setDateFrom(dateFormat.parse("Wed Feb 10 00:00:00 ART 2021"));
        booking.setDateTo(dateFormat.parse("Sat Feb 12 00:00:00 ART 2021"));
        booking.setDestination("Puerto Iguazú");
        booking.setHotelCode("CH-0002");
        booking.setRoomType("DOUBLE");
        booking.setPeopleAmount(2);
        List<PersonaDTO> people = new ArrayList<>();
        people.add(new PersonaDTO());
        people.add(new PersonaDTO());
        booking.setPeople(people);
        PayMethodDTO payment = new PayMethodDTO();
        payment.setType("CREDIT");
        payment.setNumber("1234-1234-1234-1234");
        payment.setDues(6);
        booking.setPaymentMethod(payment);
        request.setBooking(booking);

        when(repository.getHotelByCode(h1.getHotelCode())).thenReturn(h1);

        HotelResponseDTO expected = new HotelResponseDTO();
        expected.setBooking(booking);
        expected.setUserName("arjonamiguel@gmail.com");
        expected.setAmount(12600.0);
        expected.setInterest("10.0%");
        expected.setTotal(13860.0);
        StatusCodeDTO statusCode = new StatusCodeDTO();
        statusCode.setCode(HttpStatus.OK.value());
        statusCode.setMessage(HttpStatus.OK.toString());
        expected.setStatusCode(statusCode);

        HotelResponseDTO actual = service.booking(request);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void reservationTest() throws ParseException, BadRequestException, PaymentException, TypeOfRoomException, EmailFormatException, NotAvailabilityException, NoDestinationException, DatesAfterBeforeException {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        FlightDTO f1 = new FlightDTO();
        f1.setFlightNumber("BAPI-1235");
        f1.setOrigin("Buenos Aires");
        f1.setDestination("Puerto Iguazú");
        f1.setSeatType("Economy");
        f1.setPrice(6500.0);
        f1.setDateFrom(dateFormat.parse("Wed Feb 10 00:00:00 ART 2021"));
        f1.setDateTo(dateFormat.parse("Sat Feb 15 00:00:00 ART 2021"));

        FlightRequestDTO request = new FlightRequestDTO();
        request.setUserName("arjonamiguel@gmail.com");
        FlightReservationDTO reservation = new FlightReservationDTO();
        reservation.setDateFrom(dateFormat.parse("Wed Feb 10 00:00:00 ART 2021"));
        reservation.setDateTo(dateFormat.parse("Sat Feb 15 00:00:00 ART 2021"));
        reservation.setOrigin("Buenos Aires");
        reservation.setDestination("Puerto Iguazú");
        reservation.setFlightNumber("BAPI-1235");
        reservation.setSeatType("Economy");
        reservation.setSeats(2);
        List<PersonaDTO> people = new ArrayList<>();
        people.add(new PersonaDTO());
        people.add(new PersonaDTO());
        reservation.setPeople(people);
        PayMethodDTO payment = new PayMethodDTO();
        payment.setType("CREDIT");
        payment.setNumber("1234-1234-1234-1234");
        payment.setDues(6);
        reservation.setPaymentMethod(payment);
        request.setFlightReservation(reservation);

        when(repository.getFlightByCode(f1.getFlightNumber())).thenReturn(f1);

        FlightResponseDTO expected = new FlightResponseDTO();
        expected.setFlightReservationDTO(reservation);
        expected.setUserName("arjonamiguel@gmail.com");
        expected.setAmount(13000.0);
        expected.setInterest("10.0%");
        expected.setTotal(14300.0);
        StatusCodeDTO statusCode = new StatusCodeDTO();
        statusCode.setCode(HttpStatus.OK.value());
        statusCode.setMessage(HttpStatus.OK.toString());
        expected.setStatusCode(statusCode);

        FlightResponseDTO actual = service.flightReservation(request);

        Assertions.assertEquals(expected, actual);
    }
}

