<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List, java.util.Date"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Provjera radara</title>
</head>
<body>
	<h1>REST MVC - Provjera radara</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/radari/pocetak">Početna
				stranica o radarima</a></li>
	</ul>
	<br />
	<%
	String odgovor = (String) request.getAttribute("odgovor");
	%>
	<%
	if (odgovor != null) {
	%>
	<p><%=odgovor%></p>
	<%
	}
	%>
</body>
</html>
