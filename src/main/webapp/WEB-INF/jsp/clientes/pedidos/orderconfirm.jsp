<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="pt-BR" scope="session" />

<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
	<h1>Confirmação do Pedido</h1>
	
	<div style="height: 35px;"></div>	

	<table class="table table-striped">
		<thead>
			<tr>
				<th>Roupa</th>
				<th>Quantidade</th>
				<th>Preço Unitário</th>
				<th>Preço Total</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${pedido.itensPedido}" var="itemPedido">
				<tr>
					<td>${itemPedido.tipoRoupa.nome}</td>
					<td>${itemPedido.quantidade}</td>
					<td>${itemPedido.precoCorrenteVerbose}</td>
					<td><fmt:formatNumber value="${itemPedido.precoCorrente * itemPedido.quantidade}" minFractionDigits="2" currencySymbol="R$ " type="currency"/></td>
				</tr>	
			</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<td></td>
				<td></td>
				<td><b>Valor Total</b></td>
				<td><b>${pedido.precoTotalVerbose}</b></td>
			</tr>
			<tr>
				<td></td>
				<td></td>
				<td></td>
				<td><b>Entrega prevista para o dia <fmt:formatDate pattern="dd/MM/yyyy" value="${pedido.previsaoEntrega}" />.</b></td>
			</tr>
		</tfoot>
	</table>

	<div>
		<a href="/clientes/novopedido" class="btn btn-warning">Editar Pedido</a>
		<a href="/clientes/efetuapedido"  class="btn btn-success">Confirmar Pedido</a>
	</div>
</div>