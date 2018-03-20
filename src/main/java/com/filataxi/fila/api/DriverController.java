package com.filataxi.fila.api;

import com.filataxi.fila.model.Driver;
import com.filataxi.fila.repository.DriverRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/driver")
@AllArgsConstructor
public class DriverController {

	private DriverRepository driverRepository;

	@PostMapping
	public Driver addDriver(@RequestBody Driver driver) {
		return driverRepository.save(driver);
	}

	@GetMapping
	public List<Driver> getDrivers() {
		return driverRepository.findAll();
	}

	@PutMapping("/{id}")
	public void editDriver(@PathVariable Integer id, @RequestBody Driver driver) {
		Driver existingDriver = driverRepository.findOne(id);
		driver.setName(driver.getName());
		driverRepository.save(existingDriver);
	}

	@DeleteMapping("/{id}")
	public void deleteDriver(@PathVariable Integer id) {
		driverRepository.delete(id);
	}
}
