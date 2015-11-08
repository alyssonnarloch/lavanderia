package com.lavanderia.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tipos_roupa")
public class TipoRoupa implements Serializable {

	@Id
	@GeneratedValue
	private int id;

	private String nome;
	
	private Double preco;

	private Integer diasLavagem;

	private Integer situacao;

	@OneToMany
	@JoinColumn(name = "tipoRoupaId", referencedColumnName = "id")
	private List<ItemPedido> itensPedido;

	public final static int TODOS = 2;
	public final static int ATIVOS = 1;
	public final static int INATIVOS = 0;

	public TipoRoupa() {
	}

	public TipoRoupa(int id, String nome, Double preco, Integer diasLavagem,
			Integer situacao, List<ItemPedido> itensPedido) {
		this.id = id;
		this.nome = nome;
		this.preco = preco;
		this.diasLavagem = diasLavagem;
		this.situacao = situacao;
		this.itensPedido = itensPedido;
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

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public Integer getSituacao() {
		return situacao;
	}

	public void setSituacao(Integer situacao) {
		this.situacao = situacao;
	}

	public Integer getDiasLavagem() {
		return diasLavagem;
	}

	public void setDiasLavagem(Integer diasLavagem) {
		this.diasLavagem = diasLavagem;
	}

	public List<ItemPedido> getItensPedido() {
		return itensPedido;
	}

	public void setItensPedido(List<ItemPedido> itensPedido) {
		this.itensPedido = itensPedido;
	}

	public String getPrecoVerbose() {
		NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance();
		return " " + formatoMoeda.format(this.preco);
	}

	public String getSituacaoVerbose() {
		if (this.situacao == ATIVOS) {
			return "Ativo";
		} else {
			return "Inativo";
		}
	}

	public String getDiasLavagemVerbose() {
		if (this.diasLavagem > 1) {
			return this.diasLavagem + " dias";
		} else {
			return this.diasLavagem + " dia";
		}
	}
}
