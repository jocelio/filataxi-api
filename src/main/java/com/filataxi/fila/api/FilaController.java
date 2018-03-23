package com.filataxi.fila.api;

import com.filataxi.fila.model.Driver;
import com.filataxi.fila.model.Position;
import com.filataxi.fila.model.Status;
import com.filataxi.fila.repository.DriverRepository;
import com.filataxi.fila.repository.PositionRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.IntStream;

import static com.filataxi.fila.model.Status.AGUARDANDO;
import static com.filataxi.fila.model.Status.RODANDO;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/fila")
@AllArgsConstructor
public class FilaController {

	private DriverRepository driverRepository;

	private PositionRepository positionRepository;

	@GetMapping
	public List<Position> get() {
		return positionRepository.findAllByOrderByIndexAsc();
	}

	@PostMapping("head")
	public List<Position> head() {

		List<Position> all = positionRepository.findAllByOrderByIndexAsc();

		Position position = all.stream().sorted(comparing(Position::getIndex)).findFirst().get();
		Position lastIndex = all.stream().sorted(comparing(Position::getIndex).reversed()).findFirst().get();

		List<Position> updates = all.stream()
				.filter(p -> p.getIndex() > position.getIndex())
				.map(Position::up)
				.collect(toList());

		updates.add(position.withIndex(lastIndex.getIndex()));

		positionRepository.save(updates);

		return positionRepository.findAllByOrderByIndexAsc();
	}



	@PostMapping("enqueue")
	public List<Position> enqueue() {

		positionRepository.deleteAll();

		List<Driver> all = driverRepository.findByEnabledTrue();

		List<Position> positions = IntStream.range(0, all.size())
				.mapToObj(o -> Position.builder().driver(all.get(o)).index(++o).status(AGUARDANDO).build())
				.collect(toList());

		return positionRepository.save(positions);

	}

	@PostMapping("move-up/{id}/{qtyPositions}")
	public List<Position> moveUp(@PathVariable Integer id, @PathVariable Integer qtyPositions) {

		List<Position> all = positionRepository.findAllByOrderByIndexAsc();

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

		positionRepository.save(updates);

		return positionRepository.findAllByOrderByIndexAsc();

	}

	@PutMapping("change-status/{id}")
	public Position changeStatus(@PathVariable Integer id) {
		Position existingDriver = positionRepository.findOne(id);
		Status status = existingDriver.getStatus().equals(AGUARDANDO) ?RODANDO :AGUARDANDO;
		existingDriver.setStatus(status);
		return positionRepository.save(existingDriver);
	}


}
