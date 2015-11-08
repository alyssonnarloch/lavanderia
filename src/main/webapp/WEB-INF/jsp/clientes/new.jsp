<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
	<h1>Novo Cliente</h1>

	<div class="row">
		<form:form commandName="cliente" method="POST" action="criar"
			cssClass="col-xs-3" id="form">
			<div class="form-group">
				<form:label path="nome">Nome</form:label>
				<form:input path="nome" maxlength="200" cssClass="form-control" />
				<form:errors path="nome" cssClass="error"></form:errors>
			</div>
			<div class="form-group">
				<form:label path="sexo">Sexo</form:label>
				<form:select path="sexo" cssClass="form-control">
					<form:options items="${sexos}"></form:options>
				</form:select>
				<form:errors path="sexo" cssClass="error"></form:errors>
			</div>
			<div class="form-group">
				<form:label path="cpf">CPF</form:label>
				<form:input path="cpf" maxlength="50" cssClass="form-control" />
				<form:errors path="cpf" cssClass="error"></form:errors>
			</div>
			<div class="form-group">
				<form:label path="email">E-mail</form:label>
				<form:input path="email" maxlength="50" cssClass="form-control" />
				<form:errors path="email" cssClass="error"></form:errors>
			</div>
			<div class="form-group">
				<form:label path="endereco">Endereço</form:label>
				<form:input path="endereco" maxlength="50" cssClass="form-control" />
				<form:errors path="endereco" cssClass="error"></form:errors>
			</div>
			<div class="form-group">
				<form:label path="telefone">Telefone</form:label>
				<form:input path="telefone" maxlength="50" cssClass="form-control" />
				<form:errors path="telefone" cssClass="error"></form:errors>
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
				<a href="/" class="btn btn-danger">Cancelar</a> <input
					type="submit" value="Salvar" class="btn btn-success" id="btn_salvar" />
			</div>
		</form:form>
	</div>

</div>

<script type="text/javascript">
	$(function() {
		$("#cpf").mask("999.999.999-99");
		$("#telefone").mask("(99) 9999-9999?", {
			translation : {
				'?' : {
					pattern : /[0-9]/,
					optional : true
				}
			}
		});

		$("#form").validate({
			rules : {
				nome : {
					required : true,
					maxlength : 255
				},
				sexo : {
					required : true,
					maxlength : 255
				},
				email : {
					required : true,
					maxlength : 255,
					email : true
				},
				cpf : {
					required : true,
					maxlength : 15,
					cpf: true
				},
				endereco : {
					required : true,
					maxlength : 255
				},
				telefone : {
					required : true,
					maxlength : 50
				},
				senha : {
					required : true,
					minlength : 6
				},
				confirmacaoSenha : {
					required : true,
					minlength : 6,
					equalTo : "#senha"
				},
				situacao : "required"
			}
		});

		jQuery.validator.addMethod("cpf", function(value, element) {
			value = jQuery.trim(value);

			value = value.replace('.', '');
			value = value.replace('.', '');
			cpf = value.replace('-', '');
			while (cpf.length < 11)
				cpf = "0" + cpf;
			var expReg = /^0+$|^1+$|^2+$|^3+$|^4+$|^5+$|^6+$|^7+$|^8+$|^9+$/;
			var a = [];
			var b = new Number;
			var c = 11;
			for (i = 0; i < 11; i++) {
				a[i] = cpf.charAt(i);
				if (i < 9)
					b += (a[i] * --c);
			}
			if ((x = b % 11) < 2) {
				a[9] = 0
			} else {
				a[9] = 11 - x
			}
			b = 0;
			c = 11;
			for (y = 0; y < 10; y++)
				b += (a[y] * c--);
			if ((x = b % 11) < 2) {
				a[10] = 0;
			} else {
				a[10] = 11 - x;
			}

			var retorno = true;
			if ((cpf.charAt(9) != a[9]) || (cpf.charAt(10) != a[10])
					|| cpf.match(expReg))
				retorno = false;

			return this.optional(element) || retorno;

		}, "Por favor, introduza um CPF válido.");
	});
</script>