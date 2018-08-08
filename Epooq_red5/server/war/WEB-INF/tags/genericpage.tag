<%@tag description="Page template" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@attribute name="title" fragment="false"%>
<%@attribute name="header" fragment="true"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title>Epooq ${title}</title>
<link rel="stylesheet" type="text/css" href="resources/css/reset.css">
<link rel="stylesheet" type="text/css" href="resources/themes/base/jquery.ui.all.css">
<link rel="stylesheet" type="text/css" href="resources/css/style3.css">
<script type="text/javascript" src="resources/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="resources/js/swfobject.js"></script>
<script type="text/javascript" src="resources/js/script.js"></script>
<script type="text/javascript" src="resources/js/video.js"></script>
<script type="text/javascript" src="resources/dropit/dropit.js"></script>
<link rel="stylesheet" type="text/css" href="resources/dropit/dropit.css"  />
<jsp:invoke fragment="header" />
<script type="text/javascript">
	var searchString = "";
</script>
</head>

<body>

	<div id="header">
		<table>
			<tr>
				<td><a href="home.html"><img src="resources/img/epooqlogo.png"></a></td>
				<td id="logo">
					<a id="myepooqlink"><img src="resources/img/omaepooq.png"></a>
				</td>
				<td id="search">
					<div>
						<ul id="filterbutton">
    						<li>
    							<spring:message code="find" var="find_placeholder" />
    						
								<a href="#"><img src="resources/img/filtteri_nappi.png"></a>
								<input type="text" id="search-input" placeholder="${find_placeholder}">
								
								<ul>
								<c:forEach items="${communities}" var="community">
									<li>
										<a href="" onclick="renderCommunity(${community.id});return false;" >
											<img src="picture.html?id=${community.pictureId}" alt="${community.name}" width="25" height="25">
											${community.name}
										</a>
									</li>
								</c:forEach>
						        </ul>
						    </li>
						</ul>
					</div>
				</td>
			</tr>
		</table>
	</div>
	
	<jsp:doBody />

	<div id="footer" >
		<a href="chooseMedia.html" class="button"><spring:message code="button.tellastory"/></a>
		<a id="add-anchor-link" href="" onclick="addAnchor();return false" class="button"><spring:message code="button.addhappening"/></a>
	</div>

</body>
</html>