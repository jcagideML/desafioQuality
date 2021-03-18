package com.bootcamp.booking.DTOS;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Setter
@Getter
public class PersonDTO {
    private Integer dni;
    private String name;
    private String lastname;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", lenient = OptBoolean.FALSE, timezone = "America/Buenos_Aires")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date birthDate;
    private String mail;
}
