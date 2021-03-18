package com.bootcamp.booking.DTOS;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Setter
@Getter
public class PersonaDTO {
    private Integer dni;
    private String name;
    private String lastname;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Buenos_Aires")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date birthDate;
    private String mail;
}
