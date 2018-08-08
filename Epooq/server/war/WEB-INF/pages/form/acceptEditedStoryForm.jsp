<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>

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
</style>
<script type="text/javascript">

	$(document).ready(function() {
		var error_message = document.getElementById("error_message");
		if (error_message.value) {
			var msgbg = document.getElementsByClassName("msgbg")[0];
			var error_msg = document.getElementById("error_msg");
			error_msg.innerHTML = error_message.value;
			msgbg.style.display = "block";
		}
	});
	
	function closeError(){
		$(".msgbg").hide();
	}

	function saveStory() {
		if (validateForm()) {
			document.getElementById("savestory").submit();
		}
	}
	

	function validateForm() {
		//title validation
		var title = document.forms["savestoryForm"]["title"].value;
		if (title==null || title=="") 
		{
		    var msgbg = document.getElementsByClassName("msgbg")[0];
			var error_msg = document.getElementById("error_msg");
			error_msg.innerHTML ="<spring:message code="story.title_required"/>";
			msgbg.style.display = "block";
			
		    return false;
		}else if(title.length>30){
			var msgbg = document.getElementsByClassName("msgbg")[0];
			var error_msg = document.getElementById("error_msg");
			error_msg.innerHTML ="<spring:message code="story.title_exceed"/>";
			msgbg.style.display = "block";
			
		    return false;
		}
		//country validation
		var country = document.forms["savestoryForm"]["country"].value;
		
		if (country==null || country=="") {
			return true;
		}
		else if (country.length>30) 
		{
		    var msgbg = document.getElementsByClassName("msgbg")[0];
			var error_msg = document.getElementById("error_msg");
			error_msg.innerHTML ="<spring:message code="story.country_exceed"/>";
			msgbg.style.display = "block";
			
		    return false;
		}else if(!(country.match(/^[a-zA-Z]+$/))){
			var msgbg = document.getElementsByClassName("msgbg")[0];
			var error_msg = document.getElementById("error_msg");
			error_msg.innerHTML = "<spring:message code="story.country_cannot_numeric"/>";
			msgbg.style.display = "block";
			
		    return false;
		}
		
		//city validation
		var city = document.forms["savestoryForm"]["city"].value;
		
		if (city==null || city=="") {
			return true;
		}
		else if (city.length>30) 
		{
		    var msgbg = document.getElementsByClassName("msgbg")[0];
			var error_msg = document.getElementById("error_msg");
			error_msg.innerHTML ="<spring:message code="story.city_exceed"/>";
			msgbg.style.display = "block";
			
		    return false;
		}else if(!(city.match(/^[a-zA-Z]+$/))){
			var msgbg = document.getElementsByClassName("msgbg")[0];
			var error_msg = document.getElementById("error_msg");
			error_msg.innerHTML ="<spring:message code="story.city_cannot_numeric"/>";
			msgbg.style.display = "block";
			
		    return false;
		}
		
		
		return true;
	}
	

	
function removeLocation(){
		
		document.getElementById("lattitude").value="";
		document.getElementById("longitude").value="";
	}
</script>


<script type="text/javascript">
var markers = [];
loadScript();
function removeLocation(){
	
	document.getElementById("lattitude").value="";
	document.getElementById("longitude").value="";
	removeMarker();
}
function removeMarker(){
	for(var i=0; i<markers.length; i++){
		markers[i].setMap(null);
	}
}

function initialize() {
	
	var map;
	
	
	 var mapOptions = {
			    zoom:9,
			    center: new google.maps.LatLng(63.315149771394466,30.03936767578125)
	};
	  
	  map = new google.maps.Map(document.getElementById('mappart'),
	      mapOptions);
	  lat=document.getElementById("lattitude").value;
	  lng=document.getElementById("longitude").value;
	  if(lat && lng){
		  marker = new google.maps.Marker({
	          map: map,
	          animation: google.maps.Animation.DROP,
	          title:'Click to Zoom',
	          position: new google.maps.LatLng(lat,lng),
	          
	      });  
		  markers.push(marker);
	  }
		
	  function setAllMap(map) {
		  for (var i = 0; i < markers.length; i++) {
		    markers[i].setMap(map);
		  }
		}

	  google.maps.event.addListener(map, 'click', function( event ){
		  document.getElementById("lattitude").value=event.latLng.lat();
		  document.getElementById("longitude").value=event.latLng.lng();
		  removeMarker();
		  addMarker(event.latLng);
		 
	});
		function addMarker(location) {
			var marker = new google.maps.Marker({
				position : location,
				map : map
			});
			markers.push(marker);
		}

	}


function loadScript() {
    var script = document.createElement('script');
    script.type = 'text/javascript';
    /* script.src="http://maps.googleapis.com/maps/api/js?key=AIzaSyDY0kkJiTPVd2U7aTOAwhc9ySH6oHxOIYM&sensor=false&callback=initialize"; */
  script.src = 'https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&callback=initialize'; 
    document.body.appendChild(script);
}
</script>

</head>

<body>
<div class="boxpanel storywindow">
<h1 id="popup_page"><spring:message code="acceptStory.tell_your_story"></spring:message><a href="#" class="close"></a></h1>
<div class="box-content">
	<h2 class="bord"><spring:message code="acceptStory.save_story"></spring:message></h2>
	
	<jsp:useBean id="date" class="java.util.Date"></jsp:useBean>
	<fmt:formatDate var="yearTemp"  value="${story.storyDate}" pattern="yyyy" />
	<fmt:formatDate var="monthTemp" value="${story.storyDate}" pattern="MM" />	
	<fmt:formatDate var="dayTemp" value="${story.storyDate}" pattern="dd" />
	
	<fmt:parseNumber var="day" integerOnly="true" type="number" value="${dayTemp}" /> 
    <fmt:parseNumber var="month" integerOnly="true" type="number" value="${monthTemp}" /> 
    <fmt:parseNumber var="year" integerOnly="true" type="number" value="${yearTemp}" />             
	
	<div class="row">
	 	<div class="msgbg" style="display: none;">
	    	<div class="msg_left">
		        <div class="msg_right">
		        	<div class="msg_error">
		        		<img src="${pageContext.request.contextPath}/resources/img/form/ic_error.png" alt="" onclick="closeError()"/>
		        		<span id="error_msg"></span>
		        	</div>
		        </div>
	    	</div>
	   	</div>  
 	</div>
	
	<form:form action="${pageContext.request.contextPath}/story/updateStory.html" modelAttribute="story" method="post" id="savestory" name="savestoryForm" onsubmit="validateForm();">
		<div class="contblock">
			<div class="leftpart">
		        <div class="row">
		     	
		        	<label class="inline"><spring:message code="title"></spring:message>:</label>
		        	<!-- <input name="title" type="text" class="medium" /> -->
		        	<form:input path="title" class="medium"/>
		        	<form:errors path="title" cssClass="error" />
		        </div>
		        <div class="row">
		        	<label class="inline"><spring:message code="acceptStory.country"></spring:message>: </label>
		        	<!-- <input name="country" type="text" class="medium" /> -->
		        	<form:input path="country" class="medium"/>
		        	<form:errors path="country" cssClass="error" />
		        </div>
		        
		        <div class="row">
		        	<label class="inline"><spring:message code="acceptStory.city"></spring:message>:</label>
		        	<!-- <input name="city" type="text" class="medium" /> -->
		        	<form:input path="city" class="medium"/>
		        	<form:errors path="city" cssClass="error" />
		        </div>
		        <div class="row">
		        	<label class="inline"><spring:message code="acceptStory.date"></spring:message>:</label> 
		        	
		        	<select name="storyDay">
		        	
			           <c:forEach var="i" begin="01" end="31">
			   		   		<c:choose>
			           			<c:when test="${i eq day}">
			           				<option value="${i}" selected="selected"><c:out value="${i}"/></option>
			           			</c:when>
			           			<c:otherwise>
			           				<option value="${i}"><c:out value="${i}"/></option>
			           			</c:otherwise>
			           		</c:choose>
					  </c:forEach>
			          
			        </select>
		        
			        <select name="storyMonth">
		          		
		          	
				          <c:forEach var="i" begin="01" end="12">
				          	<c:choose>
			           			<c:when test="${i eq month}">
			           			
			           				<option value="${i}" selected="selected"><c:out value="${i}"/></option>
			           			</c:when>
			           			<c:otherwise>
			           				<option value="${i}"><c:out value="${i}"/></option>
			           			</c:otherwise>
			           		</c:choose>
						  </c:forEach>
				  
		        	</select>
		        
			        <select name="storyYear">
			         
			          
			           <c:forEach var="i" begin="1800" end="2016">
			           	<c:choose>
			           		<c:when test="${i eq year}">
			           			<option value="${i}" selected="selected"><c:out value="${i}"/></option>
			           		</c:when>
			           		<c:otherwise>
			           			<option value="${i}"><c:out value="${i}"/></option>
			           		</c:otherwise>
			           	</c:choose>
					  </c:forEach>
			          
			        </select>
		        
		        </div>
		        
		        <div class="row">
		        	<label class="inline"><spring:message code="place"></spring:message>:</label>
		        	<form:input path="lattitude" class="medium" readonly="true"/>
		        </div>
		        <div class="row">
		        	<label class="inline"></label>
		        	<form:input path="longitude" class="medium" readonly="true"/>
		        </div>
		        <div class="row">
		        	<label class="inline"></label>
		        	<input type="hidden" id="zoomLevel" value="1"></input>
		        	<a href="#" onclick="removeLocation()" class="linkbtn-yellow"><spring:message code="acceptStory.remove_location"></spring:message></a>
		       	</div>
		    </div>
		 
		    <div id="mappart">
		   	</div>
		</div>
	</form:form>

    <div class="bottomrow">
    	<a href="${pageContext.request.contextPath}/story/form/editStoryForm.html" class="linkbtn-blue"><spring:message code="acceptStory.back_to_story"></spring:message></a>
    	<a href="#" onclick="saveStory();" class="linkbtn-blue"><spring:message code="save"></spring:message></a>
    </div>
</div>
</div>
</body>
</html>