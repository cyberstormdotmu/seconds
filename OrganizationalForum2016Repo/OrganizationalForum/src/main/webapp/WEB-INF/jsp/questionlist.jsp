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
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>Post</title>
<link href="<%=request.getContextPath()%>/resources/css/bootstrap.css"
	rel="stylesheet">
<link href="<%=request.getContextPath()%>/resources/css/style.css"
	rel="stylesheet">
<link href="<%=request.getContextPath()%>/resources/css/menu.css"
	rel="stylesheet">

<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->
<script src="<%=request.getContextPath()%>/resources/js/jquery.min.js"></script>

<!-- <script>
	function doAjaxRedirect() {
		var categoryID = document.addPostCategory.categoryId.value;
		
		$.ajax({
			type:"POST",
			url:"addpost",
			data : "categoryId="+categoryID,
			success : function(response) {  
				//alert(response);   
			},  
			error : function(e) {  
			      alert('Error: ' + e);   
			}  
			   
		});
	}
</script> -->

</head>
<body>
	<div id="wrap">
		<!-- Header Start -->
		<jsp:include page="header.jsp"></jsp:include>

		<!-- Header End -->

		<!-- Content Start -->
		<section class="page-body">
		<ol class="breadcrumb">
			<li><span class="glyphicon glyphicon-home"></span></li>
			<li><a href="dashboard">Home</a></li>
			<c:if test="${!empty categoryname}">
				<li>${categoryname}</li>
			</c:if>
		</ol>
		
			
				<div class="page-content">
					<div class="buttonbar">
						<div class="left">
							<c:if test="${!empty categoryname}">
								<h3>${categoryname}</h3>
							</c:if>	
							
						</div>			
						<div class="right">
							<form:form name ="addPostCategory" method="POST" action="selectedCategory">
								<input style="display: none;" name="categoryId" type="text" id="categoryId" value="${categoryId}" >
								<input type="submit" name="Add Post" value="Add Post" class="btn btn-primary" />
							</form:form>						
						</div>
					</div>
		
				<br>
			<!-- ********************Content Part************************* -->
	
	<c:if test="${!empty quelist}">
		<c:forEach items="${quelist}" var="post">
			<div class="topic-detail" style="height: 100px">
				<div class="postprofile" style="height: 70px">
					<div class="pp-title"><strong>${post.userPost.username}</strong>
						<div>${post.userPost.role.name}</div>
						<div>Posted on : ${post.createDate}</div>
					</div>
					
					<div class="clear"></div>
				</div>
				<!-- <ul class="post-buttons">
					<li class="p-quote"><a href="#"></a></li>
					<li class="p-post"><a href="#1">#1</a></li>
				</ul> -->
				<div class="postbody">
					
						
						<a href="postdetails?postId=${post.postid}"><h5 style="width: 700px ;overflow: hidden; text-overflow: ellipsis"> Question : ${post.question}</h5></a>
					<br>
						<div class="pp-postby">
							<p style="width: 700px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;" class="bigtext"> Description : ${post.description}</p>
						</div>
					<br>

				</div>
				<div class="clear"></div>
			</div>
</c:forEach>
</c:if>








			<!-- ********************Content Part End************************* -->
		</div>
		</section>
		<!-- Content End -->

		<!-- Content Start -->
		<jsp:include page="footer.jsp"></jsp:include>
		<!-- Content End -->
	</div>
	<p id="back-top">
		<a href="#top"><span></span></a>
	</p>


	<script
		src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>
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