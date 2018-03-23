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

	private HistoryType historyType;

	private LocalDateTime time;

	public static HistoryData enterQueue(Position position){
		return HistoryData.builder().position(position).historyType(ENTER_QUEUE).build();
	}

	public static HistoryData movePosition(Position position){
		return HistoryData.builder().position(position).historyType(MOVE_POSITION).build();
	}

	public static HistoryData changeStatus(Position position){
		return HistoryData.builder().position(position).historyType(CHANGE_STATUS).build();
	}

	public static HistoryData exitQueue(Driver driver){
		return HistoryData.builder().driver(driver).historyType(EXIT_QUEUE).build();
	}

	@JsonIgnore
	@Transient
	private Position position;

	@JsonIgnore
	@Transient
	private Driver driver;

}
