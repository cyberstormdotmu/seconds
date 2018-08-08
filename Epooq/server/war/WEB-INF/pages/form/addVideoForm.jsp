<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Epooq</title>
<link href="${pageContext.request.contextPath}/resources/css/form/form_style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/css/confirmbox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.confirm.js"></script>
<script type="text/javascript">



var videoId = "${videoId}";



function addStoryPage()
{
		var form = document.getElementById("recordVideo");
		form.action = "${pageContext.request.contextPath}"+"/story/form/chooseMediaForm.html";
		form.method = "get";
		form.submit();
}

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
	$('#videoId').val(videoId);

	var story_mode = document.getElementById("story_mode");
	if(story_mode.value=="EDIT")
	{
		document.getElementById("recordVideo").action = "${pageContext.request.contextPath}/story/edit/addWebcamVideo.html";
	}else if($("#pageIndex").val().trim()!=""){
		document.getElementById("recordVideo").action = "${pageContext.request.contextPath}/story/changeWebcamVideo.html";
	}else{
		document.getElementById("recordVideo").action = "${pageContext.request.contextPath}/story/addWebcamVideo.html";
	}
	
	$('#recordVideo').submit();
	
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

function addVideo() {
 	var msgbg = document.getElementsByClassName("msgbg")[0];
	msgbg.style.display = "none";

	//alert("Video File Selected");
	var submitVideo = document.getElementById("submitVideo");
	if(story_mode.value=="EDIT")
	{
		document.getElementById("recordVideo").action = "${pageContext.request.contextPath}/story/edit/addVideo.html";
	}else if($("#pageIndex").val().trim()!=""){
		document.getElementById("recordVideo").action = "${pageContext.request.contextPath}/story/changeVideo.html";
	}else{
		document.getElementById("recordVideo").action = "${pageContext.request.contextPath}/story/addVideo.html";
	}
	$('#recordVideo').submit();
}

	$(document).ready(function()
	{
		$("#pageIndex").val($("#viewPageNo").val());
		initFlashVideo("record");
		var error_message = document.getElementById("error_message");
		if(error_message.value)
		{
			var msgbg = document.getElementsByClassName("msgbg")[0];
			var error_msg = document.getElementById("error_msg");
			error_msg.innerHTML = error_message.value;
			msgbg.style.display = "block";
		}
		
		var storyPages = document.getElementsByClassName("storypage");
		var totalStoryPages = storyPages.length;

		//document.getElementById("popup_page").innerHTML = "Tell your story: page "+"1"+"/"+totalStoryPages;

	});

	function closeError(){
		$(".msgbg").hide();
	}

	function restart()
	{
		try {
			getFlashObject().stopRecording();
		} catch (e) {
			console.log(e);
		}
				
		//$("#recordVideo").method = "POST";
		 //getFlashObject().stopRecording();
		//document.getElementById("recordVideo").action = "${pageContext.request.contextPath}/story/edit/addWebcamVideo.html";
		var story_mode = document.getElementById("story_mode");
		if(story_mode.value=="EDIT")
		{
			window.location.replace("${pageContext.request.contextPath}/story/edit/addVideoForm.html");
		}else{
			window.location.replace("${pageContext.request.contextPath}/story/addVideoForm.html");
		}
		
	}
	function finish()
	{
		try {
			getFlashObject().stopRecording();
		} catch (e) {
			console.log(e);
		}
		
	}

</script>
<style type="text/css">
.tab {
	font-family: verdana, sans-serif;
	font-size: 14px;
	width: 100px;
	white-space: nowrap;
	text-align: center;
	border: medium none;
    border-top-left-radius: 3px;
    border-top-right-radius: 3px;
	padding: 7px 3px 8px 3px;
	cursor: pointer;
}

.tabhold {
	background-color: white;
	color: black;
	font-weight: bold;
	width: 170px;
}

.tabfocus { 
	background-color: #3b92b5;
	font-weight: bold;
	color: white;
	width: 170px;
}

.tabcontent {
	font-family: sans-serif;
	font-size: 14px;
	width: 340px;
	height: 243px;
	border-color: #3b92b5;
	border-radius: 3px;
	border-style: solid;
	border-width: 2px;
	padding-top: 15px;
	padding-left: 10px;
	padding-right: 10px;
}
</style>
</head>
<body>
<div class="boxpanel storywindow">
<h1 id="popup_page">
	<spring:message code="button.tellastory"></spring:message>
	<a href="#" class="close"></a>
</h1>
<div class="box-content">
	<div class="row">
	 	<div class="msgbg" style="display: none;">
	    	<div class="msg_left">
		        <div class="msg_right">
		        	<div class="msg_error">
		        		<img src="../resources/img/form/ic_error.png" alt="" onclick="closeError()"/>
		        		<span id="error_msg"></span>
		        	</div>
		        </div>
	    	</div>
	   	</div>  
 	</div>
 	
	<form:form id="recordVideo"  method="post" enctype="multipart/form-data">
		<input type="hidden" id="videoId" name="videoId">
		<input type="hidden" id="pageIndex" name="pageIndex">
    		<div class="innercontent">
    			<div class="storypage" style="display: block; text-align: center;">
						 <%-- <div class="leftpart" style="height: 100%">
							<div style="text-align: left; margin-top: 2%;">
									<a class="linkbtn-yellow" href="#" onclick="document.getElementById('videoUpload').click(); return false;"><spring:message code="button.fromYourCom"></spring:message></a>
									<input type="file" id="videoUpload" name="file" accept="video/*" onchange="addVideo()" style="visibility: hidden; width: 1px; height: 1px;" />
									<input type="text" id="description" name="description" style="visibility: hidden; width: 1px; height: 1px;">
									<input type="submit" id="submitVideo" value="Tallenna" style="visibility: hidden; width: 1px; height: 1px;">
							</div>
							
							<div style="text-align: left; margin-top: 100%;">
								<a class="linkbtn-yellow" href="#" onclick="restart();"><spring:message code="button.restart"></spring:message></a><br>
								<a class="linkbtn-yellow" href="#" onclick="finish();"><spring:message code="button.stop"/></a>
							</div>
							
						</div>
						<div  class="rightpart" style="width: 65%; ">
							<div id="player" class="container" style="display: block; margin-top: -21px;">
									<img class="player-graphics" src="../resources/img/recording.png"></img>
									<p id="playerContainer">Alternative content</p>
								</div>
							</div>
						</div> --%> 
						 <div id="player" class="container" style="display: block; margin-top: -21px;" >
									<img class="player-graphics" src="../resources/img/recording.png" style="background: #B0B0B0;"></img>
									<p id="playerContainer">Alternative content</p>
								</div>
						</div>
								<a class="linkbtn-blue" href="#" onclick="finish();" style="margin-left: 45%; margin-top: 2%;"><spring:message code="button.stop"/></a>
				</div>
	</form:form>

    <div class="bottomrow" style="border-top: none;">
    
    <a href="#" onclick="addStoryPage();" class="linkbtn-blue" style="display: none;"><spring:message code="button.backToChooseMedia"/></a>
    
    <div class="paging">
	         <ul>
	        </ul>
	  </div>
    </div>


    	
</div>

 
    
</div>
</div>
</body>
</html>