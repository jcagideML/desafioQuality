package com.bootcamp.booking.DTOS;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class PayMethodDTO {
    private String type;
    private String number;
    private Integer dues;
}
