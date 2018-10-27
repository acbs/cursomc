package com.andrecarlos.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.andrecarlos.cursomc.services.DBService;
import com.andrecarlos.cursomc.services.EmailService;
import com.andrecarlos.cursomc.services.SmtpEmailService;

@Configuration
@Profile("dev")
public class DevConfig {

	@Autowired
	private DBService dbService;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		
		if (!"create".equals(strategy)) {
			return false;
		}
		
		dbService.instantiateTestDatabase();
		return true;
	}
	
	// Deixando esse metodo disponível, para quando for injetado (@Autowired) no SmtpEmailService, ele reconhecer e retornar o SmtpEmailService
	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}
}
