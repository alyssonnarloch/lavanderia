<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="pt-BR" scope="session" />

<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
	<h1>Meus pedidos</h1>

	<div style="height: 20px;"></div>

	<form class="form-inline" action="/clientes/acompanhamento" method="GET">
		<div class="form-group">
			<label for="pesquisa">Nº do pedido</label> 
			<input type="text" name="numeropedido" value="${(numeroPedido != 0) ? numeroPedido : ''}" class="form-control">
		</div>
		<div class="form-group">
			<label for="situacao">Situação</label> <select id="situacao" name="situacao" class="form-control">
				<c:forEach items="${tiposSituacaoPedido}" var="tipoSituacao" varStatus="status">
					<option value="${tipoSituacao.key}" ${(situacaoSelecionada == tipoSituacao.key) ? 'selected' : ''}>${tipoSituacao.value}</option>
				</c:forEach>
			</select>
		</div>
		<button type="submit" class="btn btn-primary">Pesquisar</button>
		<a href="/clientes/novopedido" class="btn btn-success">Novo pedido</a>
	</form>

	<div style="height: 35px;"></div>

	<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">

		<c:forEach items="${pedidos}" var="pedido" varStatus="status">
			<div class="panel panel-default">
			  <div class="panel-heading" role="tab" id="${pedido.id}">
			    <h4 class="panel-title" style="color: #0088cc; text-decoration: none;">
			      <a style="font-size: 13px;" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse${pedido.id}">
			        <b>
			        	Pedido nº ${pedido.numeroPedido}, efetuado  
			        	<fmt:formatDate pattern="dd/MM/yyyy H:m:s" value="${pedido.efetuadoEm}" /> -
			        	${pedido.situacaoVerbose}
			        </b>
			      </a>
			    </h4>
			  </div>
			  <div id="collapse${pedido.id}" class="panel-collapse collapse ${(status.index == 0) ? 'in' : ''}" role="tabpanel" aria-labelledby="heading${pedido.id}">
			    <div class="panel-body">
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
								<c:if test="${pedido.tipoSituacaoId != situacaoPedidoCancelado}">
									<td><b>Entrega prevista para o dia <fmt:formatDate pattern="dd/MM/yyyy" value="${pedido.previsaoEntrega}" />.</b></td>
								</c:if>
								<c:if test="${pedido.tipoSituacaoId == situacaoPedidoCancelado}">
									<td><b>Pedido cancelado dia <fmt:formatDate pattern="dd/MM/yyyy - H:m:s" value="${pedido.canceladoEm}" />.</b></td>
								</c:if>
							</tr>
						</tfoot>
					</table>			      
			    </div>
			    <c:if test="${pedido.tipoSituacaoId != situacaoPedidoCancelado}">
	    	        <div style="padding: 8px;">
						<c:if test="${pedido.tipoSituacaoId == situacaoPedidoAberto}">			    		
				    		<button style="width: 120px;" type="button" class="btn btn-danger btn_cancelar" data-toggle="modal" data-target=".bs-example-modal-lg" data-id="${pedido.id}">Cancelar<br />Pedido</button>&nbsp;&nbsp;&nbsp;
				    	</c:if>	    	        	
				    	<c:choose>
			    	        	<c:when test="${pedido.tipoSituacaoId == situacaoPedidoAberto}">
					            	<span style="width: 120px;" class="btn btn-default">Aguardando<br /> Coleta</span>&nbsp;&nbsp;&nbsp;
					            </c:when>
					            <c:when test="${pedido.coletadoEm != null}">
					            	<span style="width: 120px;" class="btn btn-success">Coletado<br /><fmt:formatDate pattern="dd/MM/yyyy" value="${pedido.coletadoEm}" /></span>&nbsp;&nbsp;&nbsp;
					            </c:when>
				            </c:choose>
				            <c:choose>
	        		            <c:when test="${pedido.lavadoEm == null}">
					            	<span style="width: 120px;" class="btn btn-default">Aguardando<br />Lavagem</span>&nbsp;&nbsp;&nbsp;
					            </c:when>
					            <c:when test="${pedido.lavadoEm != null}">
					            	<span style="width: 120px;" class="btn btn-success">Lavado<br /><fmt:formatDate pattern="dd/MM/yyyy" value="${pedido.lavadoEm}" /></span>&nbsp;&nbsp;&nbsp;
					            </c:when>
				            </c:choose>
				            <c:choose>
	        		            <c:when test="${pedido.entregueEm == null}">
					            	<span style="width: 120px;" class="btn btn-default">Aguardando<br />Entrega</span>&nbsp;&nbsp;&nbsp;
					            </c:when>
					            <c:when test="${pedido.entregueEm != null}">
					            	<span style="width: 120px;" class="btn btn-success">Entregue<br /><fmt:formatDate pattern="dd/MM/yyyy" value="${pedido.entregueEm}" /></span>&nbsp;&nbsp;&nbsp;
					            </c:when>
				            </c:choose>
		        	</div>
	        	</c:if>
			  </div>			  
			</div>
		</c:forEach>
	</div>
	
	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="myModalLabel">Confirmação de cancelamento</h4>
	      </div>
	      <div class="modal-body">
	        	Tem certeza que deseja cancelar esse pedido?
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Voltar</button>
	        <a href="" id="link_cancelamento" class="btn btn-primary">Confirmar</a>
	      </div>
	    </div>
	  </div>
	</div>
	
</div>

<script type="text/javascript">
	$(function() {
		$(".btn_cancelar").on("click", function(){
			$("#link_cancelamento").attr("href", "/clientes/cancelapedido?id=" + $(this).data("id"));
			
			$('#myModal').modal();	
		});
		
	});
</script>