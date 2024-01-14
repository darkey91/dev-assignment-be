package com.transferz.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = Flight.TABLE_NAME)
@Data
@Entity
@Builder
@AllArgsConstructor
public class Flight {
    public final static String TABLE_NAME = "flight";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flight_id_seq")
    @SequenceGenerator(name="flight_id_seq", allocationSize=1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "orig_airport_code", nullable = false)
    private String originAirportCode;

    @Column(name = "dest_airport_code", nullable = false)
    private String destinationAirportCode;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "passenger_count", nullable = false)
    private int passengerCount;

    public Flight() { }
}
