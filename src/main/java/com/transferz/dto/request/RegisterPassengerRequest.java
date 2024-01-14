package com.transferz.dto.request;

import lombok.NonNull;

import java.time.LocalDateTime;

public record RegisterPassengerRequest(
        @NonNull String passengerName,
        @NonNull String origAirportCode,
        @NonNull  LocalDateTime departureAfterTime
) {}
