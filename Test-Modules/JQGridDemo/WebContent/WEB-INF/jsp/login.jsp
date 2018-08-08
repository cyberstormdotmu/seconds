<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
</head>
<body>
		<br><br><br>
	<center>
		Login Page....
	<form action="authenticateUser.html" method="post">
		
		<table>
			<tr>
				<td>Enter User Name:</td>
				<td><input type="text" name="userName"></td>
			</tr>
			
			<tr>
				<td>Enter Password:</td>
				<td><input type="password" name="password"></td>
			</tr>
			
			<tr>
				<td></td>
				<td>
					<input type="submit" value="Login">
					<input type="reset" value="Clear">
				</td>
			</tr>	
		</table>
	</form>
	</center>
</body>
</html>