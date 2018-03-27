package com.filataxi.fila.api;

import com.filataxi.fila.model.Driver;
import com.filataxi.fila.model.HistoryData;
import com.filataxi.fila.model.Position;
import com.filataxi.fila.repository.DriverRepository;
import com.filataxi.fila.repository.PositionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.List;
import java.util.stream.IntStream;

import static com.filataxi.fila.model.Status.AGUARDANDO;
import static java.lang.Boolean.FALSE;
import static java.time.LocalDate.now;
import static java.util.Comparator.comparing;

@RestController
@RequestMapping("/driver")
@AllArgsConstructor
public class DriverController {

	private DriverRepository driverRepository;

	private PositionRepository positionRepository;

	@Autowired
	private EventBus eventBus;

	@PostMapping("init")
	public void init() {
		driverRepository.save(Driver.builder().enabled(FALSE).name("Jocelio").email("jclls@hotmail.com").build());
		driverRepository.save(Driver.builder().enabled(FALSE).name("Rafaele").email("rafaele@mail.com").build());
		driverRepository.save(Driver.builder().enabled(FALSE).name("Heloise").email("heloise@mail.com").build());
		driverRepository.save(Driver.builder().enabled(FALSE).name("Maria Luiza").email("marialuiza@mail.com").build());
	}

	@PostMapping
	public Driver addDriver(@RequestBody Driver driver) {
		return driverRepository.save(driver);
	}

	@PostMapping("/disable/{id}")
	public Driver disable(@PathVariable Integer id) {
		Driver one = driverRepository.findOne(id);
		positionRepository.deleteByDriverId(one.getId());

		List<Position> all = positionRepository.findAllByDateOrderByIndexAsc(now());
		IntStream.range(0, all.size()).mapToObj(i -> all.get(i).withIndex(i+1))
				.forEach(positionRepository::save);

		eventBus.notify("historyConsumer", Event.wrap(HistoryData.exitQueue(one)));

		return driverRepository.save(one.disable());
	}

	@PostMapping("/enable/{id}")
	public Driver enable(@PathVariable Integer id) {

		Driver one = driverRepository.findOne(id);

		List<Position> all = positionRepository.findAllByDateOrderByIndexAsc(now());
		Position position = all.stream().sorted(comparing(Position::getIndex).reversed()).findFirst().orElse(Position.builder().index(0).build());
		Position newPosition = Position.builder().date(now()).status(AGUARDANDO).driver(one).index(position.getIndex() + 1).build();
		positionRepository.save(newPosition);

		eventBus.notify("historyConsumer", Event.wrap(HistoryData.enterQueue(newPosition)));

		return driverRepository.save(one.enable());
	}

	@GetMapping
	public List<Driver> getDrivers() {
		return driverRepository.findAll();
	}

	@GetMapping("/{email}")
	public Driver fromEmail(@PathVariable String email) {
		return driverRepository.findByEmail(email.concat(".com"));
	}

	@PutMapping("/{id}")
	public Driver editDriver(@PathVariable Integer id, @RequestBody Driver driver) {
		driver.setId(id);
		return driverRepository.save(driver);
	}

	@DeleteMapping("/{id}")
	public void deleteDriver(@PathVariable Integer id) {

		positionRepository.deleteByDriverId(id);

		List<Position> all = positionRepository.findAllByDateOrderByIndexAsc(now());
		IntStream.range(0, all.size()).mapToObj(i -> all.get(i).withIndex(i+1))
				.forEach(positionRepository::save);

		driverRepository.delete(id);
	}


}
