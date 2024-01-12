package com.transferz.dao;


import com.transferz.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightDao extends JpaRepository<Flight, String> {

    @Query("select f from Flight f where f.originAirportId = :airportId and f.departureTime >= :afterDepartureTime and f.code not in :exclusionCodes and f.passengerCount < :passengerLimit")
    List<Flight> findByOriginAndTimeFilteredByCode(String airportId, LocalDateTime afterDepartureTime, List<String> exclusionCodes, int passengersLimit);

}