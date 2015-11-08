package com.lavanderia.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

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

import com.lavanderia.model.Cliente;
import com.lavanderia.model.validator.ClienteValidator;
import com.lavanderia.util.Extras;

@Controller
public class ClientesController {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private ClienteValidator validator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}

	@RequestMapping("/cliente/novo")
	public String newModel(Model model) {
		Cliente cliente = new Cliente();

		Map sexos = new HashMap();
		sexos.put("", "Selecione");
		sexos.put(Cliente.M, "Masculino");
		sexos.put(Cliente.F, "Feminino");
		sexos.put(Cliente.O, "Outro");

		model.addAttribute("sexos", sexos);
		model.addAttribute(cliente);

		return "cliente.new";
	}

	@RequestMapping(value = "/cliente/criar", method = RequestMethod.POST)
	public String create(@Valid Cliente cliente, BindingResult result,
			Model model) {

		if (result.hasErrors()) {
			Map sexos = new HashMap();
			sexos.put("", "Selecione");
			sexos.put(Cliente.M, "Masculino");
			sexos.put(Cliente.F, "Feminino");
			sexos.put(Cliente.O, "Outro");
			
			model.addAttribute("sexos", sexos);
			
			return "cliente.new";
		}

		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();

		try {
			cliente.setSituacao(Cliente.ATIVOS);
			cliente.setSenha(Extras.getMD5(cliente.getSenha()));

			session.save(cliente);
			t.commit();
		} catch (Exception ex) {
			t.rollback();
			throw new RuntimeException("Erro ao criar Cliente");
		}

		session.close();

		return "redirect:/";
	}
}
