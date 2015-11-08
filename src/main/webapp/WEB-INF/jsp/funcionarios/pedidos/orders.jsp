<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="pt-BR" scope="session" />

<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
	<h1>Acompanhamento de pedidos</h1>

	<div style="height: 20px;"></div>

	<c:if test="${param['erro'] != null && param['erro'] != ''}">
		<div class="alert alert-danger" role="alert">
		  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
		  <span class="sr-only">Erro ao atualizar pedido:</span>
		  <c:out value="${param['erro']}" escapeXml="false"></c:out>
		</div>
	</c:if>

	<form class="form-inline" action="/funcionarios/acompanhamento" method="GET">
		<div class="form-group">
			<label for="pesquisa">Nº do pedido</label> 
			<input type="text" name="numeropedido" value="${(numeroPedido != '0') ? numeroPedido : ''}" class="form-control" maxlength="20" style="width: 100px;">
		</div>
		<div class="form-group">
			<label for="situacao">Situação</label> <select id="situacao" name="situacao" class="form-control">
				<c:forEach items="${tiposSituacaoPedido}" var="tipoSituacao" varStatus="status">
					<option value="${tipoSituacao.key}" ${(situacaoSelecionada == tipoSituacao.key) ? 'selected' : ''}>${tipoSituacao.value}</option>
				</c:forEach>
			</select>
		</div>
		<div class="form-group">
			<label for="pesquisa">Data Inicial</label> 
			<input type="text" id="data_inicial" name="datainicial" value="${dataInicial}" class="form-control" style="width: 98px;">
		</div>
		<div class="form-group">
			<label for="pesquisa">Data Final</label> 
			<input type="text" id="data_final" name="datafinal" value="${dataFinal}" class="form-control" style="width: 98px;">
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
			    	<div style="padding: 8px;">
			    		<b>Efetuado por:</b> ${pedido.cliente.nome} (${pedido.cliente.cpf}) - ${pedido.cliente.telefone}
			    	</div>
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
									<td><b>Efetuar entrega no endereço <i>"${pedido.cliente.endereco}"</i> até o dia <fmt:formatDate pattern="dd/MM/yyyy" value="${pedido.previsaoEntrega}" />.</b></td>
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
	    	        	<c:choose>
		    	        	<c:when test="${pedido.tipoSituacaoId == situacaoPedidoAberto}">
				            	<span style="width: 120px;" class="btn btn-warning btn-lancar_coleta" data-id="${pedido.id}">Lançar<br />Coleta</span>&nbsp;&nbsp;&nbsp;
				            </c:when>
				            <c:when test="${pedido.coletadoEm != null}">
				            	<span style="width: 120px;" class="btn btn-success">Coletado<br /><fmt:formatDate pattern="dd/MM/yyyy" value="${pedido.coletadoEm}" /></span>&nbsp;&nbsp;&nbsp;
				            </c:when>
			            </c:choose>
			            <c:choose>
		    	        	<c:when test="${pedido.tipoSituacaoId == situacaoPedidoColetado}">
				            	<span style="width: 120px;" class="btn btn-warning btn-lancar_lavagem" data-id="${pedido.id}">Lançar<br />Lavagem</span>&nbsp;&nbsp;&nbsp;
				            </c:when>
        		            <c:when test="${pedido.lavadoEm == null}">
				            	<span style="width: 120px;" class="btn btn-default">Lançar<br />Lavagem</span>&nbsp;&nbsp;&nbsp;
				            </c:when>
				            <c:when test="${pedido.lavadoEm != null}">
				            	<span style="width: 120px;" class="btn btn-success">Lavado<br /><fmt:formatDate pattern="dd/MM/yyyy" value="${pedido.lavadoEm}" /></span>&nbsp;&nbsp;&nbsp;
				            </c:when>
			            </c:choose>
			            <c:choose>
		    	        	<c:when test="${pedido.tipoSituacaoId == situacaoPedidoLavado}">
				            	<span style="width: 120px;" class="btn btn-warning btn-lancar_entrega" data-id="${pedido.id}">Lançar<br />Entrega</span>&nbsp;&nbsp;&nbsp;
				            </c:when>
        		            <c:when test="${pedido.entregueEm == null}">
				            	<span style="width: 120px;" class="btn btn-default">Lançar<br />Entrega</span>&nbsp;&nbsp;&nbsp;
				            </c:when>
				            <c:when test="${pedido.entregueEm != null}">
				            	<span style="width: 120px;" class="btn btn-success">Entregue<br /><fmt:formatDate pattern="dd/MM/yyyy" value="${pedido.entregueEm}" /></span>&nbsp;&nbsp;&nbsp;
				            </c:when>
			            </c:choose>
		        	</div>
	        	</c:if>
	        	
        		<!-- Modal -->
				<div class="modal fade" id="modal_${pedido.id}" tabindex="-1" role="dialog"
					aria-labelledby="myModalLabel">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<h4 class="modal-title" id="modal_title_${pedido.id}"><span id="titulo_modal_${pedido.id}"></span></h4>
							</div>
							<div class="modal-body">
						    	<div style="padding: 8px;">
						    		<b>Efetuado por:</b> ${pedido.cliente.nome} (${pedido.cliente.cpf}) - ${pedido.cliente.telefone}<br />
						    		<b>Endereço:</b> ${pedido.cliente.endereco}
						    	</div>							
								<table class="table table-striped">
									<thead>
										<tr>
											<th>Roupa</th>
											<th>Quantidade</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${pedido.itensPedido}" var="itemPedido">
											<tr>
												<td>${itemPedido.tipoRoupa.nome}</td>
												<td>${itemPedido.quantidade}</td>
											</tr>	
										</c:forEach>
									</tbody>
									<tfoot>
									</tfoot>
								</table>
								<form id="form_mudanca_situacao_${pedido.id}" class="form-inline" action="/funcionarios/acompanhamento/atualizasituacao" method="GET">
									<div class="form-group">
										<label for="data"><span id="label_data_${pedido.id}"></span></label> 
										<input type="hidden" name="id" value="${pedido.id}" />
										<input type="text" name="data_mudanca_situacao" class="form-control data_mudanca_situacao" style="width: 100px;" />
										<c:choose>
											<c:when test="${pedido.tipoSituacaoId == situacaoPedidoAberto}">
												<input type="hidden" name="nova_situacao" value="${situacaoPedidoColetado}" />		
											</c:when>
											<c:when test="${pedido.tipoSituacaoId == situacaoPedidoColetado}">
												<input type="hidden" name="nova_situacao" value="${situacaoPedidoLavado}" />		
											</c:when>
											<c:when test="${pedido.tipoSituacaoId == situacaoPedidoLavado}">
												<input type="hidden" name="nova_situacao" value="${situacaoPedidoEntregue}" />		
											</c:when>											
										</c:choose>
									</div>
									<div class="form-group">
										<button type="submit" class="btn btn-primary">Confirmar</button>
										<button type="button" class="btn btn-danger" data-dismiss="modal">Cancelar</button>
									</div>
								</form>
							</div>
							<div class="modal-footer">
							</div>
						</div>
					</div>
				</div>
	        	
			  </div>			  
			</div>
		</c:forEach>
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
		$(".data_mudanca_situacao").datepicker({
			format: "dd/mm/yyyy"
		});
		$(".data_mudanca_situacao").mask("99/99/9999");
		
		$(".btn-lancar_coleta").on("click", function(){
			var id = $(this).data("id");
			$("#titulo_modal_" + id).html("Confirmação de realização de coleta");
			$("#label_data_" + id).html("Data da coleta");
			$("#modal_" + id).modal();
			
			$("#form_mudanca_situacao_" + id).validate({
			    rules: {
			        data_mudanca_situacao: {
			            required: true,
			            dateBR: true
			        }
			    }
			});
		});
		
		$(".btn-lancar_lavagem").on("click", function(){
			var id = $(this).data("id");
			$("#titulo_modal_" + id).html("Confirmação de realização de lavagem");
			$("#label_data_" + id).html("Data da lavagem");
			$("#modal_" + id).modal();
			
			$("#form_mudanca_situacao_" + id).validate({
			    rules: {
			        data_mudanca_situacao: {
			            required: true,
			            dateBR: true
			        }
			    }
			});
		});
		
		$(".btn-lancar_entrega").on("click", function(){
			var id = $(this).data("id");
			$("#titulo_modal_" + id).html("Confirmação de realização de entrega");
			$("#label_data_" + id).html("Data da entrega");
			$("#modal_" + id).modal();
			
			$("#form_mudanca_situacao_" + id).validate({
			    rules: {
			        data_mudanca_situacao: {
			            required: true,
			            dateBR: true
			        }
			    }
			});
		});			
	});
</script>