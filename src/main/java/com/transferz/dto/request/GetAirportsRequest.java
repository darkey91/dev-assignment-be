package com.transferz.dto.request;


import java.util.List;

public record GetAirportsRequest(
        int limit,
        int page,
        List<String> names,
        List<String> codes
) {}