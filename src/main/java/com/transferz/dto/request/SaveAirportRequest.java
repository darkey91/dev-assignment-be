package com.transferz.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public record SaveAirportRequest(
        @NotNull String name,
        @NotNull String code,
        @NotNull String country
) {}

