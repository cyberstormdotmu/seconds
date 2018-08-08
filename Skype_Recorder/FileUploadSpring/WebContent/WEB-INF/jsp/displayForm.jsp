<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>


	<script 
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
	<script>
	$(document).ready(function() {
		//add more file components if Add is clicked
		$('#addFile').click(function() {
			var fileIndex = $('#fileTable tr').children().length ;
			alert(fileIndex);
			$('#fileTable').append(
					'<tr><td>'+
					'	<input type="file" name="files['+ fileIndex +']" />'+
					'</td></tr>');
		});
		
	});
	</script>


</head>
<body>
	
	<center>
		<h1>Spring Multiple File Upload example</h1>
		
		<form:form method="post" action="${pageContext.request.contextPath}/upload.spring" 
				modelAttribute="uploadForm" enctype="multipart/form-data">
		
			<p>Select files to upload. Press Add button to add more file inputs.</p>
		
			<input id="addFile" type="button" value="Add File" />
			<table id="fileTable">
				<tr>
					<td><input name="files[0]" type="file" /></td>
				</tr>
			</table>
			<br/><input type="submit" value="Upload" />
		</form:form>
	</center>
</body>
</html>