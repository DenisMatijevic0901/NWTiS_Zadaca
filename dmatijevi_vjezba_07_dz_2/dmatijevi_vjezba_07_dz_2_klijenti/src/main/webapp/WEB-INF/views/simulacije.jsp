<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List, java.util.Date, java.text.SimpleDateFormat,edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.Voznja"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Pregled vožnji</title>
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
	<h1>REST MVC - Pregled vožnji</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/simulacije/pocetak">Početna
				stranica o simulacijama</a></li>
	</ul>
	<br />
	<table>
		<tr>
			<th>R.br.
			<th>Vozilo</th>
			<th>Broj</th>	
			<th>Vrijeme</th>
			<th>Brzina</th>
			<th>Snaga</th>
			<th>Struja</th>
			<th>Visina</th>
			<th>GPS brzina</th>
			<th>Temp vozila</th>
			<th>Postotak baterije</th>
			<th>Napon baterije</th>
			<th>Kapacitet baterije</th>
			<th>Temp baterije</th>
			<th>Preostalo km</th>
			<th>Ukupno km</th>
			<th>GPS širina</th>
			<th>GPS dužina</th>
		</tr>
		<%
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
		List<Voznja> voznje = (List<Voznja>) request.getAttribute("voznje");
		for (Voznja v : voznje) {
		  i++;
		  Date vrijeme = new Date(v.getVrijeme() * 1000);
		%>
		<tr>
			<td class="desno"><%=i%></td>
			<td><%=v.getId()%></td>
			<td><%=v.getBroj()%></td>
			<td><%=sdf.format(vrijeme)%></td>
			<td><%=v.getBrzina()%></td>
			<td><%=v.getSnaga()%></td>
			<td><%=v.getStruja()%></td>
			<td><%=v.getVisina()%></td>
			<td><%=v.getGpsBrzina()%></td>
			<td><%=v.getTempVozila()%></td>
			<td><%=v.getPostotakBaterija()%></td>
			<td><%=v.getNaponBaterija()%></td>
			<td><%=v.getKapacitetBaterija()%></td>
			<td><%=v.getTempBaterija()%></td>
			<td><%=v.getPreostaloKm()%></td>
			<td><%=v.getUkupnoKm()%></td>
			<td><%=v.getGpsSirina()%></td>
			<td><%=v.getGpsDuzina()%></td>
		</tr>
		<%
		}
		%>
	</table>
</body>
</html>
