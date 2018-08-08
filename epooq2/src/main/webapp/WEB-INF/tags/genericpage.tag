<%@tag import="java.util.List"%>
<%@tag import="java.util.ArrayList"%>
<%@tag import="fi.korri.epooq.model.User"%>
<%@tag description="Page template" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@attribute name="title" fragment="false"%>
<%@attribute name="header" fragment="true"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title>Epooq</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/reset.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/themes/base/jquery.ui.all.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/style3.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/swfobject.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/script.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/video.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/dropit/dropit.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/dropit/dropit.css"  />
<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/img/favicon.ico">
<jsp:invoke fragment="header" />
<script type="text/javascript">
	var searchString = "";
	
</script>
</head>

<body id="bodyHeader">

	<div id="header">
		<table>
			<tr>
				<td><a href="${pageContext.request.contextPath}/home.html"><img
						src="${pageContext.request.contextPath}/resources/img/epooqlogo.png"></a></td>
				<td id="logo"><a id="myepooqlink"><img
						src="${pageContext.request.contextPath}/resources/img/omaepooq.png"></a>
				</td>
				<td id="search">
					<div>
						<ul id="filterbutton">
    						<li>
    							<spring:message code="find" var="find_placeholder" />
    							
    							<input list="storyList" id="search-input"/>
    							<datalist id="storyList" >
    							    <c:forEach items="${storyList}" var="story">
    							    	<option value="${story.title}" ></option>
    							    </c:forEach> 
    							</datalist>
    							
								<a href="#" onclick="searchStoryClick();"><img src="${pageContext.request.contextPath}/resources/img/filtteri_nappi.png"></a>
								<ul>

								</ul></li>
						</ul>
					</div>
				</td>
				<c:if test="${sessionScope.userSession.id == 0}">
					<td class="login_button"><a
						href="${pageContext.request.contextPath}/admin/listMapImage.html"><spring:message code="link.administration"/></a>
					</td>
				</c:if>
				<c:choose>
					<c:when test="${sessionScope.userSession != null}">
						<td class="login_button"><a
							href="${pageContext.request.contextPath}/user/logout.html"><spring:message code="button.logout"/></a>
						</td>
					</c:when>
					<c:otherwise>
						<td class="login_button"><a
							href="${pageContext.request.contextPath}/user/loginMain.html"><spring:message code="button.login"/></a>
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
	</div>

	<jsp:doBody />

	<div id="footer" >
		<%-- <a href="user/registration.html" class="button" id="tell-story"><spring:message code="button.tellastory"/></a> --%>
		<div id="footer_left">
                    <div id="links">
                        <a id="privacy_policy_anchor" href="${pageContext.request.contextPath}/terms/privacy_policy.jsp" target="_blank"><spring:message code="link.privacy"/></a> - 
                        <a id="terms_of_use_anchor" href="${pageContext.request.contextPath}/terms/termsOfUsage.jsp" target="_blank"><spring:message code="link.terms"/></a> - 
                        <a id="about_anchor" href="${pageContext.request.contextPath}/terms/complaint_policy.jsp" target="_blank"><spring:message code="link.report"/></a>  
                    </div>
        </div>
		<% if(session.getAttribute("userSession")!=null) {%>
			<a href="${pageContext.request.contextPath}/story/add.html" class="button"><spring:message code="button.tellastory"/></a> 
		<%}else{%>
		<a class="button" id="tell-story" href="${pageContext.request.contextPath}/user/registrationMain.html"><spring:message code="button.tellastory"/></a>
		 <%} %>
		<%-- <a id="add-anchor-link" href="" onclick="addAnchor();return false" class="button"><spring:message code="button.addhappening"/></a> --%>
	</div>

</body>
</html>