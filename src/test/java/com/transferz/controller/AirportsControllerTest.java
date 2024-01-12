package com.transferz.controller;

import com.transferz.dao.AirportDao;
import com.transferz.entity.Airport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AirportsControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AirportDao airportDao;

    @Test
    void saveAirportTest_handlesNewAirportSuccessfully() throws Exception {
        when(airportDao.findByCodeOrName(any(), any())).thenReturn(Optional.empty());
        when(airportDao.save(any())).thenReturn(new Airport());

        mockMvc.perform(post("/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"anAirport\", \"code\":\"aCode\", \"country\":\"aCountry\"}"))
                .andExpect(status().isOk());

        verify(airportDao, times(1)).findByCodeOrName(any(), any());
        verify(airportDao, times(1)).save(any());
    }

    @Test
    void saveAirportTest_handlesExistingAirport() throws Exception {
        when(airportDao.findByCodeOrName(any(), any())).thenReturn(Optional.of(new Airport()));

        mockMvc.perform(post("/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"anAirport\", \"code\":\"aCode\", \"country\":\"aCountry\"}"))
                .andExpect(status().isBadRequest());

        verify(airportDao, times(1)).findByCodeOrName(any(), any());
        verify(airportDao, times(0)).save(any());
    }
}