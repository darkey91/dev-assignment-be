package com.transferz.dao;


import com.transferz.entity.Airport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AirportDaoTest {

    @Autowired
    private AirportDao airportDao;

    @Test
    public void shouldFindAirportByCodeWhenCorrectCodeProvided() {
        //given
        var code = "AMS";
        var name = "Schiphol";
        var airport = Airport.builder()
                .code(code)
                .name(name)
                .country("Netherlands")
                .build();
        var savedAirport = airportDao.save(airport);

        //when
        Optional<Airport> found = airportDao.findByCodeOrName(code, "some name");

        //then
        assertThat(found.isPresent()).isTrue();
        found.ifPresent(a -> {
            assertEquals(savedAirport.getId(), a.getId());
            assertEquals(airport.getCode(), a.getCode());
            assertEquals(airport.getName(), a.getName());
        });
    }

    @Test
    public void shouldFindAirportByNameWhenCorrectNameProvided() {
        //given
        var code = "AMS";
        var name = "Schiphol";
        var airport = Airport.builder()
                .code(code)
                .name(name)
                .country("Netherlands")
                .build();
        var savedAirport = airportDao.save(airport);

        //when
        Optional<Airport> found = airportDao.findByCodeOrName("some code", name);

        //then
        assertThat(found.isPresent()).isTrue();
        found.ifPresent(a -> {
            assertEquals(savedAirport.getId(), a.getId());
            assertEquals(airport.getCode(), a.getCode());
            assertEquals(airport.getName(), a.getName());
        });
    }

    @Test
    void shouldFindByCode() {
        //given
        var airport = Airport.builder()
                .code("ABC")
                .name("Airport Name")
                .country("Country")
                .build();
        var savedAirport = airportDao.save(airport);

        //when
        Optional<Airport> found = airportDao.findByCode("ABC");

        //then
        assertThat(found.isPresent()).isTrue();
        found.ifPresent(a -> {
            assertEquals(savedAirport.getId(), a.getId());
            assertEquals(airport.getCode(), a.getCode());
            assertEquals(airport.getName(), a.getName());
        });
    }

    @Test
    void shouldReturnEmptyWhenAirportNotFoundByCode() {
        //given
        var airport = Airport.builder()
                .code("ABC")
                .name("Airport Name")
                .country("Country")
                .build();
        airportDao.save(airport);

        //when
        Optional<Airport> found = airportDao.findByCode("CBA");

        //then
        assertTrue(found.isEmpty());
    }

    @Test
    public void shouldFindByNames() {
        //given
        var airport1 = Airport.builder()
                .name("Test Airport 1")
                .code("TA1")
                .country("Country1")
                .build();
        var airport2 = Airport.builder()
                .name("Test Airport 2")
                .code("TA2")
                .country("Country2")
                .build();
        airportDao.saveAll(List.of(airport1, airport2));
        List<String> airportNames = new ArrayList<>();
        airportNames.add("Test Airport 1");

        //when
        List<Airport> airports = airportDao.findAllByNameIn(airportNames, null);

        //then
        assertEquals(1, airports.size());
        assertEquals(airport1.getCode(), airports.get(0).getCode());
    }

    @Test
    public void shouldReturnEmptyListWhenInvalidNamesProvided() {
        //given
        var airport1 = Airport.builder()
                .name("Test Airport 1")
                .code("TA1")
                .country("Country1")
                .build();
        var airport2 = Airport.builder()
                .name("Test Airport 2")
                .code("TA2")
                .country("Country2")
                .build();
        airportDao.saveAll(List.of(airport1, airport2));

        //when
        List<Airport> airports = airportDao.findAllByNameIn(List.of("Airport 1", "Airport 2"), null);

        //then
        assertTrue(airports.isEmpty());
    }

    @Test
    public void shouldFindAllByCodeIn() {
        //given
        var airport1 = Airport.builder()
                .name("Test Airport 1")
                .code("A1")
                .country("Country1")
                .build();
        var airport2 = Airport.builder()
                .name("Test Airport 2")
                .code("A2")
                .country("Country2")
                .build();
        airportDao.saveAll(List.of(airport1, airport2));
        List<String> codes = Arrays.asList("A1", "A2");
        Pageable pageable = PageRequest.of(0, 2);

        //when
        List<Airport> found = airportDao.findAllByCodeIn(codes, pageable);

        //then
        assertEquals(2, found.size());
    }

    @Test
    public void shouldFindAllByNameInOrCodeIn() {
        //given
        Airport airport1 = Airport.builder().name("Airport1").code("Code1").country("Country1").build();
        Airport airport2 = Airport.builder().name("Airport2").code("Code2").country("Country2").build();
        airportDao.saveAll(List.of(airport1, airport2));
        List<String> names = List.of("Airport1");
        List<String> codes = List.of("Code2");
        Pageable pageable = PageRequest.of(0, 1);

        //when
        List<Airport> airports = airportDao.findAllByNameInOrCodeIn(names, codes, pageable);

        //then
        assertThat(airports).hasSize(1);
        assertThat(airports.get(0)).isEqualTo(airport1);
    }

    @Test
    public void shouldReturnEmptyListWhenNoMatch() {
        //given
        Airport airport1 = Airport.builder().name("Airport1").code("Code1").country("Country1").build();
        Airport airport2 = Airport.builder().name("Airport2").code("Code2").country("Country2").build();
        airportDao.save(airport1);
        airportDao.save(airport2);
        List<String> names = List.of("Airport3");
        List<String> codes = List.of("Code3");
        Pageable pageable = PageRequest.of(0, 2);

        //when
        List<Airport> airports = airportDao.findAllByNameInOrCodeIn(names, codes, pageable);

        //then
        assertThat(airports).isEmpty();
    }
}
