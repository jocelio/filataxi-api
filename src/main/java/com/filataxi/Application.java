package com.filataxi;

import com.filataxi.fila.service.HistoryConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.Environment;
import reactor.bus.EventBus;

import static reactor.bus.selector.Selectors.$;
@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private EventBus eventBus;

	@Autowired
	private HistoryConsumer historyConsumer;

	@Bean
	Environment env() {
		return Environment.initializeIfEmpty().assignErrorJournal();
	}

	@Bean
	EventBus createEventBus(Environment env) {
		return EventBus.create(env, Environment.THREAD_POOL);
	}

	@Override
	public void run(String... args) throws Exception {
		eventBus.on($("historyConsumer"), historyConsumer);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
