package com.transferz.dto.request;

import lombok.NonNull;

public record SaveAirportRequest(
        @NonNull String name,
        @NonNull String code,
        @NonNull String country
) {}

