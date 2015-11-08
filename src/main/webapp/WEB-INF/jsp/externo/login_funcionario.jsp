<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="container" style="width: 360px;">
	<h2>Lavanderia On-Line Área do Funcionário</h2>
	<form id="form" class="form-horizontal" action="/funcionariologin" method="POST">
	  <div class="form-group">
	    <div class="col-sm-10">
	      <input type="text" id="matricula" name="matricula" class="form-control" placeholder="Matrícula" value="${matricula}" />
	      ${erros.matricula}
	    </div>
	  </div>
	  <div class="form-group">
	    <div class="col-sm-10">
	      <input type="password" id="senha" name="senha" class="form-control" placeholder="Senha" />
	      ${erros.senha}
	      ${erros.ambos}
	    </div>
	  </div>
	  <div class="form-group">
	    <div class="col-sm-offset-5">
	    	<a href="/" class="btn btn-default">Voltar</a>
	      	<input type="submit" value="Entrar" class="btn btn-info" />	     
	    </div>
	  </div>
	</form>
</div>

<script type="text/javascript">
    $(function () {
        $("#form").validate({
            rules: {
                matricula: "required",
                senha: "required"
            },
            messages: {
            	matricula: "Informe sua matrícula.",
            	senha: "Informe sua senha."
            }
        });
    });

</script>