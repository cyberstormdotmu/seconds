<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:genericpage title="">
    <jsp:attribute name="header">
    		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/three.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.simplemodal.1.4.4.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/minified/jquery-ui.min.js"></script>
		
		<style>
		.dimmer{
		    background: #000; 
		    position: absolute; 
		    opacity: .5; 
		    top: 0; 
		    z-index:25000;
		}
		
		#loading {
			background: #000;
			position: absolute;
			opacity: .5; 
		    top: 0; 
		    z-index:25000;
		}
		
		#viewstoryoverlayInner{
		    position:absolute; 
		    /*top:20%;
		    left:28%;*/
		    /*width:auto;
		    height:auto;*/ 
		    z-index:26000;
		 }
		 #logoutOverlayInner{
			    position:absolute; 
			    /*top:20%;
			    left:28%;*/
			    /*width:auto;
			    height:auto;*/ 
			    z-index:26000;
		 }
		 
		</style>
		
		<spring:message code="label.communityselected" var="community_placeholder" />
		
    </jsp:attribute>
	<jsp:body>
	<form id="storyform" action="mapView.html" method="post">
		<input type="hidden" id="storyId0" name="storyId0"/>
		<input type="hidden" id="storyId1" name="storyId1"/>
		<input type="hidden" id="storyId2" name="storyId2"/>
	</form>
	<div id="info">
		<p id="info-content"></p>
		<a id="info-close" href=""><img src="resources/img/sulje.png"></a>
	</div>
	
	<!-- Navigation Slider to Render Story -->
	<div id="nav" style="">
 		<div id="nav-slider" style="height: 200px;"></div>
 		<a id="map-button" href="#" onclick=""><img src="resources/img/kartta_nappi.png"></a>
 		<!-- <a id="connection-button" href=""><img src="resources/img/sidos_nappi.png"></a> -->
 	</div>

	<div id="connection-layer">
		<img src="resources/img/bg_1.png" alt="" width="100%" height="100%">
	</div>
	
	<span id="viewstoryoverlayOuter"> 
		<span id="viewstoryoverlayInner" style="display: none;">
			<!-- external content to be loaded here --> 
		</span>
	</span>

	<div id="canvas">
	</div>

	<div id="loading">
		<img alt="Loading..." src="resources/img/loading1.gif" style="position: fixed; top: 50%; left: 50%;">
	</div>

	<span id="logoutOverlayOuter"> 
	    <span id="logoutOverlayInner" style="display: none;">
	       <!-- external content to be loaded here --> 
		</span>
	</span>


	<div id="content-dialog" class="dialog">
		<a href="#" class="simplemodal-close close-button"><img src="resources/img/X.png"></a>
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
			<img src="resources/img/epooq_vip.png" width="200" height="200">
		</p>
	</div>
	
	<div id="anchor-edit-dialog" class="dialog">
		<a href="#" class="simplemodal-close close-button"><img src="resources/img/X.png"></a>
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
		<a href="#" class="simplemodal-close close-button"><img src="resources/img/X.png"></a>
		<div class="header">
			<h2 id="anchor-show-title"></h2>
			<p>
				<span id="anchor-show-place"></span><span id="anchor-show-time"></span>
			</p>
		</div>
		<img id="anchor-show-picture" src="resources/img/anchor.png">
		<p id="anchor-show-content"></p>
		<div id="anchor-show-edit-link-container">
			<a href="" id="anchor-show-edit-link" class="simplemodal-close button"><spring:message code="button.edit"/></label></a>
		</div>
	</div>

 	<script type="text/javascript" src="resources/js/clouds.js"></script>
 	
	<script type="text/javascript">
		var videoId;
	
		// Called from flash when connected to the server
		function onConnectSuccess() {
			console.log("Connected to server");
			startPlayback(videoId);
		}
		// Called from flash when connection to the server failed
		function onConnectFailed() {
			console.log("Failed to connected to server");
		}
		// Called from flash when connection to the server closed
		function onConnectClosed() {
			console.log("Connection to server closed");
		}
		// Called from flash when playback starts
		function onPlayStart() {
			console.log("Playback started");
		}
		// Called from flash when playback stops
		function onPlayStop() {
			console.log("Playback stopped");
		}
				
		function startPlayback(videoId) {
			try {
				getFlashObject().startPlayback(videoId);
			} catch (e) {
				console.log(e);
			}				
		}
	
		function getFlashObject() {
			return $("#playRecord")[0];
		}
				
		$(document).ready(function() {
			//initFlashVideo("playRecord");
			
			var v="${flashScope.logout}";
			if(v.trim()=="true"){
				 $("#logoutOverlayInner").load("user/logoutForm.html");
				 $('#logoutOverlayInner').css({
					  margin: 'auto',
				  	  position: 'absolute',
				  	  height: '50%',
				  	  top: 0, left: 0, bottom: 0, right: 0
					});
				 window.setTimeout(alignPopupCenter, 800);
				 var dimmer = document.createElement("div");
				 dimmer.style.width =  window.innerWidth + 'px';
				 dimmer.style.height = window.innerHeight + 'px';
				 dimmer.className = 'dimmer';
				 document.body.appendChild(dimmer);
				 /* $("#errorMessageDiv").append($("#errorMessage").val()); */
				 return false;

			}
			
			
			
		});
		
		$(document).on("click", "a.close-gr", function(){
					closePopup();					
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

		function closePopup()
		{
			$(".dimmer").remove();
			$("#viewstoryoverlayInner").html("");
			$("#viewstoryoverlayInner").hide();
		}
		
		
		function alignPopupCenter()
		{
			 var windowWidth =  $("html").width();
			 var windowHeight = $("html").height();

			 $("#logoutOverlayInner").fadeIn(500);
			 $("#logoutOverlayInner").show();

			 $("#viewstoryoverlayInner").fadeIn(500);
			 $("#viewstoryoverlayInner").show();
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