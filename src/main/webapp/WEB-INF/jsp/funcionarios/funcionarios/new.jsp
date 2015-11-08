<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
	<h1>Novo Funcionário</h1>

	<div class="row">
		<form:form commandName="funcionario" method="POST" action="criar"
			cssClass="col-xs-3" id="form">
			<div class="form-group">
				<form:label path="matricula">Número da Matrícula</form:label>
				<form:input path="matricula" maxlength="50" cssClass="form-control" />
				<form:errors path="matricula" cssClass="error"></form:errors>
			</div>
			<div class="form-group">
				<form:label path="nome">Nome</form:label>
				<form:input path="nome" maxlength="200" cssClass="form-control" />
				<form:errors path="nome" cssClass="error"></form:errors>
			</div>
			<div class="form-group">
				<form:label path="email">E-mail</form:label>
				<form:input path="email" maxlength="50" cssClass="form-control" />
				<form:errors path="email" cssClass="error"></form:errors>
			</div>
			<div class="form-group">
				<form:label path="dataNascimento">Data de Nascimento</form:label>
				<form:input path="dataNascimento" maxlength="10"
					cssClass="form-control" />
				<form:errors path="dataNascimento" cssClass="error"></form:errors>
			</div>
			<div class="form-group">
				<form:label path="senha">Senha</form:label>
				<form:password path="senha" maxlength="30" cssClass="form-control" />
				<form:errors path="senha" cssClass="error"></form:errors>
			</div>
			<div class="form-group">
				<form:label path="confirmacaoSenha">Confirmação de Senha</form:label>
				<form:password path="confirmacaoSenha" maxlength="30"
					cssClass="form-control" />
				<form:errors path="confirmacaoSenha" cssClass="error"></form:errors>
			</div>
			<div class="form-group">
				<form:label path="situacaoVerbose">Situação</form:label>
				<form:input disabled="true" path="situacaoVerbose"
					cssClass="form-control" />
				<form:hidden path="situacao" />
				<form:errors path="situacao" cssClass="error"></form:errors>
			</div>
			<div class="form-group">
				<a href="index" class="btn btn-info">Voltar</a>
				<input type="submit" value="Salvar" class="btn btn-success" id="btn_salvar" />
			</div>
		</form:form>
	</div>

</div>

<script type="text/javascript">
	$(function() {
		//$("#dataNascimento").mask("99/99/9999");
		$("#dataNascimento").datepicker({
			format: "dd/mm/yyyy"
		});

		$("#matricula").keyup(function() {
			$(this).val($(this).val().replace(/[^0-9\.]/g, ''));
		});

		$("#form").validate({
		    rules: {
		        matricula: {
		            required: true,
		            maxlength: 50,
		            minlength: 6
		        },
		        nome: {
		            required: true,
		            maxlength: 200
		        },
		        email: {
		            required: true,
		            maxlength: 50,
		            email: true
		        },
		        dataNascimento: {
		            required: true,
		            maxlength: 50,
		            dateBR: true
		        },
		        senha: {
		            required: true,
		            minlength: 6
		        },
		        confirmacaoSenha: {
		            required: true,
		            minlength: 6,
		            equalTo: "#senha"
		        },
		        situacao: "required"
		    }
		});
	});
</script>