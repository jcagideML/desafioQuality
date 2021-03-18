package com.bootcamp.booking.services;

import com.bootcamp.booking.DTOS.*;
import com.bootcamp.booking.exceptions.*;
import com.bootcamp.booking.repositories.IBookingRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class BookingService implements IBookingService {

    @Autowired
    private final IBookingRepository repository;

    public BookingService(IBookingRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<HotelDTO> getHotels(HotelParamsDTO params) throws DatesAfterBeforeException, BadRequestException, NotAvailabilityException, NoDestinationException {
        if (notNullHotelParams(params)) {
            if (validateDateFromBeforeTo(params.getDateFrom(), params.getDateTo())) {
                return repository.getHotelsByDatesAndDestination(params);
            } else {
                throw new DatesAfterBeforeException();
            }
        } else {
            return repository.getHotels();
        }
    }

    @Override
    public HotelResponseDTO booking(HotelRequestDTO requestDTO) throws BadRequestException, DatesAfterBeforeException, NoDestinationException, EmailFormatException, TypeOfRoomException, PaymentException, NotAvailabilityException {

        HotelDTO hotel = repository.getHotelByCode(requestDTO.getBooking().getHotelCode()); //Obtengo el Hotel por su código.
        HotelResponseDTO response = new HotelResponseDTO();
        response.setUserName(requestDTO.getUserName());
        response.setBooking(requestDTO.getBooking());

        if (validateBookingPayload(requestDTO, hotel)) { //Valido la mayoría de los parametros (No metodo de pago ni fecha de reserva)
            AvailableDTO aux = null; //Lo uso para luego generar las fechas sin reserva

            for (AvailableDTO dates : hotel.getAvailableDates()) { //Reviso todos las segmentos de fecha disponible
                if ((dates.getDateFrom().before(requestDTO.getBooking().getDateFrom()) | dates.getDateFrom().equals(requestDTO.getBooking().getDateFrom()))
                        & (dates.getDateTo().after(requestDTO.getBooking().getDateTo()) | dates.getDateTo().equals(requestDTO.getBooking().getDateTo()))) {
                    aux = dates;

                    Double amount = getCantDays(requestDTO.getBooking().getDateFrom(), requestDTO.getBooking().getDateTo()) * hotel.getPrice();

                    response.setAmount(amount);

                    Double interest = calculateInterest(requestDTO.getBooking().getPaymentMethod());

                    response.setInterest((interest * 100) + "%");
                    response.setTotal(amount + amount * interest);

                    StatusCodeDTO statusCode = new StatusCodeDTO();
                    statusCode.setCode(HttpStatus.OK.value());
                    statusCode.setMessage(HttpStatus.OK.toString());
                    response.setStatusCode(statusCode);
                }
            }
            if (aux != null) {
                hotel.getAvailableDates().remove(aux);//Quito las fechas que reservaron
                List<AvailableDTO> availableDates = generateAvailableDates(hotel, aux,
                        requestDTO.getBooking().getDateFrom(), requestDTO.getBooking().getDateTo());//Genero la lista nueva de fechas disponibles
                hotel.setAvailableDates(availableDates);//Se las seteo al hotel
            } else {
                throw new NotAvailabilityException();
            }
        }
        return response;
    }

    @Override
    public List<FlightDTO> getFlights(FlightParamsDTO params) throws DatesAfterBeforeException, NoDestinationException, NotAvailabilityException, BadRequestException {
        if (notNullFlightParams(params)) {
            if (validateDateFromBeforeTo(params.getDateFrom(), params.getDateTo())) {
                return repository.getFlightsByDatesAndDestination(params);
            } else {
                throw new DatesAfterBeforeException();
            }
        } else {
            return repository.getFlights();
        }
    }

    @Override
    public FlightResponseDTO flightReservation(FlightRequestDTO requestDTO) throws BadRequestException, EmailFormatException, DatesAfterBeforeException, NoDestinationException, PaymentException, NotAvailabilityException {
        FlightDTO flight = repository.getFlightByCode(requestDTO.getFlightReservation().getFlightNumber());
        FlightResponseDTO response = new FlightResponseDTO();

        if (validateReservationPayLoad(requestDTO, flight)) {

            if (flight.getDateFrom().equals(requestDTO.getFlightReservation().getDateFrom()) &
                    flight.getDateTo().equals(requestDTO.getFlightReservation().getDateTo())) {

                response.setUserName(requestDTO.getUserName());
                response.setFlightReservation(requestDTO.getFlightReservation());

                Double amount = requestDTO.getFlightReservation().getSeats() * flight.getPrice();

                response.setAmount(amount);

                Double interest = calculateInterest(requestDTO.getFlightReservation().getPaymentMethod());

                response.setInterest((interest * 100) + "%");
                response.setTotal(amount + amount * interest);

                response.getFlightReservation().setPaymentMethod(null);//Para que no se muestre en el response.

                StatusCodeDTO statusCode = new StatusCodeDTO();
                statusCode.setCode(HttpStatus.OK.value());
                statusCode.setMessage(HttpStatus.OK.toString());
                response.setStatusCode(statusCode);
            } else
                throw new NotAvailabilityException();
        }
        return response;
    }

    private boolean notNullHotelParams(HotelParamsDTO params) throws BadRequestException {
        if (params.getDateFrom() != null & params.getDateTo() != null & params.getDestination() != null)
            return true;
        else if (params.getDateFrom() == null & params.getDateTo() == null & params.getDestination() == null)
            return false;
        else {
            throw new BadRequestException("Los datos ingresados no son válidos.");
        }
    }

    private boolean notNullFlightParams(FlightParamsDTO params) throws BadRequestException {
        if (params.getDateFrom() != null & params.getDateTo() != null & params.getOrigin() != null & params.getDestination() != null)
            return true;
        else if (params.getDateFrom() == null & params.getDateTo() == null & params.getOrigin() == null & params.getDestination() == null)
            return false;
        else {
            throw new BadRequestException("Los datos ingresados no son válidos.");
        }
    }

    private boolean validateDateFromBeforeTo(Date from, Date to) {
        return from.before(to);
    }

    private boolean validateBookingPayload(HotelRequestDTO request, HotelDTO hotel) throws NoDestinationException, DatesAfterBeforeException, BadRequestException, TypeOfRoomException, EmailFormatException {
        if (validateDateFromBeforeTo(request.getBooking().getDateFrom(), request.getBooking().getDateTo())) {
            if (request.getBooking().getDestination().equals(hotel.getDestination())) {
                if (request.getBooking().getPeopleAmount().equals(request.getBooking().getPeople().size())) {
                    if (request.getBooking().getRoomType().equalsIgnoreCase(hotel.getRoomType())) {
                        if (request.getBooking().getPeopleAmount() <= getSizeOfRoom(hotel.getRoomType())) {
                            if (validateEmail(request.getUserName())) {
                                return true;
                            } else {
                                throw new EmailFormatException();
                            }
                        } else {
                            throw new TypeOfRoomException();
                        }
                    } else {
                        throw new BadRequestException("Los datos del hotel no coinciden con los datos almacenados en la base de datos.");
                    }
                } else {
                    throw new BadRequestException("La cantidad de personas no coincide con la cantidad de personas declaradas.");
                }
            } else {
                throw new NoDestinationException();
            }
        } else {
            throw new DatesAfterBeforeException();
        }
    }

    private boolean validateReservationPayLoad(FlightRequestDTO request, FlightDTO flight) throws DatesAfterBeforeException, NoDestinationException, EmailFormatException, BadRequestException {
        if (validateDateFromBeforeTo(request.getFlightReservation().getDateFrom(), request.getFlightReservation().getDateTo())) {
            if (request.getFlightReservation().getOrigin().equals(flight.getOrigin()) &
                    request.getFlightReservation().getDestination().equals(flight.getDestination())) {
                if (request.getFlightReservation().getSeats().equals(request.getFlightReservation().getPeople().size())) {
                    if (request.getFlightReservation().getSeatType().equalsIgnoreCase(flight.getSeatType())) {
                        if (request.getFlightReservation().getSeats() <= 150) {
                            if (validateEmail(request.getUserName())) {
                                return true;
                            } else {
                                throw new EmailFormatException();
                            }
                        } else {
                            throw new BadRequestException("La cantidad de asientos y personas sobrepasa la capacidad del vuelo.");
                        }
                    } else {
                        throw new BadRequestException("Los datos del vuelo no coinciden con los datos almacenados en la base.");
                    }
                } else {
                    throw new BadRequestException("La cantidad de personas no coincide con la cantidad de personas declaradas.");
                }
            } else {
                throw new NoDestinationException();
            }
        } else {
            throw new DatesAfterBeforeException();
        }
    }

    private boolean validateEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    private Integer getSizeOfRoom(String type) {
        switch (type) {
            case "SINGLE":
                return 1;
            case "DOUBLE":
                return 2;
            case "TRIPLE":
                return 3;
            default:
                return 10;
        }
    }

    private Integer getCantDays(Date from, Date to) {
        long diff = to.getTime() - from.getTime();
        return Math.toIntExact((TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)));
    }

    private Double calculateInterest(PayMethodDTO payment) throws PaymentException {
        if (payment.getType().equalsIgnoreCase("debit")) {
            if (payment.getDues() == 1) {
                return 0.0;
            } else {
                throw new PaymentException("No se permite el pago en cuotas con tarjetas de débito.");
            }
        } else {
            int dues = payment.getDues();
            if (dues == 1) {
                return 0.0;
            } else if (dues == 2 | dues == 3) {
                return 0.05;
            } else if (dues >= 4 & dues <= 6) {
                return 0.1;
            } else if (dues >= 7 & dues <= 9) {
                return 0.15;
            } else if (dues >= 10 & dues <= 12) {
                return 0.2;
            } else {
                throw new PaymentException("La cantidad de cuotas ingresada no esta permitida.");
            }
        }
    }

    private List<AvailableDTO> generateAvailableDates(HotelDTO hotel, AvailableDTO aux, Date from, Date to) {
        List<AvailableDTO> result = new ArrayList<>();

        if (!aux.getDateFrom().equals(from)) {
            AvailableDTO date = new AvailableDTO();
            date.setDateFrom(aux.getDateFrom());
            date.setDateTo(Date.from(LocalDate.from(from.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .minusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            result.add(date);
        }

        if (!aux.getDateTo().equals(to)) {
            AvailableDTO date = new AvailableDTO();
            date.setDateFrom(Date.from(LocalDate.from(to.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            date.setDateTo(aux.getDateTo());
            result.add(date);
        }

        result.addAll(hotel.getAvailableDates());

        return result;
    }
}
