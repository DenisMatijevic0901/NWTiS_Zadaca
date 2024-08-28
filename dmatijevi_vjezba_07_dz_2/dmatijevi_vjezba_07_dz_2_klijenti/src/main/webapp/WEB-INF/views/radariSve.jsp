<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Radari</title>
</head>
<body>
	<h1>REST MVC - Radari</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/sadrzaj">Sadržaj</a></li>

		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/radari/dohvatiSveRadare">Ispis
				svih radara</a></li>

		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/radari/resetSvihRadara">Resetiraj radare</a></li>

		<li>
			<h2>Dohvaćanje podataka za radar sa zadanim ID</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/radari/dohvatiJednogRadara">
				<table>
					<tr>
						<td>ID Radara:</td>
						<td><input name="id" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value=" Dohvati radar "></td>
					</tr>
				</table>
			</form>
		</li>

		<li>
			<h2>Provjera radara sa zadanim ID</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/radari/provjeriRadar">
				<table>
					<tr>
						<td>ID Radara:</td>
						<td><input name="id" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value=" Provjeri radar "></td>
					</tr>
				</table>
			</form>
		</li>

		<li>
			<h2>Brisanje svih radara</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/radari/obrisiRadare">
				<input type="hidden" name="metoda" value="DELETE">
				<button type="submit">Brisanje svih radara</button>
			</form>
		</li>

		<li>
			<h2>Brisanje radara sa zadanim ID</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/radari/obrisiJednogRadara">
				<table>
					<tr>
						<td>ID Radara:</td>
						<td><input name="id" /> <input type="hidden" name="metoda"
							value="DELETE"> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value=" Obriši radar "></td>
					</tr>
				</table>
			</form>
		</li>


	</ul>
</body>
</html>
