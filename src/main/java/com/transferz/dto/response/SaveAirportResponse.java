package com.transferz.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.transferz.dto.request.SaveAirportRequest;
import com.transferz.entity.Airport;
import com.transferz.rc.ResponseCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SaveAirportResponse(
        ResponseCode responseCode,
        Airport airport
) {
    public SaveAirportResponse(ResponseCode responseCode) {
        this(responseCode, null);
    }
}
