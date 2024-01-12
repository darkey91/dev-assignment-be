package com.transferz.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Table(name = Airport.TABLE_NAME)
@Data
@Entity
@Builder
@AllArgsConstructor
public class Airport {

	public final static String TABLE_NAME = "airports";

	public final static String NAME = "passengerName";

	public final static String CODE = "code";

//todo 	"validation for fields" а какие ограничения елки?
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	@Column(name = "passengerName", nullable = false)
	private String name;

	@Column(name = "code", nullable = false, unique = true)
	private String code;

	@Column(name = "country", nullable = false)
	private String country;

	public Airport() { }

}
