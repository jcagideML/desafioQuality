package com.bootcamp.booking.DTOS;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class HotelDTO {

    private String hotelCode;
    private String name;
    private String destination;
    private String roomType;
    private Double price;
    private List<AvailableDTO> availableDates;
    private Boolean available;

    public HotelDTO() {
    }

    public HotelDTO(String hotelCode, String name, String destination, String roomType, Double price, Date dateFrom, Date dateTo, List<AvailableDTO> availableDates, Boolean available) {
        setHotelCode(hotelCode);
        setName(name);
        setDestination(destination);
        setRoomType(roomType);
        setPrice(price);
        setAvailableDates(availableDates);
        setAvailable(available);
    }
}
