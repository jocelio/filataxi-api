package com.filataxi.fila.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Position {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
	private Driver driver;

	private Integer index;

	private Status status;

	private LocalDate date;

	public Position down(){
		return Position.builder()
				.id(id)
				.driver(driver)
				.status(status)
				.date(date)
				.index(index+1).build();
	}

	public Position up(){
		return Position.builder()
				.id(id)
				.driver(driver)
				.status(status)
				.date(date)
				.index(index-1).build();
	}

	public Position up(Integer qty){
		return Position.builder()
				.id(id)
				.driver(driver)
				.status(status)
				.date(date)
				.index(index-qty).build();
	}

	public Position withIndex(Integer index){
		return Position.builder()
				.id(id)
				.driver(driver)
				.status(status)
				.date(date)
				.index(index).build();
	}

	public Position withStatus(Status status){
		return Position.builder()
				.id(id)
				.driver(driver)
				.status(status)
				.date(date)
				.index(index).build();
	}

	@Override
	public String toString() {
		return "Position{" +
				"driver=" + driver +
				", index=" + index +
				", status=" + status +
				", date=" + date +
				'}';
	}
}
