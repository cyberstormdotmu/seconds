<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>Epooq</title>
<link href="${pageContext.request.contextPath}/resources/css/form/form_style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/css/confirmbox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.confirm.js"></script>
<script type="text/javascript">
	$(document).ready(function()
	{
		var storyPages = document.getElementsByClassName("storypage");
		var totalStoryPages = storyPages.length;

		// set thumbnail image of story
		var story_thumbnail = document.getElementById("story_thumbnail");
		var thumbnail_found = false;
		for(var index = 0; index < storyPages.length; index++)
		{
			var image = storyPages[index].getElementsByTagName("img")[0];

			if(image!=null && image!="" && image!="[]")
			{
				story_thumbnail.src = image.src;
				thumbnail_found = true;
				break;
			}
			
		}

		if(!thumbnail_found)
		{
			story_thumbnail.src = "${pageContext.request.contextPath}"+"/resources/img/form/no_thumbnail.png";
		}

		var paging_ul = document.getElementsByClassName("paging")[0].getElementsByTagName("ul")[0];
		
		// add previous link to paging
		addPreviousLink(paging_ul);

		// add intermediate story page links to paging
		addStoryPageLinks(paging_ul, storyPages);

		// add next link to paging
		addNextLink(paging_ul, totalStoryPages);
		
	});

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

			storypage_li.appendChild(storypage_li_a);
			handleClickEvent(storypage_li_a);
			
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
			}
			else
			{
				storyPages[temp].style.display = "none";
			}
		}
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
			}
			else
			{
				storyPages[temp].style.display = "none";
			}
		}
	}

	function editStory()
	{
		var form = document.getElementById("form-submit");
		form.action = "${pageContext.request.contextPath}"+"/story/editStory.html"+"?storyId="+"${story.id}";
		form.method = "post";
		form.submit();
	}

	function removeStory()
	{
		$.confirm({
			text: "Are you sure want to remove the story?",
			confirm: function(button) {

				var form = document.getElementById("form-submit");
				form.action = "${pageContext.request.contextPath}"+"/story/removeStory.html"+"?storyId="+"${story.id}";
				form.method = "post";
				form.submit();
				
		    },
		    cancel: function(button) {
		    	return false;
		    },
		    confirmButton: "Yes",
		    cancelButton: "No"
		});
	}
	
</script>
</head>
<body>
<div class="boxpanel storywindow">
<div class="box-content">
	
	<div class="innercontent">
		<a href="#" class="close-gr" style="margin-top: 3.5%;"></a>
		
		<div class="leftpart">
			<img alt="Image 
not 
available" src="" id="story_thumbnail" height="48px;" width="50px;" style="white-space: pre;"/>
		</div>
		<div class="leftpart">
			<p>
				<h3 style="text-shadow:none; color:#6E6E6E; padding-bottom: 5px;"> <c:out value="${story.title}"></c:out> </h3>
				<fmt:formatDate value="${story.storyDate}" var="storyDateFmt" pattern="EEEE, MMMMM d, yyyy"/>
				<label> <c:out value="${storyDateFmt}"></c:out> </label>
				<br/>
				<label> <c:out value="${story.city}"></c:out> </label>&nbsp;
				<label> <c:out value="${story.country}"></c:out> </label>
			</p>
		</div>
		<div class="rightpart" style="margin-right: 3%; margin-top: 3%;">
			<p>
				<label><c:out value="${story.user.firstName}"></c:out></label>,&nbsp;
				<fmt:formatDate value="${story.user.birthDate}" var="birthYear" pattern="yyyy" />
				<jsp:useBean id="date" class="java.util.Date"/>
				<fmt:formatDate value="${date}" var="year" pattern="yyyy"/>
				<label><c:out value="${year - birthYear}"></c:out></label>
			</p>
		</div>
	</div>	
	
    <div class="innercontent" style="background: #EDEDED; border-radius: 5px;">
    
    	<form:form action="${pageContext.request.contextPath}/story/acceptStory.html" modelAttribute="story" method="post" id="acceptStory">
    	
        	<c:forEach var="storyPage" items="${story.storyPages}" varStatus="pages">
        	
				<c:if test="${fn:contains(storyPage.storyContent.type,'video')}">
					<div class="storypage">
						<div style="min-height: 400px; max-height: 400px; max-width: 810px; overflow: auto;">
							<video id="player" width="400" height="290" style="margin-top: 20px;" controls="controls">
								<source src="${pageContext.request.contextPath}/${storyPage.storyContent.content}" type="${storyPage.storyContent.type}"></source>
							</video>
							<p style="max-width: 400px; margin: 0px auto; padding-top: 10px;">
								<c:out value="${storyPage.storyContent.description}"></c:out>
							</p>
						</div>
					</div>
				</c:if>
				
				<c:if test="${fn:contains(storyPage.storyContent.type,'audio')}">
					<div class="storypage">
						<div style="min-height: 400px; max-height: 400px; max-width: 810px; overflow: auto;">
							<embed src="${pageContext.request.contextPath}/${storyPage.storyContent.content}" width="400" height="290" style="margin-top: 20px;"/>
							<p style="max-width: 400px; margin: 0px auto; padding-top: 10px;">
								<c:out value="${storyPage.storyContent.description}"></c:out>
							</p>
						</div>
					</div>
				</c:if>
				
				<c:if test="${fn:contains(storyPage.storyContent.type,'image')}">
					<div class="storypage">
						<div style="min-height: 400px; max-height: 400px; max-width: 810px; overflow: auto;">
							<img src="${pageContext.request.contextPath}/${storyPage.storyContent.content}" width="400" height="290" style="margin-top: 20px;">
							<p style="max-width: 400px; margin: 0px auto; padding-top: 10px;">
								<c:choose>
									<c:when test="${fn:contains(storyPage.storyContent.narrationAudio,'Y')}">
										<embed src="${pageContext.request.contextPath}/${storyPage.storyContent.description}" width="400" height="50" autoplay="false" />
									</c:when>
									<c:otherwise>
										<c:out value="${storyPage.storyContent.description}"></c:out>										
									</c:otherwise>
								</c:choose>
							</p>
						</div>
					</div>
				</c:if>
				
				<c:if test="${fn:contains(storyPage.storyContent.type,'TEXT')}">
					<div class="storypage">
						<div style="min-height: 400px; max-height: 400px; max-width: 810px; overflow: auto; text-align: center;">
							<form:textarea path="storyPages[${pages.index}].storyContent.content" form="acceptStory" 
											   rows="20" cols="100" style="min-height:274px; resize:none; margin-top: 30px;" class="textstory" readonly="true"/>
						</div>
					</div>
				</c:if>
				
			</c:forEach>
			
		</form:form>
    </div>

 
    <div class="bottomrow">
    
    <% if(session.getAttribute("userSession")!=null) {%>
		<a href="${pageContext.request.contextPath}/story/add.html" class="linkbtn-blue"><spring:message code="button.tellastory"/></a> 
	<%}else{%>
		<a href="${pageContext.request.contextPath}/user/registrationMain.html" class="linkbtn-blue" id="tell-story"><spring:message code="button.tellastory"/></a>
	 <%} %>
    
    <c:if test="${userSession.id eq story.user.id}">
    	<form:form action="" method="post" id="form-submit">
    		<a href="#" class="linkbtn-blue" onclick="editStory();"><spring:message code="button.edit"/></a>
    		<a href="#" class="linkbtn-blue" onclick="return removeStory();"><spring:message code="button.remove"/></a>
    	</form:form>
    </c:if>
    
    <div class="paging">
         <ul>
        </ul>
   </div>
    </div>
    
</div>
</div>
</body>
</html>