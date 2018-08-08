<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>Epooq</title>
<link href="${pageContext.request.contextPath}/resources/css/form/form_style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/css/confirmbox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.confirm.js"></script>
<style>
.error {
	color: #ff0000;
	font-style: italic;
}

input,select {
	float: right;
}

img.mapimage {
	height: 100%;
	width: 60%;
}

.left {
	width: 400px;
	float: left;
	height: 283px;
}

.right {
	width: 400px;
	float: left;
	height: 283px;
}

.row {
	width: 360px;
}
</style>
<script type="text/javascript">
	var path = "";
	var historicalOverlay;
	var map;
	var rectangle;
	var bounds;
	var mapOptions;

	$(document)
			.ready(
					function() {

						var error_message = document.getElementById("error_message");
						if(error_message.value)
						{
							var msgbg = document.getElementsByClassName("msgbg")[0];
							var error_msg = document.getElementById("error_msg");
							error_msg.innerHTML = error_message.value;
							msgbg.style.display = "block";
						}
						var mapStartLat;
						var mapEndLat;
						var mapStartLong;
						var mapEndLong;
						var mylevel;
						var myTitle;
						
						if ("${mapImage}" != null && "${mapImage.startLat}" != 0.0 && "${mapImage.startLat}" != "") 
						{
							mapStartLat = parseFloat("${mapImage.startLat}");
							mapEndLat = parseFloat("${mapImage.endLat}");
							mapStartLong = parseFloat("${mapImage.startLong}"); 
							mapEndLong = parseFloat("${mapImage.endLong}");
							mylevel = parseFloat("${mapImage.level}");
							myTitle = parseFloat("${mapImage.title}");
						}
						else
						{	
							mapStartLat = 67;
							mapStartLong = 29;
							mapEndLat =63;
							mapEndLong = 25;
							mylevel = 6;
							myTitle = document.getElementById("titleTemp").value;
						}
						
						document.getElementById("startLat").value = mapStartLat;
						document.getElementById("startLong").value = mapStartLong;
						document.getElementById("endLat").value = mapEndLat;
						document.getElementById("endLong").value = mapEndLong;
						
						
						document.getElementById("hstartLat").value = mapStartLat;
						 document.getElementById("hstartLong").value = mapStartLong;
						document.getElementById("hendLat").value = mapEndLat; 
						document.getElementById("hendLong").value = mapEndLong;
						
						
						document.getElementById("level").value = mylevel;
						document.getElementById("level2").value = mylevel;
						document.getElementById("title1").value = myTitle;
						
						loadScript();
						
						path = document.getElementById("file").value;

						if (path.length != 0) {
							document.getElementById("imagearea").innerHTML = '<img src = "${pageContext.request.contextPath}/' + path + '" class="mapimage"/>';
							
						}
					
						//setRect();
					});

	function validateForm() {
		var file = document.forms["uploadMapImage"]["file"].value;
		var title = document.forms["uploadMapImage"]["title"].value;
		
		if (file == null || file == "") {
			var msgbg = document.getElementsByClassName("msgbg")[0];
			var error_msg = document.getElementById("error_msg");
			error_msg.innerHTML = "<spring:message code="adminEroor.requireImage"/>";
			msgbg.style.display = "block";

			return false;
		}
		if (title == null || title == "") {
			var msgbg = document.getElementsByClassName("msgbg")[0];
			var error_msg = document.getElementById("error_msg");
			error_msg.innerHTML = "<spring:message code="adminEroor.requireTitle"/>";
			msgbg.style.display = "block";

			return false;
		}
		return true;
	}

	function validateImage()
	{	
		var imageFile = document.forms["ChooseMapImage"]["imageUpload"].value;
		if (imageFile == null || imageFile == "") {
			var msgbg = document.getElementsByClassName("msgbg")[0];
			var error_msg = document.getElementById("error_msg");
			error_msg.innerHTML = "<spring:message code="adminEroor.requireImage"/>";
			msgbg.style.display = "block";

			return false;
		}
	}
	
	function saveMapImage() {
		if (validateForm()) {
			document.getElementById("uploadMapImage").submit();
		}
	}

	function addImage() {
		path = document.getElementById("imageUpload").value;

		if (path != "" && path.length != 0) {
			document.getElementById("imagearea").innerHTML = "<img src = '" + path + "' />";
		}
		document.getElementById("file").value = "";
	}

	//loadScript();
	function initialize() {
		var error_message = document.getElementById("error_message");
		if(error_message.value)
		{
			var msgbg = document.getElementsByClassName("msgbg")[0];
			var error_msg = document.getElementById("error_msg");
			error_msg.innerHTML = error_message.value;
			msgbg.style.display = "block";
		}
		var myLatLng;
		var zoomlvl;

		if ("${mapImage}" != null && "${mapImage.startLat}" != 0.0) {
			
			var mapStartLat = parseFloat("${mapImage.startLat}");
			var mapEndLat = parseFloat("${mapImage.endLat}");
			var mapStartLong = parseFloat("${mapImage.startLong}");
			var mapEndLong = parseFloat("${mapImage.endLong}");
			zoomlvl = parseInt("${mapImage.level}");
			
			myLatLng = new google.maps.LatLng((mapStartLat + mapEndLat) / 2,(mapStartLong + mapEndLong) / 2);

					bounds = new google.maps.LatLngBounds(new google.maps.LatLng(
							mapEndLat, mapEndLong), new google.maps.LatLng(
							mapStartLat, mapStartLong));

		} else {
			
			myLatLng = new google.maps.LatLng(65, 27);
			zoomlvl = 6;
			bounds = new google.maps.LatLngBounds(
					new google.maps.LatLng(63, 25), new google.maps.LatLng(67,
							29));

		}
		
		mapOptions = {
				zoom : zoomlvl,
				center : myLatLng
		};

		// var markers = [];

		map = new google.maps.Map(document.getElementById('mappart'),
				mapOptions);

		if (path != "" && path.length != 0) {
			historicalOverlay = new google.maps.GroundOverlay(
					"${pageContext.request.contextPath}/" + path, bounds);

			historicalOverlay.setMap(map);
			
			
		} else {
			rectangle = new google.maps.Rectangle({
				bounds : bounds,
				editable : true,
				draggable : true
			});

			rectangle.setMap(map);

			google.maps.event.addListener(rectangle, 'bounds_changed',
					showNewRect);
		}
		

		  google.maps.event.addListener(map, 'zoom_changed', function() {
		    var zoomLevel = map.getZoom();
		    document.getElementById("level").value = zoomLevel;
		    document.getElementById("level2").value = zoomLevel;
		  });

		function showNewRect(event) {
			var ne = rectangle.getBounds().getNorthEast();
			var sw = rectangle.getBounds().getSouthWest();

			document.getElementById("startLat").value = ne.lat();
			document.getElementById("startLong").value = ne.lng();
			document.getElementById("endLat").value = sw.lat();
			document.getElementById("endLong").value = sw.lng();
			document.getElementById("hstartLat").value = ne.lat();
			document.getElementById("hstartLong").value = ne.lng();
			document.getElementById("hendLat").value = sw.lat();
			document.getElementById("hendLong").value = sw.lng();
		}

	}
	function loadScript() {
		var script = document.createElement('script');
		script.type = 'text/javascript';
		script.src = 'https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&callback=initialize';
		document.body.appendChild(script);

	}

	function readURL(input) {

		if (input.files && input.files[0]) {
			var reader = new FileReader();

			reader.onload = function(e) {
				document.getElementById("imagearea").innerHTML = "<img src = '" + e.target.result + "' class='mapimage' />";
			};

			reader.readAsDataURL(input.files[0]);
		}
		$("#title1").val($("#title").val());
		document.getElementById("chooseMapImage").submit();
	}
	
	function titlewrite(value)
	{
		document.getElementById("title1").value = value;
	}
	
	$(document).on("click", "a.close", function()
			{

				$.confirm({
					text: "<spring:message code="adminAlert.cancleMapOne"/><br/><spring:message code="adminAlert.cancleMapTwo"/>",
					confirm: function(button) {
						closePopup();
				    },
				    cancel: function(button) {
				    	return false;
				    },
				    confirmButton : "<spring:message code="button.yes" />",
					cancelButton : "<spring:message code="button.no" />"
				});
			});

	function closePopup()
	{
		var cancelPage = "${pageContext.request.contextPath}"+"/admin/listMapImage.html";
		window.location.replace(cancelPage);
	}

	function removeUploadedImage()
	{
		var form = document.forms["uploadMapImage"];
		form.action = "${pageContext.request.contextPath}"+"/admin/removeImage.html";
		form.submit();
	}

	function closeError(){
		$(".msgbg").hide();
	}

</script>
<%-- </jsp:attribute> --%>
</head>

<%-- <jsp:body> --%>
<body>
	<input type="hidden" id="error_message" value="${error}"></input>
	<input type="hidden" id="titleTemp" value="${title}"></input>
	<input type="hidden" id="levelTemp" value="${level}"></input>
	<div class="boxpanel storywindow">
		<h1 id="popup_page">
			<spring:message code="uploadMapImage.message"/>
			<a href="#" class="close"></a>
		</h1>
		<div class="box-content">
			<h2 class="bord"><spring:message code="uploadMapImage.messageSelect"/></h2>

			<div class="row">
				<div class="msgbg" style="display: none;">
					<div class="msg_left">
						<div class="msg_right">
							<div class="msg_error">
								<img src="${pageContext.request.contextPath}/resources/img/form/ic_error.png" alt=""
									onclick="closeError()" /> <span id="error_msg"></span>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="innercontent">
				<div class="left">
					<form:form
						action="${pageContext.request.contextPath}/admin/uploadMapImage.html"
						method="post" id="uploadMapImage" name="uploadMapImage"
						onsubmit="validateForm();" modelAttribute="mapImage"
						enctype="multipart/form-data">
						<div class="contblock">
							<div class="leftpart">
								<div class="row">
									<label class="inline"><spring:message code="listMapImage.North-East_lat"/>:</label>
									<form:input path="startLat" class="short" readonly="true"/>
								</div>
								<div class="row">
									<label class="inline"><spring:message code="listMapImage.North-East_lng"/>:</label>
									<form:input path="startLong" class="short" readonly="true" />
								</div>
								<div class="row">
									<label class="inline"><spring:message code="listMapImage.South-West_lat"/>: </label>
									<form:input path="endLat" class="short" readonly="true" />
									
								</div>
								<div class="row">
									<label class="inline"><spring:message code="listMapImage.South-West_lng"/>:</label>
									<form:input path="endLong" class="short" readonly="true" />
								</div>
								<div class="row">
									<label class="inline"><spring:message code="title"/>:</label>
									<form:input id="title" path="title" class="short" onchange="titlewrite(this.value)"/>
								</div>
								<div class="row">
									<label class="inline"><spring:message code="level"/>: </label> <form:select path="level" id="level" name="level" onchange="levelwrite(this.value)" >
										<c:forEach var="i" begin="0" end="21">
											<form:option value="${i}"><c:out value="${i}" /></form:option>
										</c:forEach>
									</form:select>
<%-- 									<form:errors path="level" cssClass="error" /> --%>
								</div>
								
								<div class="row">
									<!-- 		        	<label class="inline">Image: </label> -->
									<form:hidden id="file" path="file" />
								</div>
	
							</div>
							
						</div>
						
					</form:form>
					<div class="row"></div>

				</div>
				<div class="right">
					<form:form
						action="${pageContext.request.contextPath}/admin/addImage.html"
						onsubmit="return validateImage();" name="ChooseMapImage" id="chooseMapImage"
						method="post" enctype="multipart/form-data">
						<input type="hidden" id="hstartLat" name="hstartLat">
						<input type="hidden" id="hstartLong" name="hstartLong">
						<input type="hidden" id="hendLat" name="hendLat">
						<input type="hidden" id="hendLong" name="hendLong">
						<input type="hidden" id="level2" name="level">
						<input type="hidden" id="title1" name="title">
						<label class="inline"><spring:message code="image"/>: </label>
						
						<input type="file" id="imageUpload" name="imageUpload"
							accept="image/*" onchange="return readURL(this).value;"
							class="linkbtn-yellow" />
							
						<input type="button" id="removeImage" value="<spring:message code="button.remove"/>" 
							class="linkbtn-yellow" onclick="removeUploadedImage()">
						<%-- <input type="submit" id="submitImage" value="<spring:message code="button.upload"/>"
							class="linkbtn-yellow"> --%>
					</form:form>
					<div id="imagearea"></div>
				</div>
			</div>
			<div class="row">
							<a href="#" onclick="saveMapImage();" class="linkbtn-blue"><spring:message code="save"/></a>
						</div>
			<div class="row"></div>
			<div class="innercontent" id="mappart"
				style="height: 600px; width: 800px;"></div>
			<div class="row"></div>
			
		</div>
		
	</div>
<%-- </jsp:body> --%>
</body>
<%-- </t:genericpage> --%>
</html>