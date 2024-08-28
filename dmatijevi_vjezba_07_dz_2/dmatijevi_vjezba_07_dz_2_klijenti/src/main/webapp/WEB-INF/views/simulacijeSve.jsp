<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Simulacije</title>
</head>
<body>
	<h1>REST MVC - Simulacije</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/sadrzaj">Sadržaj</a></li>

		<li>
			<h2>Pretraživanje vožnji u intervalu</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/simulacije/pretrazivanjeSimulacijeInterval">
				<table>
					<tr>
						<td>Od vremena:</td>
						<td><input name="odVremena" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Do vremena:</td>
						<td><input name="doVremena" />
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value=" Dohvati vožnje "></td>
					</tr>
				</table>
			</form>
		</li>

		<li>
			<h2>Pretraživanje vožnje za zadano vozilo</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/simulacije/ispisVoznjiZaVozilo">
				<table>
					<tr>
						<td>ID vozila:</td>
						<td><input name="id" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value=" Dohvati vožnje "></td>
					</tr>
				</table>
			</form>
		</li>

		<li>
			<h2>Pretraživanje vožnje za zadano vozilo u intervalu</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/simulacije/ispisVoznjiZaVoziloInterval">
				<table>
					<tr>
						<td>ID vozila:</td>
						<td><input name="id" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Od vremena:</td>
						<td><input name="odVremena" />
					</tr>
					<tr>
						<td>Do vremena:</td>
						<td><input name="doVremena" />
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value=" Dohvati vožnje "></td>
					</tr>
				</table>
			</form>
		</li>

		<li>
			<h2>Pokretanje simulacije</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/simulacije/simulacija">
				<table>
					<tr>
						<td>Naziv datoteke s podacima telemetrije:</td>
						<td><input name="nazivDatoteke" type="text" required /> <input
							type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}" />
						</td>
					</tr>
					<tr>
						<td>ID vozila:</td>
						<td><input name="idVozila" type="text" required /></td>
					</tr>
					<tr>
						<td>Trajanje sekunde:</td>
						<td><input name="trajanjeSek" type="number" required /></td>
					</tr>
					<tr>
						<td>Trajanje pauze:</td>
						<td><input name="trajanjePauze" type="number" required /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Pokreni simulaciju" /></td>
					</tr>
				</table>
			</form>
		</li>
	</ul>
</body>
</html>
