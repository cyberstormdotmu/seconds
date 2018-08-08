<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:genericpage title="">
    <jsp:attribute name="header">
		<script type="text/javascript">
		  $(window).keydown(function(event){
			    if(event.keyCode == 13) {
			      event.preventDefault();
			      return false;
			    }
			});
		</script>
    </jsp:attribute>

	<jsp:body>
		<div id="content">
			
			<div class="container">
				
				<h1><spring:message code="header.storyinfo"/></h1>
				
				<div class="form-container">
					<form action="setVisibility.html" method="post">
	
						<p class="input-container">
							<label for="title"><h2><spring:message code="title"/></h2></label><br>
							<input type="text" id="title" name="title" class="input" />
						</p>
						
						<p class="input-container">
							<label for="place"><h2><spring:message code="place"/></h2></label><br>
							<input type="text" id="place" name="place" class="input" />
						</p>
						
						<p class="input-container">            	
							<label for="date"><h2><spring:message code="date"/></h2></label><br>
							<input type="text" id="date" name="date" class="input" placeholder="1.12.1970" />
		    			</p>
						
						<div class="container">            	
		        			<input type="hidden" name="videoId" value="${videoId}" />
		        			<button type="submit" class="button"><spring:message code="button.save"/></button>
						</div>
					</form>
				</div>
			</div>

		</div>
    </jsp:body>
</t:genericpage>