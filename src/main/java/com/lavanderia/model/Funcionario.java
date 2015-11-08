package com.lavanderia.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@DiscriminatorValue("2")
public class Funcionario extends Usuario {

	private String matricula;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dataNascimento;

	public Funcionario() {
	}

	public Funcionario(String matricula, Date dataNascimento, int id,
			String nome, String email, String senha, String confirmacaoSenha) {
		super(id, nome, email, senha, confirmacaoSenha);
		this.matricula = matricula;
		this.dataNascimento = dataNascimento;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getDataNascimentoVerbose() {
		return new SimpleDateFormat("dd/MM/yyyy").format(this.dataNascimento);
	}

}
