package com.transferz.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Table(name = "passengers")
@Data
@Entity
@Builder
@AllArgsConstructor
public class Passenger {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	@Column(name = "passengerName", nullable = false)
	private String name;

	@Column(name = "flight_code", nullable = false)
	private String flightCode;

	public Passenger() {

	}

	//todo create unique constraint for complex field (passengerName , flight_code)
}
