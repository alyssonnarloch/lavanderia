package com.lavanderia.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lavanderia.model.TipoRoupa;
import com.lavanderia.model.validator.TipoRoupaValidator;

@Controller
public class TiposRoupaController {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private TipoRoupaValidator validator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}

	@RequestMapping(value = "/funcionarios/tiposroupa/index", method = RequestMethod.GET)
	public String index(
			@RequestParam(value = "pesquisa", required = false) String pesquisa,
			@RequestParam(value = "situacao", required = false) String situacao,
			Model model) {

		Session session = sessionFactory.openSession();

		int situacaoParametro;
		String sql = "FROM TipoRoupa WHERE nome LIKE :pesquisa";

		try {
			situacaoParametro = Integer.parseInt(situacao);
		} catch (Exception ex) {
			situacaoParametro = TipoRoupa.ATIVOS;
		}

		if (situacaoParametro != TipoRoupa.TODOS) {
			sql += " AND situacao = :situacao";
		}

		sql += " ORDER BY nome";

		Query query = session.createQuery(sql);
		pesquisa = (pesquisa == null) ? "" : pesquisa;
		query.setString("pesquisa", "%" + pesquisa + "%");

		if (situacaoParametro != TipoRoupa.TODOS) {
			query.setInteger("situacao", situacaoParametro);
		}

		model.addAttribute("tiposRoupa", query.list());
		model.addAttribute("pesquisa", pesquisa);
		model.addAttribute("situacao", situacaoParametro);

		session.close();

		return "funcionario.tiposroupa.index";
	}

	@RequestMapping(value = "/funcionarios/tiposroupa/mudasituacao", method = RequestMethod.GET)
	public String changeStatus(
			@RequestParam(value = "id", required = true) int id) {
		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();

		TipoRoupa tipoRoupa = (TipoRoupa) session.get(TipoRoupa.class, id);

		if (tipoRoupa.getSituacao().equals(TipoRoupa.ATIVOS)) {
			tipoRoupa.setSituacao(TipoRoupa.INATIVOS);
		} else {
			tipoRoupa.setSituacao(TipoRoupa.ATIVOS);
		}

		session.update(tipoRoupa);
		t.commit();
		session.close();

		return "redirect:index";
	}

	@RequestMapping("/funcionarios/tiposroupa/novo")
	public String newModel(Model model) {
		TipoRoupa tipoRoupa = new TipoRoupa();
		tipoRoupa.setSituacao(TipoRoupa.ATIVOS);
		model.addAttribute(tipoRoupa);

		return "funcionario.tiposroupa.new";
	}

	@RequestMapping(value = "/funcionarios/tiposroupa/criar", method = RequestMethod.POST)
	public String create(@Valid TipoRoupa tipoRoupa, BindingResult result,
			Model model) {

		if (result.hasErrors()) {
			return "funcionario.tiposroupa.new";
		}

		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();

		try {
			session.save(tipoRoupa);
			t.commit();
		} catch (Exception ex) {
			t.rollback();
			throw new RuntimeException("Erro ao criar TipoRoupa");
		}

		session.close();

		return "redirect:index";
	}

	@RequestMapping("/funcionarios/tiposroupa/editar")
	public String editar(@RequestParam(value = "id", required = true) int id,
			Model model) {
		Session session = sessionFactory.openSession();

		TipoRoupa tipoRoupa = (TipoRoupa) session.get(TipoRoupa.class, id);

		Map situacoes = new HashMap();
		situacoes.put(TipoRoupa.ATIVOS, "Ativo");
		situacoes.put(TipoRoupa.INATIVOS, "Inativo");

		model.addAttribute(tipoRoupa);
		model.addAttribute("situacoes", situacoes);

		return "funcionario.tiposroupa.edit";
	}

	@RequestMapping(value = "/funcionarios/tiposroupa/alterar", method = RequestMethod.POST)
	public String update(@Valid TipoRoupa tipoRoupa, BindingResult result,
			Model model) {

		if (result.hasErrors()) {
			Map situacoes = new HashMap();
			situacoes.put(TipoRoupa.ATIVOS, "Ativo");
			situacoes.put(TipoRoupa.INATIVOS, "Inativo");
		
			model.addAttribute("situacoes", situacoes);
			
			return "funcionario.tiposroupa.edit";
		}

		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();

		try {
			session.update(tipoRoupa);
			t.commit();
		} catch (Exception ex) {
			t.rollback();
			throw new RuntimeException("Erro ao atualizar TipoRoupa");
		}

		session.close();

		return "redirect:index";
	}

}
