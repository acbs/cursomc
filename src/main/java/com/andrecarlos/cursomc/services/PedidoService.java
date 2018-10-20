package com.andrecarlos.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andrecarlos.cursomc.domain.Pedido;
import com.andrecarlos.cursomc.repositories.PedidoRepository;
import com.andrecarlos.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired // Quer dizer q ela será instanciada automaticamente pelo spring
	private PedidoRepository repo;

	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}

}
