<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:genericpage title="">
	<jsp:body>
		<div id="content">
		
			<div class="community-container">
			
				<div class="container">
					<h1><spring:message code="header.chooseCommunity"/></h1>
				</div>
			
		        <div class="communities">
		        	<c:forEach items="${communities}" var="community">
						<div class="community">
							<form action="thankYou.html" method="post" class="community">
				        		<input type="hidden" name="community" value="${community.id}" />
				        		<input type="hidden" name="videoId" value="${videoId}">
				        		<button >
									<img src="picture.html?id=${community.pictureId}" alt="${community.name}">
								</button>
				        	</form>
							<p class="community-name">${community.name}</p>
						</div>
					</c:forEach>
		        </div>

			</div>
		        
	        <div class="container">
		        <form action="setVisibility.html" method="post">
		        	<input type="hidden" name="videoId" value="${videoId}">
		        	<button type="submit"><spring:message code="button.cancel"/></button>
		        </form>
	        </div>

		</div>
    </jsp:body>
</t:genericpage>