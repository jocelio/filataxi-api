package com.filataxi.fila.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Driver {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private String email;

	private Boolean enabled = Boolean.TRUE;

	public Driver disable(){
		return Driver.builder().email(email).id(id).name(name).enabled(FALSE).build();
	}

	public Driver enable(){
		return Driver.builder().email(email).id(id).name(name).enabled(TRUE).build();
	}

	@Override
	public String toString() {
		return "Driver{" +
				"name='" + name + '\'' +
				'}';
	}
}
