<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
      <%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<t:genericpage title="">
<jsp:attribute name="header">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Epooq</title>
<link href="${pageContext.request.contextPath}/resources/css/form/form_style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resources/css/confirmbox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.confirm.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	 
	 $(".grid-block").fadeIn(500);
	 $(".grid-block").show();
});

function deleteMapImage(id, title)
{
	$.confirm({
		text: "<spring:message code="adminAlert.deleteMap"/>: " + title + " ?",
		confirm: function(button) {
			window.location.href = "deleteMapImage.html?id=" + id;
	    },
	    cancel: function(button) {
	    	return false;
	    },
	    confirmButton : "<spring:message code="button.yes" />",
		cancelButton : "<spring:message code="button.no" />"
	});
}
</script>	
</jsp:attribute>
<jsp:body>
<div class="grid-block" style="width: 80%; display:none;">
<h1 style="font-size: 30px;"><spring:message code="listMapImage.message"/></h1>
<%-- <div class="grid-top">
	
				<a href="uploadMapImage.html" class="addbtn"><img src="../resources/img/form/add-ic.png" alt="" /><spring:message code="button.add"/></a>
			
</div> --%>
<table width="100%" border="0" class="grid" cellpadding="0" cellspacing="0">
		
		<tr style="border:none;" align="right">
			<td colspan="11" class="grid_add">
				<a href="uploadMapImage.html" class="addbtn"><img src="../resources/img/form/add-ic.png" alt="" /><spring:message code="button.add"/></a>
			</td>
		</tr>
					
         <tr class="grid-title">
         
            <th align="left"><spring:message code="button.srNo"/></th>
            <th align="left"><spring:message code="title"/></th>
            <th align="left"><spring:message code="listMapImage.North-East_lat"/></th>
            <th align="left"><spring:message code="listMapImage.North-East_lng"/></th>
            <th align="left"><spring:message code="listMapImage.South-West_lat"/></th>
            <th align="left"><spring:message code="listMapImage.South-West_lng"/></th>
            <th align="left"><spring:message code="listMapImage.level"/></th>
            <th align="left"><spring:message code="listMapImage.imagePath"/></th>
            <th align="left"><spring:message code="listMapImage.enable"/></th>
            <th width="40" align="center"><spring:message code="button.edit"/></th>
            <th width="40" align="center"><spring:message code="listMapImage.delete"/></th>
            
            
          </tr>
          <tr>
          <c:set var="totalImages" value="${fn:length(mapImageList)}"/>
          <c:set var="index" value="${(currentPage - 1) * 6}" scope="page"/>
          
          <c:forEach var="map" items="${mapImageList}" varStatus="mapLoop">
          <c:set var="index" value="${index + 1}" scope="page"/>
          <c:choose>
          	<c:when test="${index % 2 eq 0}">
          		<tr class="row1">
          	</c:when>
          	<c:otherwise>
          		<tr class="row2">
          	</c:otherwise>
          </c:choose>
				
					<td align="center">${index}</td>
					<td>${map.title }</td>
					<td>${map.startLat }</td>
					<td>${map.startLong }</td>
					<td>${map.endLat }</td>
					<td>${map.endLong }</td>
					<td align="center">${map.level }</td>
					<td><a href="../${map.file }">${map.file }</a></td>
					<td align="center">
						<c:choose>
							<c:when test="${map.enableFlag }">
								<spring:message code="button.yes"/>
							</c:when>
							<c:otherwise>
								<spring:message code="button.no"/>
							</c:otherwise>
						</c:choose>
					</td>
					
					<td align="center"><a href="editMapImage.html?id=${map.id }"><img
							src="../resources/img/form/ic_edit.png" alt="" /></a></td>
					<td align="center"><img	src="../resources/img/form/ic_delete.png" alt="" onclick="deleteMapImage('${map.id}', '${map.title}')"/></td> 

		
			
			</c:forEach>		
			</tr>
          	<tr class="grid-bottom" align="right">
          		<td colspan="11">
          			
<div class="grid-bottom" style="border: none;">
		 <div class="paging" style="border: none;">
         <ul>
         <c:choose>
         	<c:when test="${currentPage eq 1 }">
         		<li><a href="#" class="prevar disable"></a></li>	
         	</c:when>
         	<c:otherwise>
         		<li><a href="listPageMapImage.html?page=${currentPage - 1 }" class="prevar"></a></li>
         	</c:otherwise>
         </c:choose>
         
         <c:forEach var="pageno" begin="1" end="${totalPages}">
         	<c:choose>
         		<c:when test="${currentPage eq pageno }">
         			<li><a href="#" class="active">${pageno }</a></li>
         		</c:when>
         		<c:otherwise>
         			<li><a href="listPageMapImage.html?page=${pageno }" >${pageno }</a></li>
         		</c:otherwise>
         	</c:choose>         	
         </c:forEach>
         
         <c:choose>
         	<c:when test="${currentPage eq totalPages }">
         		<li><a href="#" class="nxtar disable"></a></li>	
         	</c:when>
         	<c:otherwise>
         		<li><a href="listPageMapImage.html?page=${currentPage + 1 }" class="nxtar"></a></li>
         	</c:otherwise>
         </c:choose>
         
        </ul>
</div> 
</div>
          		</td>
          	</tr>
</table>


</div>
</jsp:body>
</t:genericpage>
