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

import com.lavanderia.model.Funcionario;
import com.lavanderia.model.TipoRoupa;
import com.lavanderia.model.validator.FuncionarioValidator;
import com.lavanderia.util.Extras;

@Controller
public class FuncionariosController {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private FuncionarioValidator validator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}

	@RequestMapping(value = "/funcionarios/funcionarios/index", method = RequestMethod.GET)
	public String index(
			@RequestParam(value = "pesquisa", required = false) String pesquisa,
			@RequestParam(value = "situacao", required = false) String situacao,
			Model model) {

		Session session = sessionFactory.openSession();

		int situacaoParametro;
		String sql = "FROM Funcionario WHERE (nome LIKE :pesquisa OR email LIKE :pesquisa OR matricula LIKE :pesquisa)";

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

		model.addAttribute("funcionarios", query.list());
		model.addAttribute("pesquisa", pesquisa);
		model.addAttribute("situacao", situacaoParametro);

		session.close();

		return "funcionario.funcionario.index";
	}

	@RequestMapping(value = "/funcionarios/funcionarios/mudasituacao", method = RequestMethod.GET)
	public String changeStatus(
			@RequestParam(value = "id", required = true) int id) {
		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();

		Funcionario funcionario = (Funcionario) session.get(Funcionario.class,
				id);

		if (funcionario.getSituacao().equals(TipoRoupa.ATIVOS)) {
			funcionario.setSituacao(TipoRoupa.INATIVOS);
		} else {
			funcionario.setSituacao(TipoRoupa.ATIVOS);
		}

		session.update(funcionario);
		t.commit();
		session.close();

		return "redirect:index";
	}

	@RequestMapping("/funcionarios/funcionarios/novo")
	public String newModel(Model model) {
		Funcionario funcionario = new Funcionario();
		funcionario.setSituacao(TipoRoupa.ATIVOS);
		model.addAttribute(funcionario);

		return "funcionario.funcionario.new";
	}

	@RequestMapping(value = "/funcionarios/funcionarios/criar", method = RequestMethod.POST)
	public String create(@Valid Funcionario funcionario, BindingResult result,
			Model model) {

		if (result.hasErrors()) {
			return "funcionario.funcionario.new";
		}

		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();

		try {
			funcionario.setSenha(Extras.getMD5(funcionario.getSenha()));

			session.save(funcionario);
			t.commit();
		} catch (Exception ex) {
			t.rollback();
			throw new RuntimeException("Erro ao criar Funcionario");
		}

		session.close();

		return "redirect:index";
	}

	@RequestMapping("/funcionarios/funcionarios/editar")
	public String editar(@RequestParam(value = "id", required = true) int id,
			Model model) {
		Session session = sessionFactory.openSession();

		Funcionario funcionario = (Funcionario) session.get(Funcionario.class,
				id);

		Map situacoes = new HashMap();
		situacoes.put(Funcionario.ATIVOS, "Ativo");
		situacoes.put(Funcionario.INATIVOS, "Inativo");

		model.addAttribute(funcionario);
		model.addAttribute("situacoes", situacoes);

		return "funcionario.funcionario.edit";
	}

	@RequestMapping(value = "/funcionarios/funcionarios/alterar", method = RequestMethod.POST)
	public String update(@Valid Funcionario funcionario, BindingResult result,
			Model model) {

		if (result.hasErrors()) {
			Map situacoes = new HashMap();
			situacoes.put(TipoRoupa.ATIVOS, "Ativo");
			situacoes.put(TipoRoupa.INATIVOS, "Inativo");

			model.addAttribute("situacoes", situacoes);

			return "funcionario.funcionario.edit";
		}

		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();	

		try {
			if (funcionario.getSenha() != null && !funcionario.getSenha().isEmpty()) {
				funcionario.setSenha(Extras.getMD5(funcionario.getSenha()));
				session.update(funcionario);
			}else{
				Funcionario oldFuncionario = (Funcionario) session.get(Funcionario.class, funcionario.getId());
				funcionario.setSenha(oldFuncionario.getSenha());
				session.merge(funcionario);
			}
			
			t.commit();
		} catch (Exception ex) {
			t.rollback();
			throw new RuntimeException("Erro ao atualizar Funcionario: " + ex.getMessage());
		}

		session.close();

		return "redirect:index";
	}
	
	@RequestMapping("/funcionario/entrar")
	public String loginpage(){
		return "funcionario.login";
	}
}
