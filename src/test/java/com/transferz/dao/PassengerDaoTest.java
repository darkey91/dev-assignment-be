package com.transferz.dao;

import org.junit.jupiter.api.Test;
import com.transferz.entity.Passenger;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PassengerDaoTest {

    @Autowired
    private PassengerDao passengerDao;

    @Test
    public void shouldReturnFlightCodesByPassengerName() {
        // given
        Passenger passenger1 = new Passenger();
        passenger1.setName("Name");
        passenger1.setFlightCode("FD123");
        Passenger passenger2 = new Passenger();
        passenger2.setName("Name");
        passenger2.setFlightCode("123DF");
        passengerDao.saveAll(List.of(passenger1, passenger2));

        // when
        List<String> found = passengerDao.findFlightCodesByName("Name");

        // then
        assertThat(found).hasSize(2);
        assertThat(found).contains("FD123", "123DF");
    }

    @Test
    public void shouldReturnEmptyListWhenNotMatchByName() {
        // given
        Passenger passenger = new Passenger();
        passenger.setName("Name");
        passenger.setFlightCode("FD123");
        passengerDao.save(passenger);

        // when
        List<String> found = passengerDao.findFlightCodesByName("Invalid Name");

        // then
        assertThat(found).isEmpty();
    }
}