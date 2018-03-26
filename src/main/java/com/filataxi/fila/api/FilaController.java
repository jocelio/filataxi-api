package com.filataxi.fila.api;

import com.filataxi.fila.model.Driver;
import com.filataxi.fila.model.HistoryData;
import com.filataxi.fila.model.Position;
import com.filataxi.fila.model.Status;
import com.filataxi.fila.repository.DriverRepository;
import com.filataxi.fila.repository.PositionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import static com.filataxi.fila.model.Status.AGUARDANDO;
import static com.filataxi.fila.model.Status.RODANDO;
import static java.time.LocalDate.now;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/fila")
@AllArgsConstructor
public class FilaController {

	private DriverRepository driverRepository;

	private PositionRepository positionRepository;

	@Autowired
	private EventBus eventBus;

	@GetMapping
	public List<Position> get() {
		return positionRepository.findAllByDateOrderByIndexAsc(now());
	}

	@PostMapping("head")
	public List<Position> head() {

		List<Position> all = positionRepository.findAllByDateOrderByIndexAsc(now());

		Position position = all.stream().sorted(comparing(Position::getIndex)).findFirst().get();
		Position lastIndex = all.stream().sorted(comparing(Position::getIndex).reversed()).findFirst().get();

		List<Position> updates = all.stream()
				.filter(p -> p.getIndex() > position.getIndex())
				.map(Position::up)
				.collect(toList());

		updates.add(position.withIndex(lastIndex.getIndex()));

		updates.forEach(u -> {
			eventBus.notify("historyConsumer", Event.wrap(HistoryData.movePosition(u)));
		});

		positionRepository.save(updates);

		return positionRepository.findAllByDateOrderByIndexAsc(now());
	}



	@PostMapping("enqueue")
	public List<Position> enqueue() {

		positionRepository.deleteAll();

		List<Driver> all = driverRepository.findByEnabledTrue();

		List<Position> positions = IntStream.range(0, all.size())
				.mapToObj(o -> Position.builder().date(now()).driver(all.get(o)).index(++o).status(AGUARDANDO).build())
				.collect(toList());

		return positionRepository.save(positions);

	}

	@PostMapping("move-up/{id}/{qtyPositions}")
	public List<Position> moveUp(@PathVariable Integer id, @PathVariable Integer qtyPositions) {

		List<Position> all = positionRepository.findAllByDateOrderByIndexAsc(now());

		Position position = all.stream().filter(p -> p.getId().equals(id)).findAny().get();

		if(position.getIndex() - qtyPositions < 1){
			return null;
		}

		List<Position> updates = all.stream()
				.filter(f -> !f.equals(position))
				.filter(p -> p.getIndex() >= (position.getIndex() - qtyPositions) && p.getIndex() < position.getIndex())
				.map(m -> m.down())
				.collect(toList());

		updates.add(position.up(qtyPositions).withStatus(AGUARDANDO));

		updates.forEach(u -> {
			eventBus.notify("historyConsumer", Event.wrap(HistoryData.movePosition(u)));
		});

		positionRepository.save(updates);

		return positionRepository.findAllByDateOrderByIndexAsc(now());

	}

	@PutMapping("change-status/{id}")
	public Position changeStatus(@PathVariable Integer id) {
		Position position = positionRepository.findOne(id);
		Status status = position.getStatus().equals(AGUARDANDO) ?RODANDO :AGUARDANDO;
		position.setStatus(status);

		eventBus.notify("historyConsumer", Event.wrap(HistoryData.changeStatus(position)));

		return positionRepository.save(position);
	}

	@PostMapping("next-queue/{days}")
	public List<Position> nextQueue(@PathVariable Integer days) {

		List<Position> today = positionRepository.findAllByDateOrderByIndexAsc(now());

		List<Position> newQueue = today.stream()
				.filter(f -> f.getStatus().equals(AGUARDANDO))
				.map(p -> Position.builder()
						.date(LocalDate.now().plusDays(days))
						.index(p.getIndex())
						.driver(p.getDriver())
						.status(AGUARDANDO)
						.build())
				.sorted(Comparator.comparing(Position::getIndex))
				.collect(toList());

		List<Position> positions = IntStream.range(0, newQueue.size()).mapToObj(i -> newQueue.get(i).withIndex(i + 1)).collect(toList());

		positions.forEach(p -> {
				eventBus.notify("historyConsumer", Event.wrap(HistoryData.nextQueue(p)));
		});

		return positionRepository.save(positions);

	}


}
