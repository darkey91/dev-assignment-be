package com.transferz.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Table(name = Passenger.TABLE_NAME)
@Data
@Entity
@Builder
@AllArgsConstructor
public class Passenger {

	public final static String TABLE_NAME = "passenger";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passenger_id_seq")
	@SequenceGenerator(name="passenger_id_seq", allocationSize=1)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "flight_code", nullable = false)
	private String flightCode;

	public Passenger() {

	}
}
