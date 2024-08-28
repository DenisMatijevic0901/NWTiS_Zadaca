<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Date, java.util.Map;"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Pregled radara</title>
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
	<h1>REST MVC - Pregled radara</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/radari/pocetak">Početna
				stranica o radarima</a></li>
	</ul>
	<br />

	<%
	List<Map<String, Object>> radari = (List<Map<String, Object>>) request.getAttribute("radari");
	int i = 0;
	if (radari != null && !radari.isEmpty()) {
	%>
	<h2>Popis radara:</h2>
	<table>
		<tr>
			<th>R.br.</th>
			<th>ID</th>
			<th>Adresa</th>
			<th>Mrežna vrata</th>
			<th>GPS širina</th>
			<th>GPS dužina</th>
			<th>Maksimalna udaljenost</th>
		</tr>
		<%
		for (Map<String, Object> radar : radari) {
		  i++;
		%>
		<tr>
			<td class="desno"><%=i%></td>
			<td><%= radar.get("id") %></td>
			<td><%=radar.get("adresa")%></td>
			<td><%=radar.get("mreznaVrata")%></td>
			<td><%=radar.get("gpsSirina")%></td>
			<td><%=radar.get("gpsDuzina")%></td>
			<td><%=radar.get("maksUdaljenost")%></td>
		</tr>
		<%
		}
		%>
	</table>
	<%
	} else {
	%>
	<p>Nema radara.</p>
	<%
	}
	%>


</body>
</html>
