<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>Lavanderia On-Line</title>

<link
	href="<c:url value='/public/bootstrap-3.3.4/dist/css/bootstrap.min.css' />"
	rel="stylesheet" type="text/css" />
<link
	href="<c:url value='/public/bootstrap-3.3.4/docs/examples/dashboard/dashboard.css' />"
	rel="stylesheet" type="text/css" />
<link
	href="<c:url value='/public/datepicker/css/datepicker.css' />"
	rel="stylesheet" type="text/css" />
<link
	href="<c:url value='/public/jquery-validation-1.13.1/dist/cmxform.css' />"
	rel="stylesheet" type="text/css" />

</head>

<body>
	<script
		src="<c:url value='/public/jquery-1.11.3/jquery-1.11.3.min.js' />"></script>
	<script
		src="<c:url value='/public/bootstrap-3.3.4/dist/js/bootstrap.min.js' />"></script>
	<script
		src="<c:url value='/public/bootstrap-3.3.4/docs/assets/js/vendor/holder.js' />"></script>
	<script
		src="<c:url value='/public/bootstrap-3.3.4/docs/assets/js/ie10-viewport-bug-workaround.js' />"></script>
	<script
		src="<c:url value='/public/datepicker/js/bootstrap-datepicker.js' />"></script>
	<script
		src="<c:url value='/public/jquery-validation-1.13.1/dist/jquery.validate.min.js' />"></script>
	<script
		src="<c:url value='/public/jquery-validation-1.13.1/dist/additional-methods.min.js' />"></script>
	<script
		src="<c:url value='/public/jquery-validation-1.13.1/dist/dateBR.js' />"></script>		
	<script
		src="<c:url value='/public/jquery-validation-1.13.1/dist/localization/messages_pt_PT.min.js' />"></script>
	<script
		src="<c:url value='/public/jquery-mask-1.11.4/dist/jquery.mask.min.js' />"></script>
	<script
		src="<c:url value='/public/jquery-maskmoney-master/dist/jquery.maskMoney.min.js' />"></script>

	<tiles:insertAttribute name="header" />
	<tiles:insertAttribute name="menu" />
	<tiles:insertAttribute name="body" />
</body>

</html>

<style>
span.error {
	color: red;
	font-weight: bold;
	font-size: 12px;
	font-family: fantasy;
}
</style>

<script type="text/javascript">
	$(function() {
		$("span.error").prev('input').addClass("error");
	});
</script>