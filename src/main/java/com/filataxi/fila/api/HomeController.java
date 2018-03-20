package com.filataxi.fila.api;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {


	@GetMapping
	public String get() {
		return "Hello World";
	}


}
