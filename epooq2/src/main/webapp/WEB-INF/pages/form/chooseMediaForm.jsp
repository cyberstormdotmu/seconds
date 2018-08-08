<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Epooq</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.confirm.js"></script>
<link href="${pageContext.request.contextPath}/resources/css/form/form_style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/css/confirmbox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">

	var story_mode = null;

	$(document).ready(function()
	{	
		
		var error_message = document.getElementById("error_message");
		if(error_message.value)
		{
			var msgbg = document.getElementsByClassName("msgbg")[0];
			var error_msg = document.getElementById("error_msg");
			error_msg.innerHTML = error_message.value;
			msgbg.style.display = "block";
		}

		story_mode = document.getElementById("story_mode");
		if(story_mode.value=="EDIT")
		{	
			var storyPagesIndex = "${story.storyPages.size()}";
			document.getElementById("pageIndex").innerHTML= parseInt(storyPagesIndex) + 1;
			var backtostory_link = document.getElementById("backtostory");
			backtostory_link.href = "${pageContext.request.contextPath}/story/form/editStoryForm.html";
		}else{
			var storyPagesIndex = "${newStory.storyPages.size()}";
			document.getElementById("pageIndex").innerHTML= parseInt(storyPagesIndex) + 1;
			
		}

	});

	function closeError(){
		$(".msgbg").hide();
	}

	function addVideo() {
		var msgbg = document.getElementsByClassName("msgbg")[0];
		msgbg.style.display = "none";

		//alert("Video File Selected");
		var submitVideo = document.getElementById("submitVideo");
		if(story_mode.value=="EDIT")
		{
			submitVideo.parentNode.action = "${pageContext.request.contextPath}/story/edit/addVideo.html";
		}
		document.getElementById("submitVideo").click();
	}

	function addImage() {
		var msgbg = document.getElementsByClassName("msgbg")[0];
		msgbg.style.display = "none";

		//alert("Image File Selected");
		var submitImage = document.getElementById("submitImage");
		if(story_mode.value=="EDIT")
		{
			submitImage.parentNode.action = "${pageContext.request.contextPath}/story/edit/addImage.html";
		}
		document.getElementById("submitImage").click();
	}

	function addAudio() {
		var msgbg = document.getElementsByClassName("msgbg")[0];
		msgbg.style.display = "none";

		//alert("Image File Selected");
		var submitAudio = document.getElementById("submitAudio");
		if(story_mode.value=="EDIT")
		{
			submitAudio.parentNode.action = "${pageContext.request.contextPath}/story/edit/addAudio.html";
		}
		document.getElementById("submitAudio").click();
	}

	function addText() {
		//alert("Text File Selected");
		var submitText = document.getElementById("submitText");
		if(story_mode.value=="EDIT")
		{
			submitText.parentNode.action = "${pageContext.request.contextPath}/story/edit/addText.html";
		}
		document.getElementById("submitText").click();
	}
	
	function storyPageExists() {

		var story_mode = document.getElementById("story_mode");
		
		if(story_mode.value=="EDIT")
		{
			var storyPages = "${story.storyPages}";
		
			if (storyPages == "[]") {
				alert("No story pages exists.");
				return false;
			}
		}
		else
		{
			var storyPages = "${newStory.storyPages}";
			
			if (storyPages == "[]") {
				alert("No story pages exists.");
				return false;
			}
		}
		
	}

</script>
</head>
<body>

		 
<div class="boxpanel storywindow">
<h1 id="popup_page"><spring:message code="chooseMedia.message"></spring:message><a href="#" class="close"></a></h1>
<div class="box-content">
	
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
	<div class="instructionDiv">
		<spring:message code="chooseMedia.instruction"></spring:message>
		<br>
		<br>
		<spring:message code="chooseMedia.choose_content"></spring:message> :&nbsp<span id="pageIndex"></span>
	
	</div>
    <div class="innercontent">
    
		<div class="mediapart">
			<form:form action="${pageContext.request.contextPath}/story/addVideo.html" method="post" enctype="multipart/form-data">
				<input type="file" id="videoUpload" name="file" accept="video/*" onchange="addVideo()" style="visibility: hidden; width: 1px; height: 1px;" />
				<input type="text" id="description" name="description" style="visibility: hidden; width: 1px; height: 1px;">
				<input type="submit" id="submitVideo" value="Tallenna" style="visibility: hidden; width: 1px; height: 1px;">
			</form:form>
			<a href="#" onclick="document.getElementById('videoUpload').click(); return false;">
				<img class="form_image" src="${pageContext.request.contextPath}/resources/img/moviemedia.png">
			</a>
			
		</div>
		
		<div class="mediapart">
			<form:form action="${pageContext.request.contextPath}/story/addImage.html" method="post" enctype="multipart/form-data">
				<input type="file" id="imageUpload" name="file" accept="image/*" onchange="addImage()" style="visibility: hidden; width: 1px; height: 1px;" />
				<input type="text" id="description" name="description" style="visibility: hidden; width: 1px; height: 1px;">
				<input type="submit" id="submitImage" value="Tallenna" style="visibility: hidden; width: 1px; height: 1px;">
			</form:form>
      		<a href="#" onclick="document.getElementById('imageUpload').click(); return false;">
      			<img class="form_image" src="${pageContext.request.contextPath}/resources/img/picmedia.png">
      		</a>
		</div>
		
		<div class="mediapart">
			<form:form action="${pageContext.request.contextPath}/story/addAudio.html" method="post" enctype="multipart/form-data">
				<input type="file" id="audioUpload" name="file" accept="audio/*" onchange="addAudio()" style="visibility: hidden; width: 1px; height: 1px;" />
				<input type="text" id="description" name="description" style="visibility: hidden; width: 1px; height: 1px;">
				<input type="submit" id="submitAudio" value="Tallenna" style="visibility: hidden; width: 1px; height: 1px;">
			</form:form>
      		<a href="#" onclick="document.getElementById('audioUpload').click(); return false;">
      			<img class="form_image" src="${pageContext.request.contextPath}/resources/img/soundmedia.png">
      		</a>
		</div>
		
		<div class="mediapart">
			<form:form action="${pageContext.request.contextPath}/story/addText.html" method="post" enctype="multipart/form-data">
				<input type="text" id="text" name="text" style="visibility: hidden; width: 1px; height: 1px;">
				<input type="submit" id="submitText" value="Tallenna" style="visibility: hidden; width: 1px; height: 1px;">
			</form:form>
			<a href="#" onclick="addText(); return false;">
				<img class="form_image" src="${pageContext.request.contextPath}/resources/img/penmedia.png">
			</a>
		</div>
		
	</div>

 
    <div class="bottomrow">
    <a href="${pageContext.request.contextPath}/story/form/storyReviewForm.html" onclick="return storyPageExists()" class="linkbtn-blue" id="backtostory"><spring:message code="chooseMedia.back_to_story"></spring:message></a>
    
    </div>
    
</div>
</div>
</body>
</html>