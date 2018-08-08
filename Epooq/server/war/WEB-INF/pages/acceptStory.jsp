<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:genericpage title="">
	<jsp:body>
		<div id="content">
			
			<div class="container">
				<h1><spring:message code="header.acceptstory"/></h1>
			</div>

			<div id="player" clasS="container">
				<img class="player-graphics" src="resources/img/repeat.png"></img>
				<p id="playerContainer">
					Alternative content
				</p>
			</div>

			<div class="container">
				<a href="recordVideo.html" class="button"><spring:message code="button.again"/></a>
				&nbsp;
				<a href="#" onclick="return acceptStory();" class="button"><spring:message code="button.accept"/></a>
			</div>
			
			<form id="acceptForm" action="videoInformation.html" method="post">
				<input type="hidden" name="videoId" value="${videoId}">
			</form>
		
			<script type="text/javascript">
				var videoId = "${videoId}";
	
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
	
				function acceptStory() {
					$('#acceptForm').submit();
					
					return false;
				}
	
				function getFlashObject() {
					return document.getElementById("playRecord");
				}
				
				$(document).ready(function() {
					initFlashVideo("playRecord");
				});
			</script>
		</div>
	</jsp:body>
</t:genericpage>