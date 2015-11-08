package com.lavanderia.model;

import java.util.HashMap;
import java.util.Map;

public class TipoSituacao {

	public static final int ABERTO = 1;
	public static final int COLETADO = 2;
	public static final int LAVADO = 3;
	public static final int ENTREGUE = 4;
	public static final int CANCELADO = 6;
	public static final int TODAS = 7;

	public static Map getTiposSituacao() {
		Map tiposSituacaoPedido = new HashMap();

		tiposSituacaoPedido.put(TODAS, getNomeSituacao(TODAS));
		tiposSituacaoPedido.put(ABERTO, getNomeSituacao(ABERTO));
		tiposSituacaoPedido.put(COLETADO, getNomeSituacao(COLETADO));
		tiposSituacaoPedido.put(LAVADO, getNomeSituacao(LAVADO));
		tiposSituacaoPedido.put(ENTREGUE, getNomeSituacao(ENTREGUE));
		tiposSituacaoPedido.put(CANCELADO, getNomeSituacao(CANCELADO));
		
		return tiposSituacaoPedido;
	}

	public static String getNomeSituacao(int tipoSituacaoId) {
		switch (tipoSituacaoId) {
			case TipoSituacao.TODAS:
				return "Todos";
			case TipoSituacao.ABERTO:
				return "Aguardando Retirada";
			case TipoSituacao.COLETADO:
				return "Aguardando Lavagem";
			case TipoSituacao.LAVADO:
				return "Aguardando Entrega";
			case TipoSituacao.ENTREGUE:
				return "Entregue";
			default:
				return "Cancelado";
		}
	}
}
