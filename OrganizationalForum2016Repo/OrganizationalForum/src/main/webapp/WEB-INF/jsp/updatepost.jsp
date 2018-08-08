<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>Post</title>
<link href="<%=request.getContextPath()%>/resources/css/bootstrap.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/resources/css/style.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/resources/css/menu.css" rel="stylesheet">

<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->
<script src="<%=request.getContextPath()%>/resources/js/jquery.min.js"></script>

</head>
<body>
<div id="wrap">
	<!-- Header Start -->
	<jsp:include page="header.jsp"></jsp:include>
    <!-- Header End -->
    
    <!-- Content Start -->
	<section class="page-body">
    	<ol class="breadcrumb">
          <li><a href="dashboard"><span class="glyphicon glyphicon-home"></span></a></li>
          <li><a href="#">Category 1</a></li>
        </ol>
        <div class="page-content">
        	<h2 class="title">Category 1</h2>
          	<br>
        	           
            <div class="topic-detail">            	
            	<div class="postprofile">
                  
                  <div class="pp-title">
                  <c:set var="postOwner" value="${sessionScope.currentLoggedinUser}"/>
                		<a href="#">${postOwner.username}</a>
                	<c:if test="${postOwner.role.roleid eq 1}">
                		<div>Admin</div>
                  	</c:if>
                  	<c:if test="${postOwner.role.roleid ne 1}">
                  		<div>User</div>
					</c:if>
                  </div>
                  <div class="pp-postby">Posts: 119<br> Joined : ${postOwner.createDate}</div>
                  <div class="clear"></div>
           	  </div>
              <%-- <div>${categoryID}</div> --%>
              <div class="postbody">
              		<form:form name ="addPostForm" method="post" action="updatepostsucessfull" modelAttribute="postUpdate" class="form-horizontal cus-form">
                	
              		<form:textarea path="question" id="question" rows="1" cols="100" name="question"/>
              		<br>
              		<form:textarea path="description" id="description" rows="5" cols="100" name="description"/>
              		
              		<br>
              		<form:input type="hidden" name="postid" id="postid" path="postid" />
              		<form:input type="hidden" name="userid" id="userid" path="userPost.userid" />
              		<input type="submit" name="Update" value="Update" class="btn btn-primary" /> &nbsp;
					<input name="Reset" type="reset" class="btn btn-default" value=" Reset ">
              		</form:form>
              </div>              
              <div class="clear"></div>
            </div>
            
            
       </div>        
    </section>
    <!-- Content End -->
    
    <!-- Content Start -->
	<jsp:include page="footer.jsp"></jsp:include>
    <!-- Content End -->    
</div>
<p id="back-top"><a href="#top"><span></span></a></p>


<script src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/menu.js"></script>

<!-- Back to Top Arrow Script -->
<script type="<%=request.getContextPath()%>/resources/text/javascript">
jQuery(function() {
    jQuery(window).scroll(function() {
        if (jQuery(this).scrollTop() > 100) {
            jQuery('#back-top').fadeIn();
        } else {
            jQuery('#back-top').fadeOut();
        }
    });
    jQuery('#back-top a').click(function() {
        jQuery('body,html').stop(false, false).animate({
            scrollTop: 0
        }, 800);
        return false;
    });
});
</script>
<!-- end -->



</body>
</html>