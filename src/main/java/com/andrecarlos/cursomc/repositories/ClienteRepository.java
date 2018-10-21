package com.andrecarlos.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.andrecarlos.cursomc.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	
	// findBy + nome do campo -> O Spring detecta automaticamente que queremos fazer uma busca por email
	@Transactional(readOnly=true) // readOnly=true - não necessita ser envlvida como uma transação de db (evita lock no db)
	Cliente findByEmail(String email);
}
