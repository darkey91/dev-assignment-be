package com.transferz.dao;


import com.transferz.entity.Airport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class AirportDaoTest {

    @Autowired
    private AirportDao airportDao;

    @Test
    public void shouldFindAirportByCode() {
        // given
        var code = "AMS";
        var name = "Schiphol";
        var airport = Airport.builder()
                .code(code)
                .name(name)
                .country("Netherlands")
                .build();
        var savedAirport = airportDao.save(airport);

        // when
        Optional<Airport> found = airportDao.findByCodeOrName(code, null);

        // then
        assertThat(found.isPresent()).isTrue();
        found.ifPresent(a -> {
            assertEquals(savedAirport.getId(), a.getId());
            assertEquals(airport.getCode(), a.getCode());
            assertEquals(airport.getName(), a.getName());
        });
    }
}
