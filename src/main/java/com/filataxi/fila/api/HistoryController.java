package com.filataxi.fila.api;

import com.filataxi.fila.model.HistoryData;
import com.filataxi.fila.repository.HistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/history")
@AllArgsConstructor
public class HistoryController {

	private HistoryRepository historyRepository;

	@GetMapping
	public List<HistoryData> get() {
		return historyRepository.findAll();
	}
}
