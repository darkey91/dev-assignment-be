package com.transferz.dao;


import com.transferz.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightDao extends JpaRepository<Flight, Long> {

    @Query("select f from Flight f where f.originAirportCode = :airportCode and f.departureTime >= :afterDepartureTime and f.code not in :excludedFlightCodes and f.passengerCount < :passengersLimit")
    List<Flight> findByOriginAndTimeFilteredByCode(String airportCode, LocalDateTime afterDepartureTime, List<String> excludedFlightCodes, int passengersLimit);

}