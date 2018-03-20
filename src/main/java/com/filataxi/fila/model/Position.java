package com.filataxi.fila.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Position {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Driver driver;

	private Integer index;

	public Position down(){
		return Position.builder().driver(driver).id(id).index(++index).build();
	}

	public Position up(){
		return Position.builder().driver(driver).id(id).index(--index).build();
	}

	public Position up(Integer qty){
		return Position.builder().driver(driver).id(id).index(qty-index).build();
	}

	public Position withIndex(Integer index){
		return Position.builder().driver(driver).id(id).index(index).build();
	}
	
}
