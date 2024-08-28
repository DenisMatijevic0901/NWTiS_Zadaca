<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List, java.util.Date, java.text.SimpleDateFormat,edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.PracenaVoznja"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Pregled praćenih vožnji</title>
</head>
<body>
	<h1>REST MVC - Praćenje vožnje za vozilo</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/vozila/pocetak">Početna
				stranica o vozilima</a></li>
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
