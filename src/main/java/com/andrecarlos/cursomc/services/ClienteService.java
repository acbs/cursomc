package com.andrecarlos.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andrecarlos.cursomc.domain.Cliente;
import com.andrecarlos.cursomc.repositories.ClienteRepository;
import com.andrecarlos.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired // Quer dizer q ela será instanciada automaticamente pelo spring
	private ClienteRepository repo;

	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

}
