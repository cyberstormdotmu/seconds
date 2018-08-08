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

<!-- TatvaSoft - set current user from scope -->
<c:set var="currentUser" value="${sessionScope.currentLoggedinUser}" />

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
			<li><a
				href="postlist?categoryId=${postDetails.category.categoryid}">${postDetails.category.categoryName}</a></li>
			<li>Post No : ${postDetails.postid}</li>
		</ol>
		<div class="page-content">
			<!-- <div class="buttonbar">
				<div class="left">
					<h3>Category Name</h3>
				</div>
				<div class="right">
					<a href="addpost"><input type="button" name="Add Post"
						value="Add Post" class="btn btn-primary" /></a>
				</div>
			</div> -->
			<br>
			<!-- ********************Content Part************************* -->



			<div class="topic-detail">
				<div class="postprofile">
					<div class="pp-img">
						<a href="#"><img
							src="<%=request.getContextPath()%>/resources/images/no_avatar.jpg"
							alt=""></a>
					</div>

					<div class="pp-title">
						<a href="#">${postDetails.userPost.username}</a>
						<div class="clear">${postDetails.userPost.role.name}</div>

					</div>
					<br>
					<div class="pp-postby">Posted on : ${postDetails.createDate}
					</div>
					<div class="clear"></div>
				</div>
				
				<div class="postbody">
					<div class="author">
						<h3>Question :</h3>
					</div>
					<h4>${postDetails.question}


						<c:if test="${! empty errormsg}">
							<c:out value="${errormsg}"></c:out>
						</c:if>

					</h4>
					<br>

					<p class="bigtext">${postDetails.description}</p>
					<br>

				</div>
				<div class="clear"></div>
			</div>

			<!-- Answer Update starts here -->
			<c:if test="${! empty currentUser}">
				<div class="topic-answer">
					<div class="postprofile">
						
						<div class="pp-title">
							<a href="#">Hello</a>
							<div class="clear">Hello 1</div>

						</div>
						<br>
						<div class="pp-postby">Posted on :</div>
						<div class="clear"></div>
					</div>
					
					<div class="postbody">
						<form:form name="updateReplyForm" method="post"
							action="updatereplysucessfull" modelAttribute="replyUpdate"
							class="form-horizontal cus-form">

							<form:textarea path="description"
								placeholder="Enter Your Answer Here" id="description" rows="5"
								cols="100" name="description" />
							<br>
							<form:input type="hidden" path="userPost.postid"
								value="${postDetails.postid}" />
							<form:input type="hidden" path="answerid" />

							<br>
							<input type="submit" name="Update"
								onclick="return validateanswer();" value="Update"
								class="btn btn-primary" /> &nbsp;
							<input name="Reset" type="reset" class="btn btn-default"
								value=" Reset ">

						</form:form>

						<br>

					</div>
					<div class="clear"></div>
				</div>
			</c:if>

			<!-- ********************Content Part End************************* -->
		</div>
		</section>
		<!-- Content End -->

		<!-- Footer Start -->
			<jsp:include page="footer.jsp"></jsp:include>
		<!-- Footer End -->
	</div>
	<p id="back-top">
		<a href="#top"><span></span></a>
	</p>


	<script	src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>
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

	<!-- Validation for Post -->
	<script>
		function validateanswer() {
			var answer = document.updateReplyForm.description.value;

			if (answer == null || answer == "") {

				alert("Answer can't be Null.");
				return false;

			} else if (answer.length > 5000) {

				alert("Answer must be less than 5000 character.");
				return false;

			} else {
				return true;
			}
			;
		};
	</script>
	<!-- end -->

</body>
</html>