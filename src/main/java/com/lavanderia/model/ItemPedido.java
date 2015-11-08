package com.lavanderia.model;

import java.io.Serializable;
import java.text.NumberFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "itens_pedido")
public class ItemPedido implements Serializable {

	@Id
	@GeneratedValue
	private int id;
	
	private int quantidade;
	
	private double precoCorrente;
	
	@ManyToOne
	@JoinColumn(name = "tipoRoupaId")
	private TipoRoupa tipoRoupa;
	
	@ManyToOne
	@JoinColumn(name = "pedidoId")
	private Pedido pedido;

	public ItemPedido() {
	}

	public ItemPedido(int id, int quantidade, double precoCorrente,
			TipoRoupa tipoRoupa, Pedido pedido) {
		this.id = id;
		this.quantidade = quantidade;
		this.precoCorrente = precoCorrente;
		this.tipoRoupa = tipoRoupa;
		this.pedido = pedido;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public double getPrecoCorrente() {
		return precoCorrente;
	}

	public String getPrecoCorrenteVerbose() {
		NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance();
		return " " + formatoMoeda.format(this.precoCorrente);
	}
	
	public void setPrecoCorrente(double precoCorrente) {
		this.precoCorrente = precoCorrente;
	}

	public TipoRoupa getTipoRoupa() {
		return tipoRoupa;
	}

	public void setTipoRoupa(TipoRoupa tipoRoupa) {
		this.tipoRoupa = tipoRoupa;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

}
