package com.transferz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.transferz.dao.FlightDao;
import com.transferz.dao.PassengerDao;
import com.transferz.dto.request.RegisterPassengerRequest;
import com.transferz.entity.Flight;
import com.transferz.entity.Passenger;
import com.transferz.exception.AppExceptionHandler;
import com.transferz.exception.TransferzException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PassengerRegistrationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FlightDao flightDao;

    @Mock
    private PassengerDao passengerDao;

    @InjectMocks
    private PassengerRegistrationController controller;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new AppExceptionHandler())
                .build();
        objectMapper.registerModule(new JavaTimeModule());
        ReflectionTestUtils.setField(controller, "passengerCount", 3);
    }

    @Test
    public void shouldReturnOkWhenRegisteringNewPassenger() throws Exception {
        //given
        LocalDateTime departAfter = LocalDateTime.now().minusDays(1);
        var originAirportCode = "AMS";
        var passengerName = "Name";
        var destinationAirportCode = "FRU";
        var flightUsed = Flight.builder()
                .code("Flight01")
                .originAirportCode(originAirportCode)
                .destinationAirportCode("DEST")
                .departureTime(LocalDateTime.now())
                .arrivalTime(LocalDateTime.now().plusHours(3))
                .passengerCount(3)
                .build();
        var flight = Flight.builder()
                .code("Flight02")
                .originAirportCode(originAirportCode)
                .destinationAirportCode(destinationAirportCode)
                .departureTime(LocalDateTime.now())
                .arrivalTime(LocalDateTime.now().plusHours(3))
                .passengerCount(2)
                .build();
        RegisterPassengerRequest request = new RegisterPassengerRequest(passengerName, originAirportCode, departAfter);
        when(passengerDao.findFlightCodesByName(passengerName)).thenReturn(List.of(flightUsed.getCode()));
        when(flightDao.findByOriginAndTimeFilteredByCode(eq(originAirportCode), eq(departAfter), eq(List.of("Flight01")), eq(3)))
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
    public void shouldReturnBadRequestWhenRequestDataInvalid() throws Exception {
        mockMvc.perform(post("/register")
                        .content("{ \"passengerName\": \"Name\", \"origAirportCode\": \"AMS\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldThrowExceptionWhenNoAvailableFlights() throws Exception {
        LocalDateTime departAfter = LocalDateTime.now();
        var originAirportCode = "AMS";
        RegisterPassengerRequest request = new RegisterPassengerRequest("John", originAirportCode, departAfter);
        var usedFlightCode = "Flight01";
        when(passengerDao.findFlightCodesByName("John")).thenReturn(List.of(usedFlightCode));
        when(flightDao.findByOriginAndTimeFilteredByCode(eq(originAirportCode), eq(departAfter), eq(List.of(usedFlightCode)), eq(3)))
                .thenReturn(List.of());

        mockMvc.perform(post("/register")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(TransferzException.class, result.getResolvedException()));
    }
}