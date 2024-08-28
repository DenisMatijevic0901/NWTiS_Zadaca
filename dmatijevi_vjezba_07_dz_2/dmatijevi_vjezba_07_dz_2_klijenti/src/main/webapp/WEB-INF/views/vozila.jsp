<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List, java.util.Date, java.text.SimpleDateFormat,edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.PracenaVoznja"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Pregled praćenih vožnji</title>
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
	<h1>REST MVC - Pregled praćenih vožnji</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/vozila/pocetak">Početna
				stranica o vozilima</a></li>
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
		List<PracenaVoznja> praceneVoznje = (List<PracenaVoznja>) request.getAttribute("praceneVoznje");
		for (PracenaVoznja pv : praceneVoznje) {
		  i++;
		  Date vrijeme = new Date(pv.getVrijeme() * 1000);
		%>
		<tr>
			<td class="desno"><%=i%></td>
			<td><%=pv.getId()%></td>
			<td><%=pv.getBroj()%></td>
			<td><%=sdf.format(vrijeme)%></td>
			<td><%=pv.getBrzina()%></td>
			<td><%=pv.getSnaga()%></td>
			<td><%=pv.getStruja()%></td>
			<td><%=pv.getVisina()%></td>
			<td><%=pv.getGpsBrzina()%></td>
			<td><%=pv.getTempVozila()%></td>
			<td><%=pv.getPostotakBaterija()%></td>
			<td><%=pv.getNaponBaterija()%></td>
			<td><%=pv.getKapacitetBaterija()%></td>
			<td><%=pv.getTempBaterija()%></td>
			<td><%=pv.getPreostaloKm()%></td>
			<td><%=pv.getUkupnoKm()%></td>
			<td><%=pv.getGpsSirina()%></td>
			<td><%=pv.getGpsDuzina()%></td>
		</tr>
		<%
		}
		%>
	</table>
</body>
</html>
