<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Vozila</title>
</head>
<body>
	<h1>REST MVC - Vozila</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/sadrzaj">Sadržaj</a></li>

		<li>
			<h2>Pretraživanje praćenih vožnji u intervalu</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/pretrazivanjeVoziloInterval">
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
						<td><input type="submit" value=" Dohvati praćene vožnje "></td>
					</tr>
				</table>
			</form>
		</li>

		<li>
			<h2>Pretraživanje praćene vožnje za zadano vozilo</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/ispisPracenihVoznjiZaVozilo">
				<table>
					<tr>
						<td>ID vozila:</td>
						<td><input name="id" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value=" Dohvati praćene vožnje "></td>
					</tr>
				</table>
			</form>
		</li>

		<li>
			<h2>Pretraživanje praćene vožnje za zadano vozilo u intervalu</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/ispisPracenihVoznjiZaVoziloInterval">
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
						<td><input type="submit" value=" Dohvati praćene vožnje "></td>
					</tr>
				</table>
			</form>
		</li>

		<li>
			<h2>Pokreni praćenje vožnje za zadano vozilo</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/pokreniPracenjeVoznje">
				<table>
					<tr>
						<td>ID vozila:</td>
						<td><input name="id" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value=" Pokreni praćenje "></td>
					</tr>
				</table>
			</form>
		</li>

		<li>
			<h2>Prekini praćenje vožnje za zadano vozilo</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/prekiniPracenjeVoznje">
				<table>
					<tr>
						<td>ID vozila:</td>
						<td><input name="id" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value=" Prekini praćenje "></td>
					</tr>
				</table>
			</form>
		</li>

	</ul>
</body>
</html>
