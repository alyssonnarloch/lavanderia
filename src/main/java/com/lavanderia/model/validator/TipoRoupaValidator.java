package com.lavanderia.model.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.lavanderia.model.TipoRoupa;
import com.lavanderia.util.Validation;

@Component
public class TipoRoupaValidator extends Validation implements Validator {

	public boolean supports(Class clazz) {
		return TipoRoupa.class.isAssignableFrom(clazz);
	}

	public void validate(Object o, Errors errors) {
		TipoRoupa tipoRoupa = (TipoRoupa) o;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "error.nome",
				"Campo de preenchimento obrigatório.");
		if (tipoRoupa.getNome() != null && !tipoRoupa.getNome().equals("")
				&& !this.validLength(tipoRoupa.getNome(), 255)) {
			errors.rejectValue("nome", "nome.errors",
					"Conteúdo excede tamanho do campo.");
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "preco",
				"error.preco", "Campo de preenchimento obrigatório.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "diasLavagem",
				"error.diasLavagem", "Campo de preenchimento obrigatório.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "situacao",
				"error.situacao", "Campo de preenchimento obrigatório.");		
		if (!this.isUnique(tipoRoupa.getId(), TipoRoupa.class.getSimpleName(),
				tipoRoupa.getNome(), "nome")) {
			errors.rejectValue("nome", "nome.errors",
					"Esse nome já foi usado em outra roupa.");
		}
	}

}
