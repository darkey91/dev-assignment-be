package com.transferz.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.transferz.rc.ResponseCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RegisterPassengerResponse(
        ResponseCode responseCode,
        String name,
        String flightCode
) { }
