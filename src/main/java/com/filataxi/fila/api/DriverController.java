package com.filataxi.fila.api;

import com.filataxi.fila.model.Driver;
import com.filataxi.fila.model.Position;
import com.filataxi.fila.model.Status;
import com.filataxi.fila.repository.DriverRepository;
import com.filataxi.fila.repository.PositionRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Comparator.comparing;

@RestController
@RequestMapping("/driver")
@AllArgsConstructor
public class DriverController {

	private DriverRepository driverRepository;

	private PositionRepository positionRepository;

	@PostMapping("init")
	public void init() {
		driverRepository.save(Driver.builder().name("Jocelio").email("jocelio@mail.com").build());
		driverRepository.save(Driver.builder().name("Rafaele").email("rafaele@mail.com").build());
		driverRepository.save(Driver.builder().name("Heloise").email("heloise@mail.com").build());
		driverRepository.save(Driver.builder().name("Maria Luiza").email("marialuiza@mail.com").build());
	}

	@PostMapping
	public Driver addDriver(@RequestBody Driver driver) {
		return driverRepository.save(driver);
	}

	@PostMapping("/disable/{id}")
	public Driver disable(@PathVariable Integer id) {
		Driver one = driverRepository.findOne(id);
		positionRepository.deleteByDriverId(one.getId());
		return driverRepository.save(one.disable());
	}

	@PostMapping("/enable/{id}")
	public Driver enable(@PathVariable Integer id) {

		Driver one = driverRepository.findOne(id);

		List<Position> all = positionRepository.findAllByOrderByIndexAsc();
		Position position = all.stream().sorted(comparing(Position::getIndex)).findFirst().get();
		Position newPosition = Position.builder().status(Status.AGUARDANDO).driver(one).index(position.getIndex() + 1).build();
		positionRepository.save(newPosition);

		return driverRepository.save(one.enable());
	}

	@GetMapping
	public List<Driver> getDrivers() {
		return driverRepository.findAll();
	}

	@PutMapping("/{id}")
	public Driver editDriver(@PathVariable Integer id, @RequestBody Driver driver) {
		driver.setId(id);
		return driverRepository.save(driver);
	}

	@DeleteMapping("/{id}")
	public void deleteDriver(@PathVariable Integer id) {
		positionRepository.deleteByDriverId(id);
		driverRepository.delete(id);
	}
}
