package com.andrecarlos.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.andrecarlos.cursomc.services.DBService;
import com.andrecarlos.cursomc.services.EmailService;
import com.andrecarlos.cursomc.services.MockEmailService;

@Configuration
@Profile("test")
public class TesteConfig {

	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		dbService.instantiateTestDatabase();
		return true;
	}
	
	// Deixando esse metodo disponível, para quando for injetado (@Autowired) no PedidoService, ele reconhecer e retornar o MockEmailService
	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}
}
