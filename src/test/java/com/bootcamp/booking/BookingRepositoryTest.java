package com.bootcamp.booking;

import com.bootcamp.booking.DTOS.*;
import com.bootcamp.booking.exceptions.BadRequestException;
import com.bootcamp.booking.exceptions.NoDestinationException;
import com.bootcamp.booking.exceptions.NotAvailabilityException;
import com.bootcamp.booking.repositories.BookingRepository;
import com.bootcamp.booking.repositories.IBookingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookingRepositoryTest {

    IBookingRepository repository;

    @BeforeEach
    void setUp() {
        this.repository = new BookingRepository();
    }

    @Test
    public void getAllHotels() {
        List<HotelDTO> hotels = BookingRepository.getHotelFromJSON("Hoteles.json");
        Assertions.assertIterableEquals(hotels, repository.getHotels());
    }

    @Test
    public void getHotelsByDatesAndDestination() throws ParseException, NotAvailabilityException, NoDestinationException {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        //Me armo el caso de prueba
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

        HotelDTO h2 = new HotelDTO();
        h2.setName("Cataratas Hotel 2");
        h2.setHotelCode("CH-0003");
        h2.setDestination("Puerto Iguazú");
        h2.setRoomType("TRIPLE");
        h2.setPrice(8200.0);
        AvailableDTO ad2 = new AvailableDTO();
        ad2.setDateFrom(dateFormat.parse("Wed Feb 10 00:00:00 ART 2021"));
        ad2.setDateTo(dateFormat.parse("Tue Mar 23 00:00:00 ART 2021"));
        List<AvailableDTO> adl2 = new ArrayList<>();
        adl2.add(ad2);
        h2.setAvailableDates(adl2);
        h2.setAvailable(true);

        HotelParamsDTO params = new HotelParamsDTO();
        params.setDateFrom(dateFormat.parse("Wed Feb 10 00:00:00 ART 2021"));
        params.setDateTo(dateFormat.parse("Sat Mar 20 00:00:00 ART 2021"));
        params.setDestination("Puerto Iguazú");

        //Consigo el resultado verdadero
        List<HotelDTO> actual = repository.getHotelsByDatesAndDestination(params);
        List<HotelDTO> expected = new ArrayList<>();
        expected.add(h1);
        expected.add(h2);
        //Pruebo si pasan o no.
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    public void getHotelsWrongDestination() throws ParseException, NotAvailabilityException, NoDestinationException {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

        HotelParamsDTO params = new HotelParamsDTO();
        params.setDateFrom(dateFormat.parse("Wed Feb 10 00:00:00 ART 2021"));
        params.setDateTo(dateFormat.parse("Sat Mar 20 00:00:00 ART 2021"));
        params.setDestination("Puerto");

        //Pruebo si pasan o no.
        Assertions.assertThrows(NoDestinationException.class, () -> repository.getHotelsByDatesAndDestination(params));
    }

    @Test
    public void getHotelsWrongDateFrom() throws ParseException, NotAvailabilityException, NoDestinationException {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

        HotelParamsDTO params = new HotelParamsDTO();
        params.setDateFrom(dateFormat.parse("Wed Feb 01 00:00:00 ART 2021"));
        params.setDateTo(dateFormat.parse("Sat Mar 20 00:00:00 ART 2021"));
        params.setDestination("Puerto Iguazú");

        //Pruebo si pasan o no.
        Assertions.assertThrows(NotAvailabilityException.class, () -> repository.getHotelsByDatesAndDestination(params));
    }

    @Test
    public void getHotelsWrongDateTo() throws ParseException, NotAvailabilityException, NoDestinationException {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

        HotelParamsDTO params = new HotelParamsDTO();
        params.setDateFrom(dateFormat.parse("Wed Feb 10 00:00:00 ART 2021"));
        params.setDateTo(dateFormat.parse("Sat Mar 25 00:00:00 ART 2021"));
        params.setDestination("Puerto Iguazú");

        //Pruebo si pasan o no.
        Assertions.assertThrows(NotAvailabilityException.class, () -> repository.getHotelsByDatesAndDestination(params));
    }

    @Test
    public void getHotelsOneParamNull() throws ParseException, NotAvailabilityException, NoDestinationException {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

        HotelParamsDTO params = new HotelParamsDTO();
        params.setDateTo(dateFormat.parse("Sat Mar 20 00:00:00 ART 2021"));
        params.setDestination("Puerto Iguazú");

        //Pruebo si pasan o no.
        Assertions.assertThrows(NullPointerException.class, () -> repository.getHotelsByDatesAndDestination(params));
    }

    @Test
    public void getHotelByCode() throws ParseException, BadRequestException {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        //Me armo el caso de prueba
        HotelDTO expected = new HotelDTO();
        expected.setName("Cataratas Hotel");
        expected.setHotelCode("CH-0002");
        expected.setDestination("Puerto Iguazú");
        expected.setRoomType("DOUBLE");
        expected.setPrice(6300.0);
        AvailableDTO ad1 = new AvailableDTO();
        ad1.setDateFrom(dateFormat.parse("Wed Feb 10 00:00:00 ART 2021"));
        ad1.setDateTo(dateFormat.parse("Sat Mar 20 00:00:00 ART 2021"));
        List<AvailableDTO> adl1 = new ArrayList<>();
        adl1.add(ad1);
        expected.setAvailableDates(adl1);
        expected.setAvailable(true);

        //Consigo el resultado verdadero
        HotelDTO actual = repository.getHotelByCode("CH-0002");

        //Pruebo si pasan o no.
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getHotelsByNullCode() throws ParseException {
        //Pruebo si pasan o no.
        Assertions.assertThrows(BadRequestException.class, () -> repository.getHotelByCode(null));
        Assertions.assertThrows(BadRequestException.class, () -> repository.getHotelByCode("CODIGO ERRONEO"));
        Assertions.assertThrows(BadRequestException.class, () -> repository.getHotelByCode(""));
    }

    @Test
    public void getAllFlights() {
        List<FlightDTO> flights = BookingRepository.getFlightFromJSON("Vuelos.json");
        Assertions.assertIterableEquals(flights, repository.getFlights());
    }

    @Test
    public void getFlightsByDatesAndDestination() throws ParseException, NotAvailabilityException, NoDestinationException {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        //Me armo el caso de prueba
        FlightDTO f1 = new FlightDTO();
        f1.setFlightNumber("BAPI-1235");
        f1.setOrigin("Buenos Aires");
        f1.setDestination("Puerto Iguazú");
        f1.setSeatType("Economy");
        f1.setPrice(6500.0);
        f1.setDateFrom(dateFormat.parse("Wed Feb 10 00:00:00 ART 2021"));
        f1.setDateTo(dateFormat.parse("Sat Feb 15 00:00:00 ART 2021"));

        FlightParamsDTO params = new FlightParamsDTO();
        params.setDateFrom(dateFormat.parse("Sat Feb 10 00:00:00 ART 2021"));
        params.setDateTo(dateFormat.parse("Sat Feb 15 00:00:00 ART 2021"));
        params.setOrigin("Buenos Aires");
        params.setDestination("Puerto Iguazú");

        //Consigo el resultado verdadero
        List<FlightDTO> actual = repository.getFlightsByDatesAndDestination(params);
        List<FlightDTO> expected = new ArrayList<>();
        expected.add(f1);
        //Pruebo si pasan o no.
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    public void getFlightsWrongOrigin() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

        FlightParamsDTO params = new FlightParamsDTO();
        params.setDateFrom(dateFormat.parse("Sat Mar 10 00:00:00 ART 2021"));
        params.setDateTo(dateFormat.parse("Sat Mar 20 00:00:00 ART 2021"));
        params.setOrigin("Buenos");
        params.setDestination("Puerto");

        //Pruebo si pasan o no.
        Assertions.assertThrows(NoDestinationException.class, () -> repository.getFlightsByDatesAndDestination(params));
    }

    @Test
    public void getFlightsWrongDestination() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

        FlightParamsDTO params = new FlightParamsDTO();
        params.setDateFrom(dateFormat.parse("Sat Mar 10 00:00:00 ART 2021"));
        params.setDateTo(dateFormat.parse("Sat Mar 20 00:00:00 ART 2021"));
        params.setOrigin("Buenos Aires");
        params.setDestination("Puerto");

        //Pruebo si pasan o no.
        Assertions.assertThrows(NoDestinationException.class, () -> repository.getFlightsByDatesAndDestination(params));
    }

    @Test
    public void getFlightsWrongDateFrom() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

        FlightParamsDTO params = new FlightParamsDTO();
        params.setDateFrom(dateFormat.parse("Sat Mar 01 00:00:00 ART 2021"));
        params.setDateTo(dateFormat.parse("Sat Mar 20 00:00:00 ART 2021"));
        params.setOrigin("Buenos Aires");
        params.setDestination("Puerto Iguazú");

        //Pruebo si pasan o no.
        Assertions.assertThrows(NotAvailabilityException.class, () -> repository.getFlightsByDatesAndDestination(params));
    }

    @Test
    public void getFlightsWrongDateTo() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

        FlightParamsDTO params = new FlightParamsDTO();
        params.setDateFrom(dateFormat.parse("Sat Mar 10 00:00:00 ART 2021"));
        params.setDateTo(dateFormat.parse("Sat Mar 20 00:00:00 ART 2021"));
        params.setOrigin("Buenos Aires");
        params.setDestination("Puerto Iguazú");

        //Pruebo si pasan o no.
        Assertions.assertThrows(NotAvailabilityException.class, () -> repository.getFlightsByDatesAndDestination(params));
    }

    @Test
    public void getFlightsOneParamNull() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

        FlightParamsDTO params = new FlightParamsDTO();
        params.setDateTo(dateFormat.parse("Sat Mar 20 00:00:00 ART 2021"));
        params.setDestination("Puerto Iguazú");
        params.setOrigin("Buenos Aires");


        //Pruebo si pasan o no.
        Assertions.assertThrows(NullPointerException.class, () -> repository.getFlightsByDatesAndDestination(params));
    }

    @Test
    public void getFlightByCode() throws ParseException, BadRequestException {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        //Me armo el caso de prueba
        FlightDTO expected = new FlightDTO();
        expected.setFlightNumber("BAPI-1235");
        expected.setOrigin("Buenos Aires");
        expected.setDestination("Puerto Iguazú");
        expected.setSeatType("Economy");
        expected.setPrice(6500.0);
        expected.setDateFrom(dateFormat.parse("Wed Feb 10 00:00:00 ART 2021"));
        expected.setDateTo(dateFormat.parse("Sat Feb 15 00:00:00 ART 2021"));

        //Consigo el resultado verdadero
        FlightDTO actual = repository.getFlightByCode("BAPI-1235");

        //Pruebo si pasan o no.
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getFlightsByNullCode() throws ParseException {
        //Pruebo si pasan o no.
        Assertions.assertThrows(BadRequestException.class, () -> repository.getFlightByCode(null));
        Assertions.assertThrows(BadRequestException.class, () -> repository.getFlightByCode("CODIGO ERRONEO"));
        Assertions.assertThrows(BadRequestException.class, () -> repository.getFlightByCode(""));
    }
}