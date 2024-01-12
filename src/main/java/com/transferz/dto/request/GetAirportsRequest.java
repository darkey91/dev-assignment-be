package com.transferz.dto.request;


import java.util.List;

//todo select air
public record GetAirportsRequest(
        int limit,
        int page,
        List<String> names,
        List<String> codes
) {}