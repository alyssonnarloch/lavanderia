package com.lavanderia.controller;

import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lavanderia.model.Cliente;
import com.lavanderia.model.Funcionario;
import com.lavanderia.model.Usuario;
import com.lavanderia.util.Erro;
import com.lavanderia.util.Extras;

@Controller
public class MainController {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	protected HttpSession session;

	@RequestMapping("/")
	public String main() {
		return "main";
	}

	@RequestMapping(value = "/clientelogin", method = RequestMethod.POST)
	public String clienteLogin(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "senha", required = false) String senha,
			Model model) {

		Erro erro = new Erro();

		if (email == null || email.equals("")) {
			erro.add("campo", "Informe seu e-mail.");
		}

		if (senha == null || senha.equals("")) {
			erro.add("campo", "Informe sua senha.");
		}

		if (erro.getMensagens().size() > 0) {
			model.addAttribute("erros", erro.getMensagens());
			return "main";
		}

		Session s = sessionFactory.openSession();
		Query query = s.createQuery("FROM Cliente WHERE email = '" + email + "' AND senha = '" + Extras.getMD5(senha) + "'");
		query.setMaxResults(1);

		Cliente cliente = (Cliente) query.uniqueResult();

		if (cliente == null) {
			erro.add("campo", "E-mail ou senha inválidos.");
			model.addAttribute("erros", erro.getMensagens());
			return "main";
		}

		session.setAttribute("usuario.cliente", cliente);

		return "redirect:/clientes/acompanhamento";
	}

	@RequestMapping("/clientes/logout")
	public String clienteLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@RequestMapping(value = "/funcionariologin", method = RequestMethod.POST)
	public String funcionarioLogin(
			@RequestParam(value = "matricula", required = false) String matricula,
			@RequestParam(value = "senha", required = false) String senha,
			Model model) {

		Erro erro = new Erro();

		if (matricula == null || matricula.equals("")) {
			erro.add("matricula", "Informe sua matrícula.");
		}

		if (senha == null || senha.equals("")) {
			erro.add("senha", "Informe sua senha.");
		}

		model.addAttribute("matricula", matricula);
		
		if(erro.getMensagens().size() > 0){
			model.addAttribute("erros", erro.getMensagens());
			return "funcionario.login";
		}
		
		Session s = sessionFactory.openSession();
		Query query = s.createQuery("FROM Funcionario WHERE matricula = '" + matricula + "' AND senha = '" + Extras.getMD5(senha) + "'");
		query.setMaxResults(1);

		Funcionario funcionario = (Funcionario) query.uniqueResult();

		if (funcionario == null) {
			erro.add("ambos", "Matrícula ou senha inválidos.");
			model.addAttribute("erros", erro.getMensagens());
			return "funcionario.login";
		} else if (funcionario.getSituacao() != Usuario.ATIVOS) {
			erro.add("ambos", "Funcionário inativo.");
			model.addAttribute("erros", erro.getMensagens());
			return "funcionario.login";
		}

		session.setAttribute("usuario.funcionario", funcionario);

		return "redirect:/funcionarios/acompanhamento";
	}
	
	@RequestMapping("/funcionarios/logout")
	public String funcionarioLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}
