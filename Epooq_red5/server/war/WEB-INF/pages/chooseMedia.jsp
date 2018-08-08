<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:genericpage title="">
	<jsp:body>
		<div id="content">
		
			<div class="container">
				<h1><spring:message code="header.choosemedia"/></h1>
			</div>
			
			<div class="container">
	        	<a href="recordVideo.html" data-transition="slide" ><img src="resources/img/moviemedia.png"></a>
	        	<img src="resources/img/picmedia.png">
	        	<img src="resources/img/penmedia.png">
	        	<img src="resources/img/soundmedia.png">
			</div>
			
			<div class="container">
	        	<a href="home.html" class="button"><spring:message code="button.stop"/></a>
	    	</div>
    	</div>
    </jsp:body>
</t:genericpage>