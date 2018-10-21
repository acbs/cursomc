package com.andrecarlos.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.andrecarlos.cursomc.domain.enums.TipoCliente;
import com.andrecarlos.cursomc.dto.ClienteNewDTO;
import com.andrecarlos.cursomc.resources.exception.FieldMessage;
import com.andrecarlos.cursomc.services.validation.utils.BR;

// ClienteInsert - nome da anotação que será referenciada
// ClienteNewDTO - tipo de dados que será aceitado pela anotação
public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	
	@Override
	public void initialize(ClienteInsert ann) {
		// Caso queira colocar alguma programação de inicialização
	}

	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();

		if (objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCodigo()) &&
				!BR.isValidCPF(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
		}
		
		if (objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCodigo()) &&
				!BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
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