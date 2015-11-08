package com.lavanderia.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRLoader;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lavanderia.util.Extras;

@Controller
public class ReportController {

	@RequestMapping("/funcionarios/relatorios")
	public String reports(){		
		return "funcionario.reports";
	}
	
	
	@RequestMapping(value = "/funcionarios/geradorpdf", method = RequestMethod.GET)
	@ResponseBody
	public void reportGenerator(@RequestParam(value = "datainicial", required = false) String dataInicial,
						@RequestParam(value = "datafinal", required = false) String dataFinal,
						@RequestParam(value = "relatorio_id", required = false) String relatorioId,
						HttpServletResponse response, HttpServletRequest request) throws JRException, IOException, SQLException {

        Connection conn = null;
        String nomeRelatorio = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");  
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lavanderia_online", "root", "");  
        } catch (Exception e) {
            System.out.println("Erro ao obter conexao via DriverManager: " + e.getMessage());  
        }  
          
        ServletContext context = request.getServletContext();  
        byte[] bytes = null;
        
        String condicaoReceita = "";
        String tituloReceita = "Receita";
        String condicaoPedidos = "";
        String tituloPedidos = "Pedidos";
        
        if(dataInicial != null && !dataInicial.isEmpty() && dataFinal != null && !dataFinal.isEmpty()) {
        	condicaoReceita = " AND (P.efetuado_em >= '" + Extras.brDateToUs(dataInicial) + " 00:00:00' AND p.efetuado_em <= '" + Extras.brDateToUs(dataFinal) + " 23:59:59') ";
        	tituloReceita += " - " + dataInicial + " a " + dataFinal;
        	
        	condicaoPedidos = " WHERE (P.efetuado_em >= '" + Extras.brDateToUs(dataInicial) + " 00:00:00' AND p.efetuado_em <= '" + Extras.brDateToUs(dataFinal) + " 23:59:59') ";
        	tituloPedidos += " - " + dataInicial + " a " + dataFinal;
        }
        
        try {
        	Map parametros = new HashMap();
        	
        	switch (relatorioId) {
        		case "1": nomeRelatorio = "receita.jasper"; 
        				  parametros.put("titulo", tituloReceita);
        			      parametros.put("condicao", condicaoReceita);
        			      parametros.put("data_inicial_br", dataInicial);
        			      parametros.put("data_final_br", dataFinal);
        			      break;
        		case "2": nomeRelatorio = "pedidos.jasper";
        				  parametros.put("titulo", tituloPedidos);
        				  parametros.put("condicao", condicaoPedidos);
					      parametros.put("data_inicial_br", dataInicial);
        			      parametros.put("data_final_br", dataFinal);
					      break;
        		case "3": nomeRelatorio = "clientes.jasper"; break;
        		default : nomeRelatorio = "clientes_top.jasper";
        			
        	}
        	
            JasperReport relatorioJasper = (JasperReport) JRLoader.loadObjectFromFile(context.getRealPath("/WEB-INF/reports/" + nomeRelatorio));

            bytes = JasperRunManager.runReportToPdf(relatorioJasper, parametros, conn);  
        } catch (JRException e) {  
            e.printStackTrace();  
        }
        
        if (bytes != null && bytes.length > 0) {  
            response.setContentType("application/pdf");  
             
            response.setContentLength(bytes.length);
            ServletOutputStream ouputStream = response.getOutputStream();
            ouputStream.write(bytes, 0, bytes.length);
            ouputStream.flush();
            ouputStream.close();
            
            return;
        }  
	}

}
