package com.bootcamp.booking.DTOS;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class FlightDTO {

    private String flightNumber;
    private String origin;
    private String destination;
    private String seatType;
    private Double price;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Buenos_Aires")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateFrom;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Buenos_Aires")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateTo;

    public FlightDTO() {
    }

    public FlightDTO(String flightNumber, String origin, String destination, String seatType, Double price, Date dateFrom, Date dateTo) {
        setFlightNumber(flightNumber);
        setOrigin(origin);
        setDestination(destination);
        setSeatType(seatType);
        setPrice(price);
        setDateFrom(dateFrom);
        setDateTo(dateTo);
    }
}
