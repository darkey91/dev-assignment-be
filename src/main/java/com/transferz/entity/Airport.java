package com.transferz.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Table(name = Airport.TABLE_NAME)
@Data
@Entity
@Builder
@AllArgsConstructor
public class Airport {

	public final static String TABLE_NAME = "airport";

	public final static String NAME = "name";

	public final static String CODE = "code";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "airport_id_seq")
	@SequenceGenerator(name="airport_id_seq", allocationSize=1)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "code", nullable = false, unique = true)
	private String code;

	@Column(name = "country", nullable = false)
	private String country;

	public Airport() { }

}
