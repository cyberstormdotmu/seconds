<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style>
body {
	background-color: #EEEEEE;
	font-family: Verdana,Calibri,Cambria,Arial;
	font-size: 11px;
}

table {
	border: solid black 1px;	
}

.header > td {
	font-weight: bold;
	text-align: center;
}

td {
	padding: 4px;	
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!-- <link rel="stylesheet" type="text/css" href="./css/style.css"> -->

<title>Spring MVC Rest Demo</title>

</head>
<body>
	<h3>Use Rest Client Add-on for test.</h3>
	<br/>
	<br/>
	<h4>Web Service URIS:</h4>
	<table border="1">
		<tr class="header">
			<td>No.</td>
			<td>Method</td>
			<td>URI</td>
			<td>Function</td>
		</tr>
		<tr>
			<td>1</td>
			<td>GET</td>
			<td>http://192.168.0.53:8080/SpringMVCRest/country</td>
			<td>Selects list of countries</td>
		</tr>
		<tr>
			<td>2</td>
			<td>GET</td>
			<td>http://192.168.0.53:8080/SpringMVCRest/country/{countryId}</td>
			<td>Selects Country with integer country Id</td>
		</tr>
		<tr>
			<td>3</td>
			<td>POST</td>
			<td>http://192.168.0.53:8080/SpringMVCRest/country/{countryName}</td>
			<td>Inserts Country with String country name</td>
		</tr>
		<tr>
			<td>4</td>
			<td>PUT</td>
			<td>http://192.168.0.53:8080/SpringMVCRest/{countryId}/{countryName}</td>
			<td>Updates country name</td>
		</tr>
		<tr>
			<td>5</td>
			<td>DELETE</td>
			<td>http://192.168.0.53:8080/SpringMVCRest/country/{countryId}</td>
			<td>Deletes country with integer country Id</td>
		</tr>
		<tr>
			<td>6</td>
			<td>GET</td>
			<td>http://192.168.0.53:8080/SpringMVCRest/city</td>
			<td>Selects list of cities</td>
		</tr>
		<tr>
			<td>7</td>
			<td>GET</td>
			<td>http://192.168.0.53:8080/SpringMVCRest/city/country/{countryId}</td>
			<td>Selects list of cities of country with integer country Id</td>
		</tr>
		<tr>
			<td>8</td>
			<td>GET</td>
			<td>http://192.168.0.53:8080/SpringMVCRest/city/{cityId}</td>
			<td>Selects city with integer city Id</td>
		</tr>
		<tr>
			<td>9</td>
			<td>POST</td>
			<td>http://192.168.0.53:8080/SpringMVCRest/city/{countryId}/{cityName}</td>
			<td>Inserts City</td>
		</tr>
		<tr>
			<td>10</td>
			<td>PUT</td>
			<td>http://192.168.0.53:8080/SpringMVCRest/city/{cityId}/{countryId}/{cityName}</td>
			<td>Updates City</td>
		</tr>
		<tr>
			<td>11</td>
			<td>DELETE</td>
			<td>http://192.168.0.53:8080/SpringMVCRest/city/{cityId}</td>
			<td>Deletes City with integer city Id</td>
		</tr>
	</table>
</body>
</html>