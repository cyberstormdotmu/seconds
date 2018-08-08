<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:genericpage title="">
	<jsp:body>
		<div id="content">

			<div class="container visibility">
				<h1><spring:message code="header.visibility"/></h1>
				<h3><spring:message code="header.visibility.sub"/></h3>
	    	</div>
			
		    <form class="container" action="chooseCommunity.html" method="post" >
		        	<input type="hidden" name="videoId" value="${videoId}">
		        	<button type="submit"><spring:message code="button.private"/></button> 
		        </form>
		        
		    <form class="container" action="thankYou.html" method="post" >
				<input type="hidden" name="community" value="" />
		        <input type="hidden" name="videoId" value="${videoId}">
		        <button type="submit"><spring:message code="button.public"/></button>
			</form>

	    </div>
    </jsp:body>
</t:genericpage>