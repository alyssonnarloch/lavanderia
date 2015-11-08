<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
	<h1>Funcionários</h1>

	<div style="height: 35px;"></div>

	<form class="form-inline" action="index" method="GET">
		<div class="form-group">
			<label for="pesquisa">Pesquisa</label> <input type="text"
				name="pesquisa" value="${pesquisa}" class="form-control">
		</div>
		<div class="form-group">
			<label for="situacao">Situação</label> <select id="situacao"
				name="situacao" class="form-control">
				<option value="2">Todos</option>
				<option value="1">Ativos</option>
				<option value="0">Inativos</option>
			</select>
		</div>
		<button type="submit" class="btn btn-primary">Pesquisar</button>
		<a href="novo" class="btn btn-success">Novo</a>
	</form>

	<div style="height: 35px;"></div>

	<table class="table table-striped">
		<thead>
			<tr>
				<th>Número da matrícula</th>
				<th>Nome</th>
				<th>E-mail</th>
				<th>Data de Nascimento</th>
				<th>Situação</th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${funcionarios}" var="item">
				<tr>
					<td>${item.matricula}</td>
					<td>${item.nome}</td>
					<td>${item.email}</td>
					<td>${item.dataNascimentoVerbose}</td>
					<td>${item.situacaoVerbose}</td>
					<td><a href="editar?id=${item.id}" class="btn btn-warning">Editar</a>
						<button type="button" class="btn btn-primary mudar_situacao"
							data-id="${item.id}" data-nome="${item.nome}">Mudar
							Situação</button></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Confirmação de mudança de situação</h4>
				</div>
				<div class="modal-body">
					Tem certeza que deseja alterar a situação do funcionário <b><span id="nome_item_corrente"></span></b>?
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Voltar</button>
					<a href="" id="link_confirma" class="btn btn-primary">Confirmar</a>
				</div>
			</div>
		</div>
	</div>

</div>

<script type="text/javascript">
	$(function() {
		$(".mudar_situacao").on("click", function() {
			$("#id_item_corrente").val($(this).data("id"));
			$("#nome_item_corrente").html($(this).data("nome"));

			$("#link_confirma").attr("href", "/funcionarios/funcionarios/mudasituacao?id=" + $(this).data("id"));
			
			$('#myModal').modal();
		});

		$("#situacao").val('${situacao}');
	});
</script>