package com.bootcamp.booking.DTOS;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class StatusCodeDTO {
    private Integer code;
    private String message;
}
