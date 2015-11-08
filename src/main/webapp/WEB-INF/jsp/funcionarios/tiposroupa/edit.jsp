<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
	<h1>Edição Roupa</h1>

	<div class="row">
		<form:form commandName="tipoRoupa" method="POST" action="alterar"
			cssClass="col-xs-3" id="form">
			<form:hidden path="id" />
			<div class="form-group">
				<form:label path="nome">Nome</form:label>
				<form:input path="nome" maxlength="50" cssClass="form-control" />
				<form:errors path="nome" cssClass="error"></form:errors>
			</div>
			<div class="form-group">
				<form:label path="preco">Preço (R$)</form:label>
				<form:input path="preco" maxlength="10" cssClass="form-control" />
				<form:errors path="preco" cssClass="error"></form:errors>
			</div>
			<div class="form-group">
				<form:label path="diasLavagem">Tempo Lavagem (Dias)</form:label>
				<form:input path="diasLavagem" maxlength="2" cssClass="form-control" />
				<form:errors path="diasLavagem" cssClass="error"></form:errors>
			</div>
			<div class="form-group">
				<form:label path="situacaoVerbose">Situação</form:label>
				<form:select path="situacao" cssClass="form-control">
					<form:options items="${situacoes}"></form:options>
				</form:select>
				<form:errors path="situacao" cssClass="error"></form:errors>
			</div>
			<div class="form-group">
				<a href="index" class="btn btn-info">Voltar</a> 
				<input type="submit" value="Salvar" class="btn btn-success"id="btn_salvar" />
			</div>
		</form:form>
	</div>

</div>

<script type="text/javascript">
	$(function() {
		$("#preco").val(parseFloat($("#preco").val()).toFixed(2));

		$("#preco").maskMoney({
		    prefix: "R$ ",
		    thousands: ".",
		    decimal: ","
		});
		
		$("#preco").focus();
		$("#nome").focus();

		$("#diasLavagem").keyup(function() {
			$(this).val($(this).val().replace(/[^0-9\.]/g, ''));
		});

		$("#form").validate({
		    rules: {
		        nome: {
		            required: true,
		            maxlength: 50
		        },
		        preco: {
		            required: true,
		            min: 0.1
		        },
		        diasLavagem: {
		            required: true,
		            number: true
		        },
		        situacao: {
		            required: true
		        }
		    },
            messages: {
            	preco: {
            		min: "Pro favor, introduza um valor maior que Zero."
            	}
            }
		});
		
		$("#btn_salvar").on("click", function(e) {
			e.preventDefault();
			$("#preco").val($("#preco").maskMoney('unmasked')[0]);
			$("#form").submit();
		});
	});
</script>