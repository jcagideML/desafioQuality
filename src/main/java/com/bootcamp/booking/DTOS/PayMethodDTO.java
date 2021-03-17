package com.bootcamp.booking.DTOS;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayMethodDTO {
    private String type;
    private String number;
    private Integer dues;
}
