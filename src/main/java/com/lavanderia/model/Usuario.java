package com.lavanderia.model;

import java.io.Serializable;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "usuarios")
@DiscriminatorColumn(name = "tipoUsuario", discriminatorType = DiscriminatorType.INTEGER)
public class Usuario implements Serializable {

	@Id
	@GeneratedValue
	private int id;

	private String nome;

	private String email;

	private String senha;

	@Transient
	private String confirmacaoSenha;

	private Integer situacao;

	public final static int TODOS = 2;
	public final static int ATIVOS = 1;
	public final static int INATIVOS = 0;

	public Usuario() {
	}

	public Usuario(int id, String nome, String email, String senha,
			String confirmacaoSenha) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.confirmacaoSenha = confirmacaoSenha;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Integer getSituacao() {
		return situacao;
	}

	public void setSituacao(Integer situacao) {
		this.situacao = situacao;
	}

	public String getConfirmacaoSenha() {
		return confirmacaoSenha;
	}

	public void setConfirmacaoSenha(String confirmacaoSenha) {
		this.confirmacaoSenha = confirmacaoSenha;
	}

	public String getSituacaoVerbose() {
		if (this.situacao == ATIVOS) {
			return "Ativo";
		} else {
			return "Inativo";
		}
	}

}
