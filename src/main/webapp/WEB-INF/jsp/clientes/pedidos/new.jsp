<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
	<h1>Novo Pedido</h1>
	
	<div style="height: 35px;"></div>

	<form:form commandName="Pedido" method="POST" action="confirmacaopedido">
		<table class="table">
			<thead>
				<tr>
					<th>Roupa</th>
					<th>Quantidade</th>
					<th>Preço Unitário</th>
					<th>Preço Total</th>
					<th></th>
				</tr>
			</thead>
			<tbody id="body_roupas">
				<c:forEach items="${pedido.itensPedido}" var="itemPedido" varStatus="status">
					<tr class="body_row">
						<td>
							<select name="itempedido.tipoRoupaId" class="form-control select_tipos_roupa">
								<option value="">Selecione uma roupa</option>
							    <c:forEach items="${tiposRoupa}" var="tipoRoupa">
							        <option value="${tipoRoupa.id}" ${(itemPedido.tipoRoupa.id == tipoRoupa.id) ? 'selected' : ''}>${tipoRoupa.nome} | ${tipoRoupa.precoVerbose} | ${tipoRoupa.diasLavagemVerbose}</option>
							    </c:forEach>
							</select>
							<input type="hidden" class="input_preco_total" value="${itemPedido.tipoRoupa.preco * itemPedido.quantidade}" disabled />
							${errosCampoTipoRoupa[status.index]}
						</td>
						<td>
							<input name="itempedido.quantidade" maxlength="3" class="form-control input_quantidade" value="${itemPedido.quantidade}" />
							${errosCampoQuantidade[status.index]}
						</td>
						<td class="coluna_preco_unitario">${itemPedido.tipoRoupa.precoVerbose}</td>
						<td class="coluna_preco_total"><fmt:setLocale value="pt-BR" /><fmt:formatNumber value="${itemPedido.tipoRoupa.preco * itemPedido.quantidade}" minFractionDigits="2" currencySymbol="R$ " type="currency"/></td>
						<td>
							<button type="button" class="btn btn-danger btn_remover">Remover</button>
						</td>
					</tr>
				</c:forEach>										
			</tbody>
			<tfoot>
				<tr>
					<td></td>
					<td></td>
					<td><b>Valor Total</b></td>
					<td id="coluna_valor_total_total"><b>${pedido.precoTotalVerbose}</b></td>
					<td></td>
				</tr>
			</tfoot>
		</table>
		<a href="/clientes/cancelapedido" class="btn btn-danger">Cancelar Pedido</a>
		<input type="submit" value="Confirmar Pedido" class="btn btn-success" />
		<button class="btn btn-primary" style="float: right;" id="adicionar_roupa">Adicionar</button>
	</form:form>	

</div>

<div id="html_select_tipos_roupa" style="visibility: hidden">
	<select name="itempedido.tipoRoupaId" class="form-control select_tipos_roupa">
		<option value="">Selecione uma roupa</option>
	    <c:forEach items="${tiposRoupa}" var="item">
	        <option value="${item.id}">${item.nome} | ${item.precoVerbose} | ${item.diasLavagemVerbose}</option>
	    </c:forEach>
	</select>
	<input type="hidden" class="input_preco_total" disabled />
</div>

<div id="html_input_quantidade" style="visibility: hidden">
	<input name="itempedido.quantidade" maxlength="3" class="form-control input_quantidade" value="0" />
</div>

<script type="text/javascript">
	$(function() {
		$("#adicionar_roupa").on("click", function(e){
			e.preventDefault();			
			
			var newRow = "";
			
			newRow += "<tr class='body_row'>";
			
			newRow += "<td>" + $("#html_select_tipos_roupa").html() + "</td>";
			newRow += "<td>" + $("#html_input_quantidade").html() + "</td>";
			newRow += "<td class='coluna_preco_unitario'></td>";
			newRow += "<td class='coluna_preco_total'></td>";
			newRow += "<td><button type='button' class='btn btn-danger btn_remover'>Remover</button></td>";
			
			newRow += "</tr>";
			
			$("#body_roupas").append(newRow);
		});
		
		$("#body_roupas").on("click", ".btn_remover", function(){
			if($("#body_roupas tr").size() > 1){
				$(this).parent().parent().remove();
			}
			
			calcTotalValue();	
		});
		
		$("#body_roupas").on("change", ".select_tipos_roupa", function(){
			var selectedRoupa = $(this).find("option:selected").text().split("|");
			var precoRoupa = stringToFloat(selectedRoupa[1]);
			var quantidade = $(this).parent().parent().find("input.input_quantidade").val();
			var precoTotal = 0;
			
			quantidade = (quantidade == "" || quantidade == null) ? 0 : quantidade;
			precoTotal = quantidade * precoRoupa;

			$(this).parent().parent().find("td.coluna_preco_unitario").html(formatFloatToMoney(precoRoupa));
			$(this).parent().parent().find("td.coluna_preco_total").html(formatFloatToMoney(precoTotal));
			$(this).parent().parent().find("input.input_preco_total").val(precoTotal);
			
			calcTotalValue();
		});
		
		$("#body_roupas").on("keyup", ".input_quantidade", function(){
			$(this).val($(this).val().replace(/[^0-9\.]/g, ''));
			
			var selectedRoupa = $(this).parent().parent().find("option:selected").text().split("|");
			var precoRoupa = stringToFloat(selectedRoupa[1]);
			var quantidade = $(this).val();
			var precoTotal = 0;
			
			quantidade = (quantidade == "" || quantidade == null) ? 0 : quantidade;
			precoTotal = quantidade * precoRoupa;					
			
			$(this).parent().parent().find("td.coluna_preco_total").html(formatFloatToMoney(precoTotal));
			$(this).parent().parent().find("input.input_preco_total").val(precoTotal);
			
			calcTotalValue();
		});
		
		
		function stringToFloat(value){
			if(value == null || value == "")
				return "";
			return value.replace(" ", "").replace("R$", "").replace(".", "").replace(",", ".");
		}
		
		function formatFloatToMoney(value){
			if(value == null || value == "")
				return "";
			value = "" + parseFloat(value).toFixed(2);
			return completeCurrency("R$ " + value.replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,").replace(".", "@").replace(",", ".").replace("@", ","));
		}
		
		function completeCurrency(value){
			var part = value.split(",");

			if(typeof part[1] !== "undefined"){
				if(part[1].length == 1){
					return value + "0";
				}else {
					return value;
				}
			} else{
				return value + ",00";
			}
		}
		
		function calcTotalValue(){
			var total = 0;
			$(".input_preco_total").each(function(a, b){
				if($(this).val() != ''){
					total += parseFloat($(this).val()); 
				}
			});
			$("#coluna_valor_total_total").html("<b>" + formatFloatToMoney(total) + "</b>");
		}
	});
</script>