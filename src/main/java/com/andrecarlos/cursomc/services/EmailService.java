package com.andrecarlos.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.andrecarlos.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
}
