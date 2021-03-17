package com.bootcamp.booking.DTOS;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class HotelParamsDTO {
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Buenos_Aires")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateFrom;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Buenos_Aires")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateTo;
    private String destination;
}
