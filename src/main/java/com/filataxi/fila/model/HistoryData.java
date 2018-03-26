package com.filataxi.fila.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.filataxi.fila.model.HistoryType.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String description;

	private Integer idDriver;

	private HistoryType historyType;

	private LocalDateTime time;

	@JsonIgnore
	@Transient
	private Position position;

	@JsonIgnore
	@Transient
	private Driver driver;

	public static HistoryData enterQueue(Position position){
		return HistoryData.builder().idDriver(position.getDriver().getId()).position(position).historyType(ENTER_QUEUE).build();
	}

	public static HistoryData movePosition(Position position){
		return HistoryData.builder().idDriver(position.getDriver().getId()).position(position).historyType(MOVE_POSITION).build();
	}

	public static HistoryData changeStatus(Position position){
		return HistoryData.builder().idDriver(position.getDriver().getId()).position(position).historyType(CHANGE_STATUS).build();
	}

	public static HistoryData nextQueue(Position position){
		return HistoryData.builder().idDriver(position.getDriver().getId()).position(position).historyType(NEXT_QUEUE).build();
	}

	public static HistoryData exitQueue(Driver driver){
		return HistoryData.builder().idDriver(driver.getId()).driver(driver).historyType(EXIT_QUEUE).build();
	}

}
