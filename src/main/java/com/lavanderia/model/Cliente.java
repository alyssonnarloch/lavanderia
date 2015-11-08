package com.lavanderia.model;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("1")
public class Cliente extends Usuario {

	private Integer sexo;
	private String endereco;
	private String telefone;
	private String cpf;
	@OneToMany
	@JoinColumn(name = "clienteId", referencedColumnName = "id")
	private List<Pedido> pedidos;

	public final static int M = 1;
	public final static int F = 2;
	public final static int O = 3;

	public Cliente() {
	}

	public Cliente(int sexo, String endereco, String telefone, String cpf,
			List<Pedido> pedidos, int id, String nome, String email,
			String senha, String confirmacaoSenha) {
		super(id, nome, email, senha, confirmacaoSenha);
		this.sexo = sexo;
		this.endereco = endereco;
		this.telefone = telefone;
		this.cpf = cpf;
		this.pedidos = pedidos;
	}

	public Integer getSexo() {
		return sexo;
	}

	public void setSexo(Integer sexo) {
		this.sexo = sexo;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	public String getSexoVerbose() {
		switch (this.sexo) {
		case Cliente.M:
			return "Masculino";
		case Cliente.F:
			return "Masculino";
		default:
			return "Outro";
		}

	}

}
