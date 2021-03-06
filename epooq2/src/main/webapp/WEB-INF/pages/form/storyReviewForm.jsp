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
		
		var storyPages = document.getElementsByClassName("storypage");
		var totalStoryPages = storyPages.length;

		document.getElementById("popup_page").innerHTML = "<spring:message code="storyReview.tell_your_story"/> "+"1"+"/"+totalStoryPages;

		var paging_ul = document.getElementsByClassName("paging")[0].getElementsByTagName("ul")[0];
		
		// add previous link to paging
		addPreviousLink(paging_ul);

		// add intermediate story page links to paging
		addStoryPageLinks(paging_ul, storyPages);

		// add next link to paging
		addNextLink(paging_ul, totalStoryPages);

		// Set visible Narration tabs
		setVisibleNarrationTabs();

		//viewPage(totalStoryPages);
		var view_pageno = document.getElementById("view_pageno");
		if(view_pageno.value)
		{
			viewPage(view_pageno.value);
		}
		else
		{
			viewPage(totalStoryPages);
		}
	});

	function closeError(){
		$(".msgbg").hide();
	}

	function addPreviousLink(paging_ul)
	{
		var previous_li = document.createElement("li");
		var previous_li_a = document.createElement("a");
		previous_li_a.href = "#";
		previous_li_a.className = "prevar disable";

		previous_li.appendChild(previous_li_a);
		previous_li_a.onclick = function () {
			previousClick();
		};
		paging_ul.appendChild(previous_li);
	}

	function addStoryPageLinks(paging_ul, storyPages)
	{
		var totalStoryPages = storyPages.length;

		for(var i=0; i<totalStoryPages; i++)
		{
			var storypage_li = document.createElement("li");
			
			var storypage_li_a = document.createElement("a");
			storypage_li_a.href = "#";
			storypage_li_a.innerHTML = i+1;
			if(i==0)
			{
				storypage_li_a.className = "active";
				storyPages[i].style.display = "block";
			}
			else
			{
				storyPages[i].style.display = "none";
			}

			var storypage_li_span = document.createElement("span");
			storypage_li_span.className = "remove";

			var storypage_li_span_a = document.createElement("a");
			storypage_li_span_a.href = "#";

			var storypage_li_span_a_img = document.createElement("img");
			storypage_li_span_a_img.src = "${pageContext.request.contextPath}"+"/resources/img/form/remove.png";
			storypage_li_span_a_img.alt = "";

			// append child elements
			storypage_li_span_a.appendChild(storypage_li_span_a_img);
			storypage_li_span.appendChild(storypage_li_span_a);
			storypage_li.appendChild(storypage_li_a);
			storypage_li.appendChild(storypage_li_span);
			handleClickEvent(storypage_li_a);
			handleCancelEvent(storypage_li_span_a);
			
			paging_ul.appendChild(storypage_li);
		}
	}

	function handleClickEvent(storypage_li_a)
	{
		storypage_li_a.onclick = function() 
		{
			viewPage(this.innerHTML);
		}
	}

	function handleCancelEvent(storypage_li_span_a)
	{
		storypage_li_span_a.onclick = function()
		{
			var pageNo = this.parentNode.parentNode.firstChild.innerHTML;

			$.confirm({
				text: "Are you sure you want to remove this Story Page?",
				confirm: function(button) {
					
					cancelPage(pageNo, storypage_li_span_a);

			    },
			    cancel: function(button) {
			    },
			    confirmButton: "Yes",
			    cancelButton: "No"
			});
		}
	}

	function addNextLink(paging_ul, totalStoryPages)
	{
		var next_li = document.createElement("li");
		var next_li_a = document.createElement("a");
		next_li_a.href = "#";
		
		if(totalStoryPages>1)
		{
			next_li_a.className = "nxtar";
		}
		else
		{
			next_li_a.className = "nxtar disable";
		}

		next_li.appendChild(next_li_a);
		next_li_a.onclick = function () {
			nextClick();
		};
		paging_ul.appendChild(next_li);
	}

	function nextClick()
	{
		//alert("Next Array function called");
		var storyPages = document.getElementsByClassName("storypage");
		var activeLink = document.getElementsByClassName("active")[0];
		activeLink.className = "";
		var i = activeLink.innerHTML;
		var lists = document.getElementsByClassName("paging")[0]
		   					.getElementsByTagName("ul")[0]
		   					.getElementsByTagName("li");  
		
		var maxLists = lists.length - 2;
		
		for(var temp=0; temp<maxLists; temp++)
		{
			// make the next div's display inline-block and none for other divs			
			if(temp==i)
			{
				$(".storypage:eq("+temp+")").fadeIn(500).show();
				
				var list_a = lists[temp+1].getElementsByTagName("a")[0];
				list_a.className = "active";

				// enable-disable previous link
				if(temp!=0)
				{
					// enable previous link if current page is not first
					var previous_link = document.getElementsByClassName("prevar")[0];
					previous_link.className = "prevar";
				}
				else
				{
					// disable previous link if current pqage is first
					var previous_link = document.getElementsByClassName("prevar")[0];
					previous_link.className = "prevar disable";
				}

				// enable-disable next link
				if(temp==maxLists-1)
				{
					// disable next link if current page is last
					var previous_link = document.getElementsByClassName("nxtar")[0];
					previous_link.className = "nxtar disable";
				}
				else
				{
					// enable next link if current page is not last
					var previous_link = document.getElementsByClassName("nxtar")[0];
					previous_link.className = "nxtar";
				}

				// set heading
				document.getElementById("popup_page").innerHTML = "<spring:message code="storyReview.tell_your_story"/> "+(temp+1)+"/"+storyPages.length;
			}
			else
			{
				storyPages[temp].style.display = "none";
			}
		}
	}

	function previousClick()
	{
		//alert("Previous Array function called");
		var storyPages = document.getElementsByClassName("storypage");
		var activeLink = document.getElementsByClassName("active")[0];
		activeLink.className = "";
		var i = activeLink.innerHTML;
		var lists = document.getElementsByClassName("paging")[0]
		   					.getElementsByTagName("ul")[0]
		   					.getElementsByTagName("li");  
		
		var maxLists = lists.length - 2;

		for(var temp=0; temp<maxLists; temp++)
		{
			// make the previous div's display inline-block and none for other divs			
			if(temp==i-2)
			{
				$(".storypage:eq("+temp+")").fadeIn(500).show();
				
				var list_a = lists[temp+1].getElementsByTagName("a")[0];
				list_a.className = "active";

				// enable-disable previous link
				if(temp!=0)
				{
					// enable previous link if current page is not first
					var previous_link = document.getElementsByClassName("prevar")[0];
					previous_link.className = "prevar";
				}
				else
				{
					// disable previous link if current pqage is first
					var previous_link = document.getElementsByClassName("prevar")[0];
					previous_link.className = "prevar disable";
				}

				// enable-disable next link
				if(temp==maxLists-1)
				{
					// disable next link if current page is last
					var previous_link = document.getElementsByClassName("nxtar")[0];
					previous_link.className = "nxtar disable";
				}
				else
				{
					// enable next link if current page is not last
					var previous_link = document.getElementsByClassName("nxtar")[0];
					previous_link.className = "nxtar";
				}

				// set heading
				document.getElementById("popup_page").innerHTML = "<spring:message code="storyReview.tell_your_story"/> "+(temp+1)+"/"+storyPages.length;
			}
			else
			{
				storyPages[temp].style.display = "none";
			}
		}
	}

	function cancelPage(pageNo, storypage_li_span_a)
	{
		window.location.href = "${pageContext.request.contextPath}"+"/story/cancelStoryPage.html"+"?pageNo="+pageNo;

		return true; 
	}

	function viewPage(pageNo)
	{
		var storyPages = document.getElementsByClassName("storypage");
		var activeLink = document.getElementsByClassName("active")[0];
		activeLink.className = "";
		var i = activeLink.innerHTML;
		var lists = document.getElementsByClassName("paging")[0]
		   					.getElementsByTagName("ul")[0]
		   					.getElementsByTagName("li");

		var maxLists = lists.length - 2;

		for(var temp=0; temp<maxLists; temp++)
		{
			// make the clicked div's display inline-block and none for other divs
			if(temp==pageNo-1)
			{
				$(".storypage:eq("+temp+")").fadeIn(500).show();

				var list_a = lists[temp+1].getElementsByTagName("a")[0];
				list_a.className = "active";

				// enable-disable previous link
				if(temp!=0)
				{
					// enable previous link if current page is not first
					var previous_link = document.getElementsByClassName("prevar")[0];
					previous_link.className = "prevar";
				}
				else
				{
					// disable previous link if current pqage is first
					var previous_link = document.getElementsByClassName("prevar")[0];
					previous_link.className = "prevar disable";
				}

				// enable-disable next link
				if(temp==maxLists-1)
				{
					// disable next link if current page is last
					var previous_link = document.getElementsByClassName("nxtar")[0];
					previous_link.className = "nxtar disable";
				}
				else
				{
					// enable next link if current page is not last
					var previous_link = document.getElementsByClassName("nxtar")[0];
					previous_link.className = "nxtar";
				}

				// set heading
				document.getElementById("popup_page").innerHTML = "<spring:message code="storyReview.tell_your_story"/> "+(temp+1)+"/"+storyPages.length;
			}
			else
			{
				storyPages[temp].style.display = "none";
			}
		}
	}

	function finish()
	{
		var textstories = document.getElementsByClassName("textstory");
		var removepageid = [];
		var index=0;
		var messagetext;
		
		for(var i=0; i<textstories.length; i++)
		{
			var textstory = textstories[i].value;
			if(!textstory)
			{
				var textstorypagediv = textstories[i].parentNode.parentNode;
				var pageNo = $(".storypage").index(textstorypagediv) + 1;
				removepageid[index] = pageNo;
				index++;
			}
		}

		if(index>0)
		{
			if(index==1)
			{
				messagetext = "Story page "+removepageid.toString()+" is empty, so it will be discarded.<br />Are you sure?";
			}
			else
			{
				messagetext = "Story pages "+removepageid.toString()+" are empty, so they will be discarded.<br />Are you sure?";
			}

			$.confirm({
				text: messagetext,
				confirm: function(button) {

					var form = document.getElementById("acceptStory");
					form.action = "${pageContext.request.contextPath}"+"/story/acceptStory.html";
					form["removePageNo"].value = removepageid.toString();
					form.submit();
					
			    },
			    cancel: function(button) {
			    	return false;
			    },
			    confirmButton: "Yes",
			    cancelButton: "No"
			});
		}
		else
		{
			var form = document.getElementById("acceptStory");
			form.action = "${pageContext.request.contextPath}"+"/story/acceptStory.html";
			form["removePageNo"].value = removepageid.toString();
			form.submit();
		}
	}

	function addStoryPage()
	{
		var textstories = document.getElementsByClassName("textstory");
		var removepageid = [];
		var index=0;
		var messagetext;
		
		for(var i=0; i<textstories.length; i++)
		{
			var textstory = textstories[i].value;
			if(!textstory)
			{
				var textstorypagediv = textstories[i].parentNode.parentNode;
				var pageNo = $(".storypage").index(textstorypagediv) + 1;
				removepageid[index] = pageNo;
				index++;
			}
		}

		if(index>0)
		{
			var result;
			if(index==1)
			{
				messagetext = "Story page "+removepageid.toString()+" is empty, so it will be discarded.<br />Are you sure?";
			}
			else
			{
				messagetext = "Story pages "+removepageid.toString()+" are empty, so they will be discarded.<br />Are you sure?";
			}

			$.confirm({
				text: messagetext,
				confirm: function(button) {

					var form = document.getElementById("acceptStory");
					form.action = "${pageContext.request.contextPath}"+"/story/form/chooseMediaForm.html";
					form.method = "get";
					form["removePageNo"].value = removepageid.toString();
					form.submit();
					
			    },
			    cancel: function(button) {
			    	return false;
			    },
			    confirmButton: "Yes",
			    cancelButton: "No"
			});
		}
		else
		{
			var form = document.getElementById("acceptStory");
			form.action = "${pageContext.request.contextPath}"+"/story/form/chooseMediaForm.html";
			form.method = "get";
			form["removePageNo"].value = removepageid.toString();
			form.submit();
		}
	}

	function changeVideo()
	{
		//alert("Video File Selected");
		var form = document.getElementById("acceptStory");
		form.action = "${pageContext.request.contextPath}"+"/story/changeVideo.html";
		form.method = "post";
		form.submit();
	}

	function changeImage()
	{
		//alert("Video File Selected");
		var form = document.getElementById("acceptStory");
		form.action = "${pageContext.request.contextPath}"+"/story/changeImage.html";
		form.method = "post";
		form.submit();
	}

	function changeAudio()
	{
		//alert("Video File Selected");
		var form = document.getElementById("acceptStory");
		form.action = "${pageContext.request.contextPath}"+"/story/changeAudio.html";
		form.method = "post";
		form.submit();
	}

	function addNarration()
	{
		//alert("Video File Selected");
		var form = document.getElementById("acceptStory");
		form.action = "${pageContext.request.contextPath}"+"/story/addNarration.html";
		form.method = "post";
		form.submit();
	}

	function setVisibleNarrationTabs()
	{
		var tables = $('[id^="table"]');
		for(var i=0; i<tables.length; i++)
		{
			var table_id = "table_"+i;
			if($(tables[i]).find("embed").length > 0)
			{
				ManageTabPanelDisplay( table_id,'tab1focus','tab2ready','content1' );
			}
			else
			{
				ManageTabPanelDisplay( table_id, 'tab1ready','tab2focus','content2' );
			}
		}
	}

	function switchTabPanelDisplay()
	{
		var lastArgument = arguments[arguments.length-1];
		var table_id = arguments[0];
		var temp = table_id.substring(table_id.indexOf('_')+1);
		var passThis = this;
		var passarguments = arguments;

		$.confirm({
			text: "Current content will be removed. Are you sure?",
			confirm: function(button) {		
			
				if(lastArgument == "content1")
				{
					$("#narration_"+temp).val('Y');
					$("#"+table_id).find("textarea").val("");
				}
				else if(lastArgument == "content2")
				{
					$("#narration_"+temp).val('N');
					$("#"+table_id).find("embed").remove();
					$("#"+table_id).find("textarea").val("");
				}
				ManageTabPanelDisplay.apply(passThis, passarguments);
				
		    },
		    cancel: function(button) {
		    	return;
		    },
		    confirmButton: "Yes",
		    cancelButton: "No"
		});
		
		/*if(r==false)
		{
			return;
		}
		else
		{
		
			if(lastArgument == "content1")
			{
				$("#narration_"+temp).val('Y');
				$("#"+table_id).find("textarea").val("");
			}
			else if(lastArgument == "content2")
			{
				$("#narration_"+temp).val('N');
				$("#"+table_id).find("embed").remove();
				$("#"+table_id).find("textarea").val("");
			}
			ManageTabPanelDisplay.apply(this, arguments);
		}*/

	}

	function ManageTabPanelDisplay() 
	{
		var idlist = new Array('table_id','tab1focus', 'tab2focus', 'tab1ready', 'tab2ready', 'content1', 'content2');
		
		if (arguments.length < 1) 
		{
			return;
		}

		var table = $("#"+arguments[0]);
		
		for ( var i = 1; i < idlist.length; i++) 
		{
			var block = false;
			for ( var ii = 1; ii < arguments.length; ii++) 
			{
				if (idlist[i] == arguments[ii]) 
				{
					block = true;
					break;
				}
			}
			if (block) 
			{
				$(table).find("#"+idlist[i]).css("display","block");
			} 
			else 
			{
				$(table).find("#"+idlist[i]).css("display","none");
			}
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
<h1 id="popup_page"><spring:message code="storyReview.tell_your_story"/> 1/1</h1>
<a href="#" class="close" style="margin-top: 3.5%;"></a>
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
 	
    <div class="innercontent">
    
    	<form:form action="${pageContext.request.contextPath}/story/acceptStory.html" modelAttribute="newStory" method="post" id="acceptStory" enctype="multipart/form-data">
    	
    		<input type="submit" style="visibility: hidden; display: none;"/>
    		<input type="text" style="visibility: hidden;  display: none;" name="removePageNo" />
    		<input type="file" id="videoUpload" name="videoFile" accept="video/*" onchange="changeVideo()" style="visibility: hidden; display: none;" />
    		<input type="file" id="imageUpload" name="imageFile" accept="image/*" onchange="changeImage()" style="visibility: hidden; display: none;" />
    		<input type="file" id="audioUpload" name="audioFile" accept="audio/*" onchange="changeAudio()" style="visibility: hidden; display: none;" />
    		<input type="file" id="narrationUpload" name="narrationFile" accept="audio/*" onchange="addNarration()" style="visibility: hidden; display: none;" />
    		<input type="hidden" name="storyPageListIndex" id="listIndex" value="" />
    	
        	<c:forEach var="storyPage" items="${newStory.storyPages}" varStatus="pages">
			
				<c:if test="${fn:contains(storyPage.storyContent.type,'video')}">
					<div class="storypage">
						<div class="leftpart" >
							<h2 style="text-align: left; text-transform: none;"><spring:message code="storyReview.description"/>:</h2>
							<p>
								<form:textarea path="storyPages[${pages.index}].storyContent.description" rows="10" cols="48" maxlength="500" form="acceptStory" style="resize:vertical; max-height:248px;"/>
							</p>
						</div>
						<div class="rightpart">
							<video id="player" width="400" height="290" controls="controls">
								<source src="${pageContext.request.contextPath}/${storyPage.storyContent.content}" type="${storyPage.storyContent.type}"></source>
							</video>
							<div style="text-align: left; margin-top: 2%;">
								<a class="linkbtn-yellow" href="#" onclick="document.getElementById('listIndex').value=${pages.index}; document.getElementById('videoUpload').click(); return false;"><spring:message code="storyReview.change_video"/></a>
							</div>
						</div>
					</div>
				</c:if>
				
				<c:if test="${fn:contains(storyPage.storyContent.type,'audio')}">
					<div class="storypage">
						<div class="leftpart">
							<h2 style="text-align: left; text-transform: none;"><spring:message code="storyReview.description"/>:</h2>
							<p>
								<form:textarea path="storyPages[${pages.index}].storyContent.description" rows="10" cols="48" maxlength="500" form="acceptStory" style="resize:vertical; max-height:248px;"/>
							</p>
						</div>
						<div class="rightpart">
							<embed src="${pageContext.request.contextPath}/${storyPage.storyContent.content}" width="400" height="290" autoplay="false" />
							<div style="text-align: left; margin-top: 2%;">
								<a class="linkbtn-yellow" href="#" onclick="document.getElementById('listIndex').value=${pages.index}; document.getElementById('audioUpload').click(); return false;"><spring:message code="storyReview.change_audio"/></a>
							</div>
						</div>
					</div>
				</c:if>
				
				<c:if test="${fn:contains(storyPage.storyContent.type,'image')}">
					<div class="storypage">
						<div class="leftpart">
							<form:hidden path="storyPages[${pages.index}].storyContent.narrationAudio" id="narration_${pages.index}"/>
							 <table border="0" cellpadding="0" cellspacing="0" align="center" id="table_${pages.index}">
								<tr>
									<td>
										<div id="tab1focus" class="tab tabfocus" style="display: block;"><spring:message code="storyReview.add_narration/audio"/></div>
										<div id="tab1ready" class="tab tabhold" style="display: none;">
											<div onclick="switchTabPanelDisplay('table_${pages.index}','tab1focus','tab2ready','content1')"><spring:message code="storyReview.add_narration/audio"/></div>
										</div>
									</td>
									<td class="gap" style="width: 10px;"></td>
									<td>
										<div id="tab2focus" class="tab tabfocus" style="display: none;"><spring:message code="storyReview.add_description"/></div>
										<div id="tab2ready" class="tab tabhold" style="display: block;">
											<div onclick="switchTabPanelDisplay('table_${pages.index}', 'tab1ready','tab2focus','content2')"><spring:message code="storyReview.add_description"/></div>
										</div>
									</td>
									<td>
								</tr>
								<tr>
									<td colspan="6">
										<div id="content1" class="tabcontent" style="display: block;">
											<a class="linkbtn-yellow" href="#" onclick="document.getElementById('listIndex').value=${pages.index}; document.getElementById('narrationUpload').click(); return false;"><spring:message code="storyReview.add_narration/audio"/></a>
											<c:if test="${fn:contains(storyPage.storyContent.narrationAudio,'Y')}">
												<embed src="${pageContext.request.contextPath}/${storyPage.storyContent.description}" width="250" height="150" autoplay="false" />
											</c:if>
										</div>
										<div id="content2" class="tabcontent" style="display: none;">
											<p>
												<form:textarea path="storyPages[${pages.index}].storyContent.description" rows="10" cols="42" maxlength="500" form="acceptStory" style="resize:vertical; max-height:220px;"/>
											</p>
										</div>
									</td>
								</tr>
							</table>
						
							<!-- <h2 style="text-align: left; text-transform: none;">Description:</h2>
							<p>
								<form:textarea path="storyPages[${pages.index}].storyContent.description" rows="10" cols="48" maxlength="500" form="acceptStory" style="resize:vertical; max-height:248px;"/>
							</p> -->
						</div>
						<div class="rightpart">
							<img src="${pageContext.request.contextPath}/${storyPage.storyContent.content}" width="400" height="290">
							<div style="text-align: left; margin-top: 2%;">
								<a class="linkbtn-yellow" href="#" onclick="document.getElementById('listIndex').value=${pages.index}; document.getElementById('imageUpload').click(); return false;"><spring:message code="storyReview.change_image"/></a>
							</div>
						</div>
					</div>
				</c:if>
				
				<c:if test="${fn:contains(storyPage.storyContent.type,'TEXT')}">
					<div class="storypage">
						<p>
							<form:textarea path="storyPages[${pages.index}].storyContent.content" form="acceptStory" 
										   rows="16" cols="100" style="min-height:327px; resize:none;" class="textstory"/>
						</p>
					</div>
				</c:if>
				
			</c:forEach>
			
		</form:form>
    </div>

 
    <div class="bottomrow">
    
    <a href="#" onclick="addStoryPage();" class="linkbtn-blue"><spring:message code="storyReview.add_story_page"/></a>
    <a href="#" onclick="finish();" class="linkbtn-blue"><spring:message code="storyReview.finish"/></a>
    
    <div class="paging">
         <ul>
        </ul>
   </div>
    </div>
    
</div>
</div>
</body>
</html>