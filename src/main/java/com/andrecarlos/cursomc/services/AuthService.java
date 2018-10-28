package com.andrecarlos.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.andrecarlos.cursomc.domain.Cliente;
import com.andrecarlos.cursomc.repositories.ClienteRepository;
import com.andrecarlos.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private EmailService emailService;
	
	private Random rand = new Random();
	
	// Responsável por gerar uma nova senha para o usuário e enviar email
	public void sendNewPassword(String email) {
		
		Cliente cliente = clienteRepository.findByEmail(email);
		if (cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		
		String newPass = newPassword();
		cliente.setSenha(pe.encode(newPass));
		
		clienteRepository.save(cliente);
		
		emailService.sendNewPasswordEmail(cliente, newPass);
	}

	// Gerando uma senha aleatória
	private String newPassword() {
		
		char[] vet = new char[10];
		for (int i = 0; i < 10; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	private char randomChar() {
		
		// Gerando um número inteiro de 0 até 2
		int opt = rand.nextInt(3);
		
		// Para mais informações (https://unicode-table.com/pt/)
		if (opt == 0) { // Gera um dígito
			// retorna um número aleatório de 0 até 9 + 48(código do zero)
			return (char) (rand.nextInt(10) + 48);
		} else if (opt == 1) { // Gera uma letra maiúscula
			// são 26 letra possíveis + 65(código do A)
			return (char) (rand.nextInt(26) + 65);
		} else { // Gera letra minúscula
			// são 26 letra possíveis + 65(código do a)
			return (char) (rand.nextInt(26) + 97);
		}
	}
}
