package com.bootcamp.booking.DTOS;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class HotelDTO {

    private String hotelCode;
    private String name;
    private String destination;
    private String roomType;
    private Double price;
    private List<AvailableDTO> availableDates;
    private Boolean available;
}
