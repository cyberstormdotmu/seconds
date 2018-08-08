<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:genericpage title="">
	<jsp:body>
		<div id="content">
			
			<div class="container">
				<h1><spring:message code="header.startstory"/></h1>
			</div>

			<div id="player" class="container">
				<img class="player-graphics" src="resources/img/recording.png"></img>
				<p id="playerContainer">Alternative content</p>
			</div>
			
			<div class="container">
				<a href="#" id="finishRecording" onclick="return finishRecording();" class="button"><spring:message code="button.ready"/></a>
			</div>
		
			<form id="finishForm" action="acceptStory.html" method="post">
				<input type="hidden" name="videoId" value="${videoId}">
			</form>

			<script type="text/javascript">
				var videoId = "${videoId}";
	
				// Called from flash when connected to the server
				function onConnectSuccess() {
					console.log("Connected to server");
					openCamera();
				}
				// Called from flash when connection to the server failed
				function onConnectFailed() {
					console.log("Failed to connected to server");
				}
				// Called from flash when connection to the server closed
				function onConnectClosed() {
					console.log("Connection to server closed");
				}
				// Called from flash when the recording is ready to start
				function onRecordReady() {
					console.log("Recording ready");
					startRecording();
				}
				// Called from flash when the recording starts
				function onRecordStart() {
					console.log("Recording started");
				}
				// Called from flash when the recording ends
				function onRecordStop() {
					console.log("Recording stopped");
					$('#finishForm').submit();
				}
				// Called from flash when playback starts
				function onPlayStart() {
					console.log("Playback started");
				}
				// Called from flash when playback stops
				function onPlayStop() {
					console.log("Playback stopped");
				}
				
				function openCamera() {
					try {
						getFlashObject().openCamera();
					} catch (e) {
						console.log(e);
					}				
				}
	
				function startRecording() {
					try {
						getFlashObject().startRecording(videoId);
					} catch (e) {
						console.log(e);
					}
					
					return false;
				}
	
				function finishRecording() {
					try {
						getFlashObject().stopRecording();
					} catch (e) {
						console.log(e);
					}
					
					return false;
				}
	
				function getFlashObject() {
					return $("#record")[0];
				}
				
				$(document).ready(function() {
					initFlashVideo("record");
				});
			</script>
		</div>
	</jsp:body>
</t:genericpage>