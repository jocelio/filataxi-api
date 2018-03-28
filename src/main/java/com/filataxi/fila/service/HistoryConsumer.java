package com.filataxi.fila.service;

import com.filataxi.fila.model.HistoryData;
import com.filataxi.fila.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.fn.Consumer;

import static com.filataxi.fila.model.HistoryType.*;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;

@Service
public class HistoryConsumer implements Consumer<Event<HistoryData>> {

	@Autowired
	private HistoryRepository historyRepository;

	@Override
	public void accept(Event<HistoryData> notificationDataEvent) {

		HistoryData historyData = notificationDataEvent.getData();

		if(historyData.getHistoryType().equals(ENTER_QUEUE)) {
			historyData.setDescription(format("Motorista %s entrou na fila na posição %s."
				, historyData.getPosition().getDriver().getName()
				, historyData.getPosition().getIndex()));
		}

		if(historyData.getHistoryType().equals(EXIT_QUEUE)) {
			historyData.setDescription(format("Motorista %s saiu da fila."
				, historyData.getDriver().getName()));
		}

		if(historyData.getHistoryType().equals(MOVE_POSITION)) {
			historyData.setDescription(format("Motorista %s foi movido para a posição %s."
				, historyData.getPosition().getDriver().getName()
				, historyData.getPosition().getIndex()));
		}

		if(historyData.getHistoryType().equals(CHANGE_STATUS)) {
			historyData.setDescription(format("Motorista %s teve o status alterado para %s."
				, historyData.getPosition().getDriver().getName()
				, historyData.getPosition().getStatus().toString()));
		}

		if(historyData.getHistoryType().equals(NEXT_QUEUE)) {
			historyData.setDescription(format("Motorista %s foi colocado na fila do dia %s na posição %s."
				, historyData.getPosition().getDriver().getName()
				, historyData.getPosition().getDate()
				, historyData.getPosition().getIndex()));
		}

		if(historyData.getHistoryType().equals(SELF_EXIT_QUEUE)) {
			historyData.setDescription(format("Motorista %s saiu espontaneamente da fila."
				, historyData.getDriver().getName()));
		}

		historyData.setTime(now());
		historyRepository.save(historyData);

	}
}
