<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
	<h1>Relatórios</h1>
	
	<div style="height: 20px;"></div>	
	<div class="row">
		<div class="col-md-3">
			<div class="panel panel-info">
				<div class="panel-heading">
					<h3 class="panel-title">Gerenciais</h3>
				</div>
				<div class="panel-body">
					<form class="form" id="form_report">
						<div class="form-group">
							<label for="pesquisa">Data Inicial</label> 
							<input type="text" id="data_inicial" name="datainicial" class="form-control" dateBR />
						</div>
						<div class="form-group">
							<label for="pesquisa">Data Final</label> 
							<input type="text" id="data_final" name="datafinal" class="form-control" dateBR />
						</div>		
						<a href="#" class="btn btn-primary btn_report_generator" data-report_id="1">Receita</a>
						<a href="#" class="btn btn-primary btn_report_generator" data-report_id="2">Pedidos</a>
					</form>
				</div>
			</div>
		</div>
		<div class="col-md-3">
			<div class="panel panel-info">
				<div class="panel-heading">
					<h3 class="panel-title">Clientes</h3>
				</div>
				<div class="panel-body">
					<a href="/funcionarios/geradorpdf?relatorio_id=3" target="_blank" class="btn btn-primary btn-lg" style="width: 170px;">Lista de Clientes</a><br /><br />
					<a href="/funcionarios/geradorpdf?relatorio_id=4" target="_blank" class="btn btn-primary btn-lg" style="width: 170px;">Top 5 - Clientes</a>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(function() {
		$("#data_inicial").mask("99/99/9999");
		$("#data_final").mask("99/99/9999");
		$("#data_inicial").datepicker({
			format: "dd/mm/yyyy"
		});
		$("#data_final").datepicker({
			format: "dd/mm/yyyy"
		});
		
		$(".btn_report_generator").on("click", function(e){
			e.preventDefault();
			
			console.log($("#data_inicial").valid());
			console.log($("#data_final").valid());
			
			var reportId = $(this).data("report_id");
			var url = "/funcionarios/geradorpdf?relatorio_id=" + reportId + "&" + $("#form_report").serialize();
			window.open(url, "_blank");
		});
	});
</script>