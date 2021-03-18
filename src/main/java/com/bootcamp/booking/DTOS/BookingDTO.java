package com.bootcamp.booking.DTOS;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.OptBoolean;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class BookingDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", lenient = OptBoolean.FALSE, timezone = "America/Buenos_Aires")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", lenient = OptBoolean.FALSE, timezone = "America/Buenos_Aires")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateTo;
    private String destination;
    private String hotelCode;
    private Integer peopleAmount;
    private String roomType;
    List<PersonDTO> people;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PayMethodDTO paymentMethod;
}
