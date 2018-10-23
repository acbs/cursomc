package com.andrecarlos.cursomc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// CommandLineRunner(run) - Utilizado para executar alguma ação quando aplicação for iniciada
@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {


	
	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
