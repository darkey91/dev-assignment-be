package com.transferz.controller;

import com.transferz.dao.AirportDao;
import com.transferz.dto.request.GetAirportsRequest;
import com.transferz.dto.request.SaveAirportRequest;
import com.transferz.dto.response.GetAirportsResponse;
import com.transferz.dto.response.SaveAirportResponse;
import com.transferz.entity.Airport;
import com.transferz.rc.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
public class AirportsController {
    private final Logger logger = LoggerFactory.getLogger(AirportsController.class);

    private final AirportDao airportDao;

    AirportsController(final AirportDao airportDao) {
        this.airportDao = airportDao;
    }

    @PostMapping("/saveAirport")
    public ResponseEntity<SaveAirportResponse> saveAirport(@RequestBody SaveAirportRequest saveAirportRequest) {
        String name = saveAirportRequest.name();
        String code = saveAirportRequest.code();
        Optional<Airport> existingAirport = airportDao.findByCodeOrName(code, name);
        if (existingAirport.isPresent()) {
            logger.warn(String.format("Airport with passengerName=%s or code=%s already exists", name, code));
            return new ResponseEntity<>(new SaveAirportResponse(ResponseCode.FAILURE), HttpStatus.BAD_REQUEST);
        }

        Airport airport = Airport.builder()
                .name(saveAirportRequest.name())
                .code(saveAirportRequest.code())
                .country(saveAirportRequest.country())
                .build();

        Airport savedAirport = airportDao.save(airport);
        var response = new SaveAirportResponse(ResponseCode.SUCCESS, savedAirport);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/getAirports")
    public ResponseEntity<GetAirportsResponse> getAirports(@RequestBody GetAirportsRequest getAirportsRequest) {
        Pageable pageable = PageRequest.of(getAirportsRequest.page(), getAirportsRequest.limit());
        List<String> names = getAirportsRequest.names();
        List<String> codes = getAirportsRequest.codes();
        List<Airport> airports;
        if (names != null && !names.isEmpty() && codes != null && !codes.isEmpty()) {
            airports = airportDao.findAllByNameInOrCodeIn(names, codes, pageable);
        } else if (names != null && !names.isEmpty()) {
            airports = airportDao.findAllByNameIn(names, pageable);
        } else if (codes != null && !codes.isEmpty()) {
            airports = airportDao.findAllByCodeIn(codes, pageable);
        } else {
            airports = airportDao.findAll(pageable).getContent();
        }
        return new ResponseEntity<>(new GetAirportsResponse(getAirportsRequest.page(), airports), HttpStatus.OK);
    }
}
