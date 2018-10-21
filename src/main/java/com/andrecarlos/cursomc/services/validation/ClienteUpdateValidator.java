package com.andrecarlos.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.andrecarlos.cursomc.domain.Cliente;
import com.andrecarlos.cursomc.dto.ClienteDTO;
import com.andrecarlos.cursomc.repositories.ClienteRepository;
import com.andrecarlos.cursomc.resources.exception.FieldMessage;

// ClienteUpdate - nome da anotação que será referenciada
// ClienteDTO - tipo de dados que será aceitado pela anotação
public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ClienteRepository repo; 
	
	@Override
	public void initialize(ClienteUpdate ann) {
		// Caso queira colocar alguma programação de inicialização
	}

	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		
		// Pegando o map das variáveis da URI
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String> ) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriId = Integer.parseInt(map.get("id"));
		
		List<FieldMessage> list = new ArrayList<>();

		Cliente aux = repo.findByEmail(objDto.getEmail());
		
		if (aux != null && !aux.getId().equals(uriId)) {
			list.add(new FieldMessage("email", "Email já existente"));
		}
		
		// Tranportando os erros personalizados, para a lista de erro do framework
		// Está lista de erro será tratado no ExceptionHandler
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
		}
		return list.isEmpty();
	}
}