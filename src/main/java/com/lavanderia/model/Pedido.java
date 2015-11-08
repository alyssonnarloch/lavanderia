package com.lavanderia.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "pedidos")
public class Pedido implements Serializable {

	@Id
	@GeneratedValue
	private int id;
	
	private int numeroPedido;
	
	private int tipoSituacaoId;
	
	private Date efetuadoEm;
	
	private Date coletadoEm;
	
	private Date lavadoEm;
	
	private Date entregueEm;
	
	private Date pagoEm;
	
	private Date canceladoEm;	
	
	private double precoTotal;
	
	private Date previsaoEntrega;
	
	private int diasEntrega;
	
	@ManyToOne
	@JoinColumn(name = "clienteId")
	private Cliente cliente;
	
	@OneToMany(cascade = {CascadeType.ALL})
	@JoinColumn(name = "pedidoId", referencedColumnName = "id")
	private List<ItemPedido> itensPedido;

	public Pedido() {
	}
	
	public Pedido(int id, int numeroPedido, int tipoSituacaoId,
			Date efetuadoEm, Date coletadoEm, Date lavadoEm, Date entregueEm,
			Date pagoEm, Date canceladoEm, double precoTotal, Date previsaoEntrega, int diasEntrega, Cliente cliente,
			List<ItemPedido> itensPedido) {
		this.id = id;
		this.numeroPedido = numeroPedido;
		this.tipoSituacaoId = tipoSituacaoId;
		this.efetuadoEm = efetuadoEm;
		this.coletadoEm = coletadoEm;
		this.lavadoEm = lavadoEm;
		this.entregueEm = entregueEm;
		this.pagoEm = pagoEm;
		this.canceladoEm = canceladoEm;
		this.precoTotal = precoTotal;
		this.previsaoEntrega = previsaoEntrega;
		this.diasEntrega = diasEntrega;
		this.cliente = cliente;
		this.itensPedido = itensPedido;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(int numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public int getTipoSituacaoId() {
		return tipoSituacaoId;
	}

	public void setTipoSituacaoId(int tipoSituacaoId) {
		this.tipoSituacaoId = tipoSituacaoId;
	}

	public Date getEfetuadoEm() {
		return efetuadoEm;
	}

	public void setEfetuadoEm(Date efetuadoEm) {
		this.efetuadoEm = efetuadoEm;
	}

	public Date getColetadoEm() {
		return coletadoEm;
	}

	public void setColetadoEm(Date coletadoEm) {
		this.coletadoEm = coletadoEm;
	}

	public Date getLavadoEm() {
		return lavadoEm;
	}

	public void setLavadoEm(Date lavadoEm) {
		this.lavadoEm = lavadoEm;
	}

	public Date getEntregueEm() {
		return entregueEm;
	}

	public void setEntregueEm(Date entregueEm) {
		this.entregueEm = entregueEm;
	}

	public Date getPagoEm() {
		return pagoEm;
	}

	public void setPagoEm(Date pagoEm) {
		this.pagoEm = pagoEm;
	}

	public Date getCanceladoEm() {
		return canceladoEm;
	}

	public void setCanceladoEm(Date canceladoEm) {
		this.canceladoEm = canceladoEm;
	}

	public void setPrecoTotal(double precoTotal) {
		this.precoTotal = precoTotal;
	}
	
	public double getPrecoTotal(){
		return this.precoTotal;
	}
	
	public String getPrecoTotalVerbose() {
		NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance();
		return " " + formatoMoeda.format(this.precoTotal);
	}
	
	public void setPrevisaoEntrega(Date previsaoEntrega){
		this.previsaoEntrega = previsaoEntrega;
	}
	
	public Date getPrevisaoEntrega(){
		return this.previsaoEntrega;
	}
	
	public void setDiasEntrega(int diasEntrega) {
		this.diasEntrega = diasEntrega;
	}
	
	public int getDiasEntrega(){
		return this.diasEntrega;
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<ItemPedido> getItensPedido() {
		return itensPedido;
	}

	public void setItensPedido(List<ItemPedido> itensPedido) {
		this.itensPedido = itensPedido;
	}
	
	public String getSituacaoVerbose(){
		return TipoSituacao.getNomeSituacao(this.tipoSituacaoId);
	}

}
