package com.transferz.dto.request;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record RegisterPassengerRequest(
        @NotNull String passengerName,
        @NotNull String origAirportCode, //todo use AMS by default?,
        @NotNull  LocalDateTime departureAfterTime

) {}
