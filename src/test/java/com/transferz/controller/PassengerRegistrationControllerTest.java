package com.transferz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transferz.dao.AirportDao;
import com.transferz.dao.FlightDao;
import com.transferz.dao.PassengerDao;
import com.transferz.dto.request.RegisterPassengerRequest;
import com.transferz.entity.Airport;
import com.transferz.entity.Flight;
import com.transferz.entity.Passenger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PassengerRegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightDao flightDao;

    @MockBean
    private AirportDao airportDao;

    @MockBean
    private PassengerDao passengerDao;

    @Autowired
    private Jackson2ObjectMapperBuilder mapperBuilder;

    private ObjectMapper objectMapper;

    @Test
    public void shouldReturnOkWhenRegisteringNewPassenger() throws Exception {
        //given
        objectMapper = mapperBuilder.build();
        LocalDateTime departAfter = LocalDateTime.now().minusDays(1);
        var originAirportCode = "AMS";
        var passengerName = "Name";
        var originAirportId = "1";
        var destinationAirportId = "123";
        var airport = Airport.builder()
                .id(originAirportId)
                .code(originAirportCode)
                .country("Netherlands")
                .name("Schiphol")
                .build();
        var flightUsed = Flight.builder()
                .code("Flight01")
                .originAirportId(originAirportId)
                .destinationAirportId("432")
                .departureTime(LocalDateTime.now())
                .arrivalTime(LocalDateTime.now().plusHours(3))
                .passengerCount(3)
                .build();
        var flight = Flight.builder()
                .code("Flight02")
                .originAirportId(originAirportId)
                .destinationAirportId(destinationAirportId)
                .departureTime(LocalDateTime.now())
                .arrivalTime(LocalDateTime.now().plusHours(3))
                .passengerCount(2)
                .build();
        RegisterPassengerRequest request = new RegisterPassengerRequest(passengerName, originAirportCode, departAfter);
        when(airportDao.findByCode(eq(originAirportCode))).thenReturn(Optional.of(airport));
        when(passengerDao.findFlightCodesByName(passengerName)).thenReturn(List.of(flightUsed.getCode()));
        when(flightDao.findByOriginAndTimeFilteredByCode(eq(originAirportId), eq(departAfter), eq(List.of("Flight01")), eq(3)))
                .thenReturn(List.of(flight));

        //when
        mockMvc.perform(post("/register")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //then
        Passenger passenger = Passenger.builder()
                .name(passengerName)
                .flightCode("Flight02")
                .build();
        verify(passengerDao, Mockito.times(1)).save(eq(passenger));
        verify(flightDao, Mockito.times(1)).save(any());
    }

    @Test
    public void shouldReturnBadRequestWhenAirportDoesNotExist() throws Exception {
        when(airportDao.findByCode(any(String.class))).thenReturn(Optional.empty());

        mockMvc.perform(post("/register")
                        .content("{ \"passengerName\": \"Name\", \"origAirportCode\": \"AMS\", \"departureAfterTime\": \"" + LocalDateTime.now() + "\" }")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenNoAvailableFlights() throws Exception {
        LocalDateTime departAfter = LocalDateTime.now();
        RegisterPassengerRequest request = new RegisterPassengerRequest("John", "AMS", departAfter);
        var airport = Airport.builder()
                .id("id")
                .code("AMS")
                .country("Netherlands")
                .name("Schiphol")
                .build();
        when(airportDao.findByCode(any(String.class))).thenReturn(Optional.of(airport));

        when(passengerDao.findFlightCodesByName("John")).thenReturn(List.of("Flight01"));
        when(flightDao.findByOriginAndTimeFilteredByCode(eq("id"), eq(departAfter), eq(List.of("Flight01")), eq(3)))
                .thenReturn(List.of());

        mockMvc.perform(post("/register")
                        .content("{ \"passengerName\": \"John\", \"origAirportCode\": \"DFW\", \"departureAfterTime\": \"" + LocalDateTime.now().toString() + "\" }")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}