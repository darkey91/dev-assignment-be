package com.transferz.controller;

import com.transferz.dao.AirportDao;
import com.transferz.dao.FlightDao;
import com.transferz.dao.PassengerDao;
import com.transferz.dto.request.RegisterPassengerRequest;
import com.transferz.dto.response.RegisterPassengerResponse;
import com.transferz.entity.Airport;
import com.transferz.entity.Flight;
import com.transferz.entity.Passenger;
import com.transferz.exception.TransferzException;
import com.transferz.rc.ResponseCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
public class PassengerRegistrationController {

    @Value("${business.flight-limit}")
    public int passengerCount;

    private final FlightDao flightDao;

    private final AirportDao airportDao;

    private final PassengerDao passengerDao;

    public PassengerRegistrationController(final FlightDao flightDao,
                                           final AirportDao airportDao,
                                           final PassengerDao passengerDao) {
        this.flightDao = flightDao;
        this.airportDao = airportDao;
        this.passengerDao = passengerDao;
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<RegisterPassengerResponse> registerPassenger(@RequestBody RegisterPassengerRequest request) {
        var passengerName = request.passengerName();
        var originAirportCode = request.origAirportCode();
        var originAirportId = airportDao.findByCode(originAirportCode)
                .map(Airport::getId)
                .orElseThrow(() -> new TransferzException(String.format("Airport with code=%s doesn't exist.", originAirportCode )));
        List<String> passengerFlightCodes = passengerDao.findFlightCodesByName(passengerName);
        var flights = flightDao.findByOriginAndTimeFilteredByCode(originAirportId, request.departureAfterTime(), passengerFlightCodes, passengerCount);
        if (flights.isEmpty()) {
            throw new TransferzException("No available flights for registration.");
        }
        Flight selectedFlight = flights.get(0);
        Passenger passenger = Passenger.builder()
                .name(passengerName)
                .flightCode(selectedFlight.getCode())
                .build();
        passengerDao.save(passenger);
        selectedFlight.setPassengerCount(selectedFlight.getPassengerCount() + 1);
        flightDao.save(selectedFlight);
        RegisterPassengerResponse response = new RegisterPassengerResponse(ResponseCode.SUCCESS, passenger.getName(), passenger.getFlightCode());
        return ResponseEntity.ok(response);
    }

}
