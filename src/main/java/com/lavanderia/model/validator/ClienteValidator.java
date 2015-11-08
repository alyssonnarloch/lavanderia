package com.lavanderia.model.validator;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.lavanderia.model.Cliente;
import com.lavanderia.util.Validation;

@Component
public class ClienteValidator extends Validation implements Validator {

	@Autowired
	private SessionFactory sessionFactory;

	public boolean supports(Class clazz) {
		return Cliente.class.isAssignableFrom(clazz);
	}

	public void validate(Object o, Errors errors) {
		Cliente cliente = (Cliente) o;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "error.nome",
				"Campo de preenchimento obrigatório.");
		if (cliente.getNome() != null && !cliente.getNome().equals("")
				&& !this.validLength(cliente.getNome(), 255)) {
			errors.rejectValue("nome", "nome.errors",
					"Conteúdo excede tamanho do campo.");
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "sexo", "error.sexo",
				"Campo de preenchimento obrigatório.");

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cpf", "error.cpf",
				"Campo de preenchimento obrigatório.");
		if (cliente.getCpf() != null && !cliente.getCpf().equals("")) {
			if (!this.isUnique(cliente.getId(), Cliente.class.getSimpleName(),
					cliente.getCpf(), "cpf")) {
				errors.rejectValue("cpf", "cpf.errors",
						"Esse CPF já foi usado por outro cliente.");
			}
			if (!this.validCpf(cliente.getCpf())) {
				errors.rejectValue("cpf", "cpf.errors",
						"Por favor, introduza um CPF válido.");
			}
			if (!this.validLength(cliente.getCpf(), 20)) {
				errors.rejectValue("cpf", "cpf.errors",
						"Conteúdo excede tamanho do campo.");
			}
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email",
				"error.email", "Campo de preenchimento obrigatório.");

		if (cliente.getEmail() != null && !cliente.getEmail().isEmpty()) {
			if (!this.isUnique(cliente.getId(), Cliente.class.getSimpleName(),
					cliente.getEmail(), "email")) {
				errors.rejectValue("email", "email.errors",
						"Esse e-mail já foi usado por outro cliente.");
			}
			if (!this.validEmail(cliente.getEmail())) {
				errors.rejectValue("email", "email.errors",
						"Por favor, introduza um email válido.");
			}
			if (!this.validLength(cliente.getEmail(), 255)) {
				errors.rejectValue("email", "email.errors",
						"Conteúdo excede tamanho do campo.");
			}
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "endereco",
				"error.endereco", "Campo de preenchimento obrigatório.");
		if (cliente.getEndereco() != null && !cliente.getEndereco().isEmpty()
				&& !this.validLength(cliente.getEndereco(), 255)) {
			errors.rejectValue("endereco", "endereco.errors",
					"Conteúdo excede tamanho do campo.");
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "telefone",
				"error.telefone", "Campo de preenchimento obrigatório.");
		if (cliente.getTelefone() != null && !cliente.getTelefone().isEmpty()
				&& !this.validLength(cliente.getTelefone(), 25)) {
			errors.rejectValue("telefone", "telefone.errors",
					"Conteúdo excede tamanho do campo.");
		}

		if (cliente.getId() == 0) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "senha",
					"error.senha", "Campo de preenchimento obrigatório.");

			ValidationUtils.rejectIfEmptyOrWhitespace(errors,
					"confirmacaoSenha", "error.confirmacaoSenha",
					"Campo de preenchimento obrigatório.");
		}

		if (!cliente.getSenha().isEmpty()
				&& !cliente.getConfirmacaoSenha().equals(cliente.getSenha())) {
			errors.rejectValue("confirmacaoSenha", "confirmacaoSenha.errors",
					"A confirmação deve ser igual a senha.");
			if (!this.validLength(cliente.getSenha(), 255)) {
				errors.rejectValue("senha", "senha.errors",
						"Conteúdo excede tamanho do campo.");
			}
		}
	}

}
