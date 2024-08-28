<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Date"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Simulacija</title>
<style type="text/css">
table, th, td {
	border: 1px solid;
}

th {
	text-align: center;
	font-weight: bold;
}

.desno {
	text-align: right;
}
</style>
</head>
<body>
	<h1>REST MVC - Simulacija</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/simulacije/pocetak">Početna
				stranica o simulacijama</a></li>
	</ul>
	<br />
		<%
	String greske = (String) request.getAttribute("greske");
	if (greske != null && !greske.isEmpty()) {
	%>
	<h2>Greške</h2>
	<pre><%=greske%></pre>
	<%
	} else {
	%>
	<h2>Uspješna simulacija</h2>
	<p>Simulacija je uspješno završena bez grešaka.</p>
	<%
	}
	%>
</body>
</html>
