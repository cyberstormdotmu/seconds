<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:genericpage title="">
    <jsp:attribute name="header">
		<script type="text/javascript" src="../resources/js/three.js"></script>
		<script type="text/javascript" src="../resources/js/jquery.simplemodal.1.4.4.min.js"></script>
		<script type="text/javascript" src="../resources/js/minified/jquery-ui.min.js"></script>
		<spring:message code="label.communityselected" var="community_placeholder" />
    </jsp:attribute>
	<jsp:body>
	
	<input type="hidden" id="errorMessage" value="${message}"/>
	<input type="hidden" id="emailIdHidden" value="${user.email}"/>
	<div id="info">
		<p id="info-content"></p>
		<a id="info-close" href=""><img src="../resources/img/sulje.png"></a>
	</div>
	
	<div id="nav">
 		<div id="nav-slider" style="height: 200px;"></div>
 		<a id="map-button" href=""><img src="../resources/img/kartta_nappi.png"></a>
 		<a id="connection-button" href=""><img src="../resources/img/sidos_nappi.png"></a>
 	</div>

	<div id="map-layer">
		<img src="../resources/img/bg_2.png" alt="" width="100%" height="100%">
	</div>
	
	<div id="connection-layer">
		<img src="../resources/img/bg_1.png" alt="" width="100%" height="100%">
	</div>

	<div id="canvas">
	</div>
	
	<div id="loading">
		<img alt="Loading..." src="../resources/img/loading1.gif" style="position: fixed; top: 50%; left: 50%;">
	</div>
	
	<div id="loginOverlayOuter"> 
	    <div id="loginOverlayInner" style="display: none;">
	       <!-- external content to be loaded here --> 
		</div>
	</div>

	


	<div id="content-dialog" class="dialog">
		<a href="#" class="simplemodal-close close-button"><img src="../resources/img/X.png"></a>
		<div class="header">
			<h2 id="content-title"></h2>
			<p>
				<span id="content-place"></span>, <span id="content-time"></span>
			</p>
		</div>
		<div class="content">
			<p id="playerContainer">
				Alternative Content
			</p>
		</div>
	</div>
	
	<div id="community-dialog" class="dialog">
		<h2 id="community-dialog-header"><spring:message code="myepooq"/></h2>
		<div id="community-dialog-container">
		<!--<c:forEach items="${communities}" var="community">
			<div class="community">
				<button class="simplemodal-close" onclick="renderCommunity(${community.id});return false;" >
					<img src="picture.html?id=${community.pictureId}" alt="${community.name}">
				</button>
				<p id="community-dialog-name">${community.name}</p>
			</div>
		</c:forEach>-->
		</div>
		<p class="community-close-button">
			<a href="#" class="simplemodal-close button"><spring:message code="button.cancel"/></label></a>
		</p>
		<p>
			<img src="../resources/img/epooq_vip.png" width="200" height="200">
		</p>
	</div>
	
	<div id="anchor-edit-dialog" class="dialog">
		<a href="#" class="simplemodal-close close-button"><img src="../resources/img/X.png"></a>
		<form:form action="home.html" method="post" enctype="multipart/form-data">
			
			<table>
				<tr>
					<td><label for="anchor-edit-title"><spring:message code="title"/></label></td>
					<td><input type="text" id="anchor-edit-title" name="title"></td>
				</tr>
				
				<tr>
					<td><label for="anchor-edit-date"><spring:message code="date"/></label></td>
					<td><input type="text" id="anchor-edit-date" name="date" placeholder="1.12.1970"></td>
				</tr>
				
				<tr>
					<td><label for="anchor-edit-place"><spring:message code="place"/></label></td>
					<td><input type="text" id="anchor-edit-place" name="place"></td>
				</tr>

				<tr>
					<td><label for="anchor-edit-picture"><spring:message code="picture"/></label></td>
					<td><input type="file" id="anchor-edit-picture" name="file"></td>
				</tr>
				
				<tr>
					<td><label for="anchor-edit-content"><spring:message code="content"/></label></td>
					<td><textarea id="anchor-edit-content" name="content"></textarea></td>
				</tr>

				<tr>
					<td>
						<input type="hidden" name="id" id="anchor-edit-id" value="">
						<input type="hidden" name="communityId" id="anchor-edit-community-id" value="">
					</td>
					<td><button type="submit"><spring:message code="save"/></button>
				</tr>
			</table>
		</form:form>
	</div>
	
	<div id="anchor-show-dialog" class="dialog">
		<a href="#" class="simplemodal-close close-button"><img src="../resources/img/X.png"></a>
		<div class="header">
			<h2 id="anchor-show-title"></h2>
			<p>
				<span id="anchor-show-place"></span><span id="anchor-show-time"></span>
			</p>
		</div>
		<img id="anchor-show-picture" src="../resources/img/anchor.png">
		<p id="anchor-show-content"></p>
		<div id="anchor-show-edit-link-container">
			<a href="" id="anchor-show-edit-link" class="simplemodal-close button"><spring:message code="button.edit"/></label></a>
		</div>
	</div>

	
	<style>
		.dimmer{
		    background: #000; 
		    position: fixed; 
		    opacity: .5; 
		    top: 0; 
		    z-index:99;
		}
		
		#loading {
			background: #000;
			position: absolute;
			opacity: .5; 
		    top: 0; 
		    z-index:25000;
		}
		
		#loginOverlayInner{
		    position:absolute; 
		    /* top:20%;
		    left:28%; */
		    /*width:auto;
		    height:auto;*/
		    z-index:100;
		 }
	</style>
	
	<script type="text/javascript">
		
	$(document).ready(function(){
		 
		$("#loginOverlayInner").load("loginForm.html");
		window.setTimeout(alignPopupCenter, 800);
		var dimmer = document.createElement("div");
		dimmer.style.width =  window.innerWidth + 'px';
		dimmer.style.height = window.innerHeight + 'px';
		dimmer.className = 'dimmer';
		document.body.appendChild(dimmer);
		return false;
	});

	$(document)
	.ajaxStart(function (){
		// show loading div 
		$("#loading").show();
	})
	.ajaxStop(function (){
		// hide loading div
		$("#loading").hide();
	});

	function alignPopupCenter()
	{
		 var windowWidth =  $("html").width();
		 var windowHeight = $("html").height();

		 var divWidth = $("#loginOverlayInner").width();
		 var divHeight = $("#loginOverlayInner").height();

		 var top= ((windowHeight/2)- (divHeight/2));
		 var left=((windowWidth/2)- (divWidth/2));
		 
		 $("#loginOverlayInner").css('top', top);
		 $("#loginOverlayInner").css('left', left);
		 $("#loginOverlayInner").fadeIn(500);
		 $("#loginOverlayInner").show();
	}

	window.onresize = 
		function()
		{
			var dimmer = document.getElementsByClassName("dimmer")[0];
			dimmer.style.width =  window.innerWidth + 'px';
			dimmer.style.height = window.innerHeight + 'px';

			alignPopupCenter();
		};

	</script>


	</jsp:body>
</t:genericpage>