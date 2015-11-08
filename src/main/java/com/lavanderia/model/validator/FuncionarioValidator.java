package com.lavanderia.model.validator;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.lavanderia.model.Funcionario;
import com.lavanderia.util.Validation;

@Component
public class FuncionarioValidator extends Validation implements Validator {

	@Autowired
	private SessionFactory sessionFactory;

	public boolean supports(Class clazz) {
		return Funcionario.class.isAssignableFrom(clazz);
	}

	public void validate(Object o, Errors errors) {
		Funcionario funcionario = (Funcionario) o;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "error.nome",
				"Campo de preenchimento obrigatório.");
		if (funcionario.getNome() != null && !funcionario.getNome().equals("")
				&& !this.validLength(funcionario.getNome(), 255)) {
			errors.rejectValue("nome", "nome.errors",
					"Conteúdo excede tamanho do campo.");
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "matricula",
				"error.matricula", "Campo de preenchimento obrigatório.");
		if (funcionario.getMatricula() != null
				&& !funcionario.getMatricula().equals("")) {
			if (!this.isUnique(funcionario.getId(),
					Funcionario.class.getSimpleName(),
					funcionario.getMatricula(), "matricula")) {
				errors.rejectValue("matricula", "matricula.errors",
						"Essa matrícula já foi usada por outro funcionário.");
			}
			if (!this.validLength(funcionario.getMatricula(), 20)) {
				errors.rejectValue("matricula", "matricula.errors",
						"Conteúdo excede tamanho do campo.");
			}
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email",
				"error.email", "Campo de preenchimento obrigatório.");
		if (funcionario.getEmail() != null
				&& !funcionario.getEmail().equals("")) {
			if (!this.isUnique(funcionario.getId(),
					Funcionario.class.getSimpleName(), funcionario.getEmail(),
					"email")) {
				errors.rejectValue("email", "email.errors",
						"Esse email já foi usado por outro funcionário.");
			}
			if (!this.validEmail(funcionario.getEmail())) {
				errors.rejectValue("email", "email.errors",
						"Por favor, introduza um email válido.");
			}
			if (!this.validLength(funcionario.getEmail(), 255)) {
				errors.rejectValue("email", "email.errors",
						"Conteúdo excede tamanho do campo.");
			}
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dataNascimento",
				"error.dataNascimento", "Campo de preenchimento obrigatório.");
		if (!this.validDate(funcionario.getDataNascimento())) {
			errors.rejectValue("dataNascimento", "dataNascimento.errors",
					"Por favor, introduza uma data válida.");
		}

		if (funcionario.getId() == 0) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "senha",
					"error.senha", "Campo de preenchimento obrigatório.");

			ValidationUtils.rejectIfEmptyOrWhitespace(errors,
					"confirmacaoSenha", "error.confirmacaoSenha",
					"Campo de preenchimento obrigatório.");
		}

		if (!funcionario.getSenha().isEmpty()
				&& !funcionario.getConfirmacaoSenha().equals(
						funcionario.getSenha())) {
			errors.rejectValue("confirmacaoSenha", "confirmacaoSenha.errors",
					"A confirmação deve ser igual a senha.");
			if (!this.validLength(funcionario.getSenha(), 50)) {
				errors.rejectValue("senha", "senha.errors",
						"Conteúdo excede tamanho do campo.");
			}
		}
	}

}
