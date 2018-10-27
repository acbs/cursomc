package com.andrecarlos.cursomc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.andrecarlos.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {

	@Value("${default.sender}")
	private String sender;
	
	@Override
	public void sendOrderConfirmationEmail(Pedido obj) {
		
		SimpleMailMessage msg = prepareSimpleMailMessageFromPedido(obj);
		sendEmail(msg);
	}

	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido obj) {
		
		SimpleMailMessage msg = new SimpleMailMessage();
		// Destinatário
		msg.setTo(obj.getCliente().getEmail());
		// Remetente
		msg.setFrom(sender);
		// Informando o assunto
		msg.setSubject("Pedido confirmado! Código: " + obj.getId());
		// Setando a data do email com horário do servidor
		msg.setSentDate(new Date(System.currentTimeMillis()));
		// Corpo do email
		msg.setText(obj.toString());
		return msg;
	}
}
