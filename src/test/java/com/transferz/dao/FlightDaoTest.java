package com.transferz.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.transferz.entity.Flight;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FlightDaoTest {
    
    @Autowired
    private FlightDao flightDao;

    @Test
    void shouldReturnFlightsWhenFilterByOriginAndTimeAndExclusionCodes() {
        //given
        Flight oldFlight = Flight.builder()
                .code("FG123")
                .originAirportCode("NYC")
                .destinationAirportCode("LAX")
                .departureTime(LocalDateTime.now().minusDays(1))
                .arrivalTime(LocalDateTime.now().plusHours(2).minusDays(1))
                .passengerCount(2)
                .build();
        
        Flight overloadedFlight = Flight.builder()
                .code("AB321")
                .originAirportCode("NYC")
                .destinationAirportCode("JFK")
                .departureTime(LocalDateTime.now())
                .arrivalTime(LocalDateTime.now().plusHours(1))
                .passengerCount(3)
                .build();

        Flight flight = Flight.builder()
                .code("BA321")
                .originAirportCode("NYC")
                .destinationAirportCode("AMS")
                .departureTime(LocalDateTime.now())
                .arrivalTime(LocalDateTime.now().plusHours(1))
                .passengerCount(2)
                .build();
        String excludedFlightCode = "JKL456";
        Flight excludedFlight = Flight.builder()
                .code(excludedFlightCode)
                .originAirportCode("NYC")
                .destinationAirportCode("JFK")
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1))
                .passengerCount(3)
                .build();

        List<Flight> flights = List.of(oldFlight, overloadedFlight, excludedFlight);
        flightDao.saveAll(flights);
        var expectedFlight = flightDao.save(flight);

        //when
        List<Flight> actualFlights = flightDao.findByOriginAndTimeFilteredByCode("NYC", LocalDateTime.now().minusHours(1),
                Collections.singletonList(excludedFlightCode), 3);

        //then
        assertThat(actualFlights).isNotNull();
        assertThat(actualFlights).hasSize(1);
        assertThat(actualFlights.get(0)).isEqualTo(expectedFlight);
        assertThat(actualFlights).doesNotContain(excludedFlight);
    }
}