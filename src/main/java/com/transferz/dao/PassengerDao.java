package com.transferz.dao;

import com.transferz.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassengerDao extends JpaRepository<Passenger, String> {

    @Query("select p.flightCode from Passenger p where p.name = :name")
    List<String> findFlightCodesByName(String name);


}
