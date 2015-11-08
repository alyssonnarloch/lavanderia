package com.lavanderia.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.base.Joiner;
import com.lavanderia.model.Cliente;
import com.lavanderia.model.ItemPedido;
import com.lavanderia.model.Pedido;
import com.lavanderia.model.TipoRoupa;
import com.lavanderia.model.TipoSituacao;
import com.lavanderia.util.Extras;

@Controller
public class PedidosController{

	@Autowired
	private SessionFactory sessionFactory;
	
	@RequestMapping("/clientes/novopedido")
	public String neworder(Model model, HttpSession session) {
		Session sessionDb = sessionFactory.openSession();		
		Pedido pedido;

		if(session.getAttribute("pedido") != null){
			pedido = (Pedido) session.getAttribute("pedido");
		} else {
			pedido = new Pedido();
			List<ItemPedido> itensPedido = new ArrayList<ItemPedido>();
			itensPedido.add(new ItemPedido());
			pedido.setItensPedido(itensPedido);
		}
		
		Query query = sessionDb.createQuery("FROM TipoRoupa WHERE situacao = :situacao ORDER BY nome");
		query.setInteger("situacao", TipoRoupa.ATIVOS);

		model.addAttribute("tiposRoupa", query.list());
		model.addAttribute("pedido", pedido);
		
		sessionDb.close();
		
		return "cliente.neworder";
	}
	
	@RequestMapping("/clientes/confirmacaopedido")
	public String orderconfirm(Model model, HttpServletRequest request){
		Session sessionDb = sessionFactory.openSession();
		HttpSession session = request.getSession();
		
		Map<String, String[]> parameters = request.getParameterMap();
		String[] paramsTipoRoupaIds = parameters.get("itempedido.tipoRoupaId");
		String[] paramsQuantidade = parameters.get("itempedido.quantidade");
		        
		Pedido pedido = new Pedido();
		List<ItemPedido> itensPedido = new ArrayList<ItemPedido>();
		double precoTotal = 0.0;
		int diasEntrega = 0;
				
		List<String> errosCampoTipoRoupa = new ArrayList<String>();
		List<String> errosCampoQuantidade = new ArrayList<String>();
		
		boolean temErro = false;
		
		for(int i = 0; i < paramsTipoRoupaIds.length; i++){
			int tipoRoupaId = 0;
			int quantidade = 0;
			
			boolean erroTipoRoupa = false;
			boolean erroQuantidade = false;
			
			try{
				tipoRoupaId = Integer.parseInt(paramsTipoRoupaIds[i]);
			}catch(Exception ex){
				tipoRoupaId = 0;
				erroTipoRoupa = true;
				temErro = true;
			}finally{
				if (erroTipoRoupa){
					errosCampoTipoRoupa.add("<span class='error'>Selecione uma roupa.</span>");
				} else {
					errosCampoTipoRoupa.add("");
				}
			}
			
			try{
				quantidade = Integer.parseInt(paramsQuantidade[i]);
			}catch(Exception ex){
				quantidade = 0;
				erroQuantidade = true;
				temErro = true;
			}finally{
				if (erroQuantidade || quantidade == 0) {
					temErro = true;
					errosCampoQuantidade.add("<span class='error'>Informe uma quantidade.</span>");
				} else {
					errosCampoQuantidade.add("");
				}
			}
			
			TipoRoupa tipoRoupa = (TipoRoupa) sessionDb.get(TipoRoupa.class, tipoRoupaId);
			ItemPedido itemPedido = new ItemPedido();
			itemPedido.setPedido(pedido);
			if (tipoRoupa != null) {
				itemPedido.setTipoRoupa(tipoRoupa);
				itemPedido.setPrecoCorrente(tipoRoupa.getPreco());
				precoTotal += (tipoRoupa.getPreco() * quantidade);
				if(tipoRoupa.getDiasLavagem() > diasEntrega){
					diasEntrega = tipoRoupa.getDiasLavagem();
				}
			}
			
			itemPedido.setQuantidade(quantidade);									
			itensPedido.add(itemPedido);						
		}
		
		pedido.setItensPedido(itensPedido);
		pedido.setPrecoTotal(precoTotal);
		pedido.setDiasEntrega(diasEntrega);
		pedido.setPrevisaoEntrega(Extras.previsaoEntrega(new Date(), diasEntrega));
		
		model.addAttribute("pedido", pedido);
		session.setAttribute("pedido", pedido);
		
		if(temErro){
			Query query = sessionDb.createQuery("FROM TipoRoupa WHERE situacao = :situacao ORDER BY nome");
			query.setInteger("situacao", TipoRoupa.ATIVOS);
			
			model.addAttribute("tiposRoupa", query.list());
			model.addAttribute("errosCampoTipoRoupa", errosCampoTipoRoupa);
			model.addAttribute("errosCampoQuantidade", errosCampoQuantidade);
			
			return "cliente.neworder";
		}		
		
		sessionDb.close();
		
		return "cliente.orderconfirm";
	}
	
	@RequestMapping(value = "/clientes/cancelapedido", method = RequestMethod.GET)
	public String cancelOrder(HttpSession session, @RequestParam(value = "id", required = false) String pedidoId){
		Session sessionDb = sessionFactory.openSession();
		Transaction t = sessionDb.beginTransaction();
		
		if(pedidoId == null || pedidoId.isEmpty()){
			session.removeAttribute("pedido");		
			return "redirect:/clientes/novopedido";
		}else{
			try{
				int id = Integer.parseInt(pedidoId);
				
				Pedido pedido = (Pedido) sessionDb.get(Pedido.class, id);
				pedido.setCanceladoEm(new Date());
				pedido.setTipoSituacaoId(TipoSituacao.CANCELADO);
			
				sessionDb.update(pedido);
				t.commit();
				
				return "redirect:/clientes/acompanhamento";
			}catch(Exception ex){
				t.rollback();
				return "orders";
			}
		}
	}
	
	@RequestMapping("/clientes/efetuapedido")
	public String createOrder(Model model, HttpSession session){
		Session sessionDb = sessionFactory.openSession();	
		Transaction t = sessionDb.beginTransaction();
		
		Pedido pedido = null;
		Cliente cliente = null;
		
		int numeroPedido = 0;
		int numeroPedidosIguais = 0;
		
		Query query = null;
		
		if(session.getAttribute("pedido") != null){
			pedido = (Pedido) session.getAttribute("pedido");
		}else{
			return "redirect:/clientes/novopedido";
		}
		
		if(session.getAttribute("usuario.cliente") != null){
			cliente = (Cliente) session.getAttribute("usuario.cliente");
		}else{
			return "redirect:/clientes/novopedido";
		}
		
		//Gera número do pedido
		do{
			Random rand = new Random();
			
			numeroPedido = rand.nextInt(1000);
			query = sessionDb.createQuery("SELECT COUNT(id) FROM Pedido WHERE numeroPedido = " + numeroPedido);
	
			List listResult = (List) query.list();
			Number count = (Number) listResult.get(0);
			numeroPedidosIguais = count.intValue();
		} while(numeroPedidosIguais > 0);
		
		pedido.setNumeroPedido(numeroPedido);
		pedido.setEfetuadoEm(new Date());
		pedido.setPrevisaoEntrega(Extras.previsaoEntrega(pedido.getEfetuadoEm(), pedido.getDiasEntrega()));
		pedido.setCliente(cliente);
		pedido.setTipoSituacaoId(TipoSituacao.ABERTO);
		
		try {
			sessionDb.save(pedido);
			t.commit();
		} catch (Exception ex) {
			t.rollback();
			System.out.println(ex.getMessage());
			ex.getStackTrace();
			throw new RuntimeException("Erro ao criar Pedido: " + ex.getMessage());
		}
		
		sessionDb.close();
				
		session.removeAttribute("pedido");
		
		return "redirect:/clientes/pedidoconcluido?id=" + pedido.getId();
	}
	
	@RequestMapping("/clientes/pedidoconcluido")
	public String doneOrder(@RequestParam("id") int pedidoId, Model model){
		Session sessionDb = sessionFactory.openSession();
		Pedido pedido = (Pedido) sessionDb.get(Pedido.class, pedidoId);
		
		model.addAttribute("pedido", pedido);
		
		return "cliente.done";
	}
	
	@RequestMapping("/clientes/acompanhamento")
	public String clienteorders(Model model, 
			HttpSession session,
			@RequestParam(value = "numeropedido", required = false) String numeroPedido,
			@RequestParam(value = "situacao", required = false) String situacao){
		
		Session sessionDb = sessionFactory.openSession();
		
		Cliente cliente = (Cliente) session.getAttribute("usuario.cliente");
		
		int situacaoParametro;
		int numeroPedidoParametro;
		String sql = "FROM Pedido WHERE cliente_id = :clienteId";

		try {
			situacaoParametro = Integer.parseInt(situacao);
		} catch (Exception ex) {
			situacaoParametro = TipoSituacao.TODAS;
		}
		
		try {
			numeroPedidoParametro = Integer.parseInt(numeroPedido);
		} catch (Exception ex) {
			numeroPedidoParametro = 0;
		}

		if(numeroPedidoParametro > 0) {
			sql += " AND numero_pedido = :numeroPedido";
		} else if (situacaoParametro != TipoSituacao.TODAS) {
			sql += " AND tipo_situacao_id = :situacao";
		}

		sql += " ORDER BY efetuado_em DESC";

		Query query = sessionDb.createQuery(sql);
		
		if(numeroPedidoParametro > 0) {
			query.setInteger("numeroPedido", numeroPedidoParametro);
		} else if (situacaoParametro != TipoSituacao.TODAS) {
			query.setInteger("situacao", situacaoParametro);
		}
		
		query.setInteger("clienteId", cliente.getId());
		
		model.addAttribute("pedidos", query.list());
		model.addAttribute("situacaoPedidoAberto", TipoSituacao.ABERTO);
		model.addAttribute("situacaoPedidoColetado", TipoSituacao.COLETADO);
		model.addAttribute("situacaoPedidoEntregue", TipoSituacao.ENTREGUE);
		model.addAttribute("situacaoPedidoLavado", TipoSituacao.LAVADO);
		model.addAttribute("situacaoPedidoCancelado", TipoSituacao.CANCELADO);
		model.addAttribute("tiposSituacaoPedido", TipoSituacao.getTiposSituacao());
		model.addAttribute("numeroPedido", numeroPedidoParametro);
		model.addAttribute("situacaoSelecionada", situacaoParametro);
		
		return "cliente.orders";
	}
	
	@RequestMapping("/funcionarios/acompanhamento")
	public String funcionarioorders(Model model, 
			HttpSession session,
			@RequestParam(value = "numeropedido", required = false) String numeroPedido,
			@RequestParam(value = "situacao", required = false) String situacao,
			@RequestParam(value = "datainicial", required = false) String dataInicial,
			@RequestParam(value = "datafinal", required = false) String dataFinal){
		
		Session sessionDb = sessionFactory.openSession();
		
		int situacaoParametro;
		String sql = "FROM Pedido";
		
		List<String> sqls = new ArrayList<String>();
		
		try {
			situacaoParametro = Integer.parseInt(situacao);
		} catch (Exception ex) {
			situacaoParametro = TipoSituacao.ABERTO;
		}
		
		if (numeroPedido != null && !numeroPedido.equals("")){
			sqls.add("numero_pedido = :numeroPedido");
		} else {
			if(situacaoParametro != TipoSituacao.TODAS){
				sqls.add("tipo_situacao_id = :situacao");
			}
			if((dataInicial != null && !dataInicial.equals("")) && (dataFinal != null && !dataFinal.equals(""))){
				sqls.add("(DATE(efetuado_em) >= :dataInicial AND DATE(efetuado_em) <= :dataFinal)");
			} else if ((dataInicial != null && !dataInicial.equals("")) && (dataFinal == null || dataFinal.equals(""))){
				sqls.add("DATE(efetuado_em) = :dataInicial");
			} else if ((dataInicial == null || dataInicial.equals("")) && (dataFinal != null && !dataFinal.equals(""))){
				sqls.add("DATE(efetuado_em) = :dataFinal");
			}
		}

		if(sqls.size() > 0) {
			sql += " WHERE " + Joiner.on(" AND ").join(sqls);
		}

		sql += " ORDER BY efetuado_em DESC";

		Query query = sessionDb.createQuery(sql);
		
		if (numeroPedido != null && !numeroPedido.equals("")){
			query.setString("numeroPedido", numeroPedido);
		} else {
			if(situacaoParametro != TipoSituacao.TODAS){
				query.setInteger("situacao", situacaoParametro);
			}
			if((dataInicial != null && !dataInicial.equals("")) && (dataFinal != null && !dataFinal.equals(""))){
				query.setString("dataInicial", Extras.brDateToUs(dataInicial));
				query.setString("dataFinal", Extras.brDateToUs(dataFinal));
			} else if ((dataInicial != null && !dataInicial.equals("")) && (dataFinal == null || dataFinal.equals(""))){
				query.setString("dataInicial", Extras.brDateToUs(dataInicial));
			} else if ((dataInicial == null || dataInicial.equals("")) && (dataFinal != null && !dataFinal.equals(""))){
				query.setString("dataFinal", Extras.brDateToUs(dataFinal));
			}
		}
		
		model.addAttribute("pedidos", query.list());
		model.addAttribute("situacaoPedidoAberto", TipoSituacao.ABERTO);
		model.addAttribute("situacaoPedidoColetado", TipoSituacao.COLETADO);
		model.addAttribute("situacaoPedidoEntregue", TipoSituacao.ENTREGUE);
		model.addAttribute("situacaoPedidoLavado", TipoSituacao.LAVADO);
		model.addAttribute("situacaoPedidoCancelado", TipoSituacao.CANCELADO);
		model.addAttribute("tiposSituacaoPedido", TipoSituacao.getTiposSituacao());
		model.addAttribute("numeroPedido", numeroPedido);
		model.addAttribute("situacaoSelecionada", situacaoParametro);
		model.addAttribute("dataInicial", dataInicial);
		model.addAttribute("dataFinal", dataFinal);
		
		return "funcionario.orders";
	}
	
	@RequestMapping(value = "/funcionarios/acompanhamento/atualizasituacao", method = RequestMethod.GET)
	public String updatestatusorder(Model model, 
			@RequestParam(value = "id", required = true) String idParam,
			@RequestParam(value = "nova_situacao", required = true) String novaSituacaoIdParam,
			@RequestParam(value = "data_mudanca_situacao", required = true) String dataNovaSituacaoParam){
		
		Session sessionDb = sessionFactory.openSession();
		Transaction t = sessionDb.beginTransaction();
		
		String mensagemErro = "";
		
		int id = 0;
		int novaSituacaoId = 0;
		Date dataNovaSituacao = null;
		
		try {
			id = Integer.parseInt(idParam);
		} catch (Exception ex) {
			mensagemErro += "Id do pedido incorreto. / ";
		}
		
		try {
			novaSituacaoId = Integer.parseInt(novaSituacaoIdParam);
		} catch (Exception ex) {
			mensagemErro += "Status incorreto. / ";
		}
		
		try {
			dataNovaSituacao = Extras.stringBrDateToDate(dataNovaSituacaoParam);
			dataNovaSituacao.getTime();
		} catch (Exception ex) {
			mensagemErro += "Data incorreta.";
		}
		
		if (!mensagemErro.isEmpty()) {
			return "redirect:/funcionarios/acompanhamento?erro=" + mensagemErro;
		}
		
		Pedido pedido = (Pedido) sessionDb.get(Pedido.class, id);		
		
		pedido.setTipoSituacaoId(novaSituacaoId);
		
		if (novaSituacaoId == TipoSituacao.COLETADO) {
			pedido.setColetadoEm(dataNovaSituacao);
		} else if (novaSituacaoId == TipoSituacao.LAVADO) {
			pedido.setLavadoEm(dataNovaSituacao);
		} else if (novaSituacaoId == TipoSituacao.ENTREGUE) {
			pedido.setEntregueEm(dataNovaSituacao);
		}
		
		try {
			sessionDb.update(pedido);
			t.commit();
		} catch (Exception ex) {
			t.rollback();
			System.out.println("Erro ao atualizar a situação do pedido: " + ex.getMessage());
		}
		
		sessionDb.close();
		
		return "redirect:/funcionarios/acompanhamento";
	}
}
