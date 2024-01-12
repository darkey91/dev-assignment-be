package com.transferz.dto.response;

import com.transferz.entity.Airport;

import java.util.List;

public record GetAirportsResponse(
        int page,
        List<Airport> airports
) {}
