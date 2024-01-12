package com.transferz.dao;

import com.transferz.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface AirportDao extends JpaRepository<Airport, String> {

    Optional<Airport> findByCodeOrName(@NotNull String code, @NotNull String name);

    Optional<Airport> findByCode(@NotNull String code);
}