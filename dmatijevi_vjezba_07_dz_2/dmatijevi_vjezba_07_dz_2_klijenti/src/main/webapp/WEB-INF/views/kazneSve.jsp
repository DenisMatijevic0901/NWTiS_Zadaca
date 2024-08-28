<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Kazne</title>
</head>
<body>
	<h1>REST MVC - Kazne</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/sadrzaj">Sadržaj</a></li>
		
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKazni">Ispis
				svih kazni</a></li>

		<li>
			<h2>Pretraživanje kazne s traženim rb</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKaznaRb">
				<table>
					<tr>
						<td>Redni broj kazne:</td>
						<td><input name="rb" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value=" Dohvati kazne "></td>
					</tr>
				</table>
			</form>
		</li>

		<li>
			<h2>Pretraživanje kazni u intervalu</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/kazne/pretrazivanjeKazniInterval">
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
						<td><input type="submit" value=" Dohvati kazne "></td>
					</tr>
				</table>
			</form>
		</li>

		<li>
			<h2>Pretraživanje kazne za zadano vozilo</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKazneZaVozilo">
				<table>
					<tr>
						<td>ID vozila:</td>
						<td><input name="id" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value=" Dohvati kazne "></td>
					</tr>
				</table>
			</form>
		</li>

		<li>
			<h2>Pretraživanje kazne za zadano vozilo u intervalu</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKazneZaVoziloInterval">
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
						<td><input type="submit" value=" Dohvati kazne "></td>
					</tr>
				</table>
			</form>
		</li>
	</ul>
</body>
</html>
