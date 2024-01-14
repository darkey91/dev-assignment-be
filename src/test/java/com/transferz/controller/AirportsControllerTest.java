package com.transferz.controller;

import com.transferz.dao.AirportDao;
import com.transferz.entity.Airport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AirportsControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AirportDao airportDao;

    @InjectMocks
    private AirportsController controller;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void saveAirport_handlesNewAirportSuccessfully() throws Exception {
        when(airportDao.findByCodeOrName(any(), any())).thenReturn(Optional.empty());
        when(airportDao.save(any())).thenReturn(new Airport());

        mockMvc.perform(post("/saveAirport")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"anAirport\", \"code\":\"aCode\", \"country\":\"aCountry\"}"))
                .andExpect(status().isOk());

        verify(airportDao, times(1)).findByCodeOrName(any(), any());
        verify(airportDao, times(1)).save(any());
    }

    @Test
    void saveAirport_handlesExistingAirport() throws Exception {
        when(airportDao.findByCodeOrName(any(), any())).thenReturn(Optional.of(new Airport()));

        mockMvc.perform(post("/saveAirport")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"anAirport\", \"code\":\"aCode\", \"country\":\"aCountry\"}"))
                .andExpect(status().isBadRequest());
        verify(airportDao, times(1)).findByCodeOrName(any(), any());
        verify(airportDao, times(0)).save(any());
    }


    @Test
    public void getAirports_returnsAirports() throws Exception {
        Airport airport1 = Airport.builder().name("AirportName1").code("AirportCode1").country("Country").build();
        Airport airport2 = Airport.builder().name("AirportName2").code("AirportCode2").country("Country").build();

        List<String> inputNames = Arrays.asList("AirportName1", "AirportName2");
        List<String> inputCodes = Arrays.asList("AirportCode1", "AirportCode2");
        List<Airport> expectedResponse = List.of(airport1, airport2);
        Pageable pageable = PageRequest.of(0, 10);
        when(airportDao.findAllByNameInOrCodeIn(eq(inputNames), eq(inputCodes), eq(pageable))).thenReturn(expectedResponse);

        mockMvc.perform(post("/getAirports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"page\": 0, \"limit\": 10, \"names\": [\"AirportName1\", \"AirportName2\"], \"codes\": [\"AirportCode1\", \"AirportCode2\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.airports[0].name").value("AirportName1"))
                .andExpect(jsonPath("$.airports[0].code").value("AirportCode1"))
                .andExpect(jsonPath("$.airports[1].name").value("AirportName2"))
                .andExpect(jsonPath("$.airports[1].code").value("AirportCode2"))
                .andExpect(jsonPath("$.airports[0].country").value("Country"));
    }
}