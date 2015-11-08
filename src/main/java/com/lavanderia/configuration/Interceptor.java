package com.lavanderia.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.lavanderia.model.Cliente;
import com.lavanderia.model.Funcionario;

public class Interceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory
			.getLogger(Interceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		String uri = request.getRequestURI();
		if (uri.contains("clientes")) {
			Cliente cliente = (Cliente) request.getSession().getAttribute("usuario.cliente");
			
			if(cliente == null){
				response.sendRedirect("/");
				return false;
			}
		} else if (uri.contains("funcionarios")) {
			Funcionario funcionario = (Funcionario) request.getSession().getAttribute("usuario.funcionario");
			if(funcionario == null){
				response.sendRedirect("/");
				return false;
			}
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		String uri = request.getRequestURI();

		if (uri.contains("clientes")) {
			Cliente cliente = (Cliente) request.getSession().getAttribute("usuario.cliente");
			if(cliente != null){
				modelAndView.addObject("usuario", cliente);
			}
		} else if (uri.contains("funcionarios")) {
			Funcionario funcionario = (Funcionario) request.getSession().getAttribute("usuario.funcionario");
			if(funcionario != null){
				try {
					modelAndView.addObject("usuario", funcionario);
				} catch(Exception ex) {
					//System.out.println("**********ERRO: " + ex.getMessage());
				}
			}
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
