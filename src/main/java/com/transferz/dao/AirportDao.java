package com.transferz.dao;

import com.transferz.entity.Airport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface AirportDao extends JpaRepository<Airport, Long> {

    Optional<Airport> findByCodeOrName(@NotNull String code, @NotNull String name);

    Optional<Airport> findByCode(@NotNull String code);

    List<Airport> findAllByNameInOrCodeIn(List<String> names, List<String> codes, Pageable pageable);

    List<Airport> findAllByNameIn(List<String> names, Pageable pageable);

    List<Airport> findAllByCodeIn(List<String> codes, Pageable pageable);
}