package com.andrecarlos.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andrecarlos.cursomc.domain.Categoria;
import com.andrecarlos.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired // Quer dizer q ela será instanciada automaticamente pelo spring
	private CategoriaRepository repo;
	
	public Categoria buscar(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElse(null); // Se o obj retorna fot null, retorna null
	}
	
}
