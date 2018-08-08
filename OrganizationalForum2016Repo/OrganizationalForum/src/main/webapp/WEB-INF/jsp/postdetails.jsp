<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
<c:set var="currentUser" value="${sessionScope.currentLoggedinUser}"/>

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
			<li><a href="postlist?categoryId=${postDetails.category.categoryid}">${postDetails.category.categoryName}</a></li>
			<li>Post No : ${postDetails.postid}</li>
		</ol>
		<div class="page-content">
			
			<br>
			<!-- ********************Content Part************************* -->



			<div class="topic-detail">
				<div class="postprofile">
					<c:if test="${!empty postDetails}">
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
						<div class="pp-postby">Posted on : ${postDetails.createDate}</div>
						<c:if test="${not empty postDetails.lastModified}">
							<div class="pp-postby">Last Modified : ${postDetails.lastModified}</div>
						</c:if>
					</c:if>
					<div class="clear"></div>
				</div>
				
				<div class="postbody">
					<div class="author">
						<h3>Question :</h3>
					</div>
					<h4 style="overflow-wrap: break-word;">${postDetails.question}


						<c:if test="${! empty errormsg}">
							<c:out value="${errormsg}"></c:out>
						</c:if>

					</h4>
					<br>
					
					<p style="overflow-wrap: break-word;">${postDetails.description}</p>
					<br>

				</div>
				<div class="clear"></div>
			</div>

<!-- ******************** Answer List Start here ************************ -->
	
	<c:if test="${empty currentUser}">
			<div class="buttonbar">
						<div class="left">
							<h4><c:out value="You must need to Login for Post Question."></c:out></h4>
							
						</div>			
						<div class="right">
							<form:form name ="LoginRedirect" method="POST" action="loginRedirect">
								<input type="submit" name="Login" value="Login" class="btn btn-primary" />
							</form:form>						
						</div>
					</div>
		</c:if>
	
	<c:if test="${!empty replylist}">
		<c:forEach items="${replylist}" var="reply">	
			<div class="topic-answer">
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
						<div class="pp-postby"> Posted on : ${reply.ansDate}</div>
						<c:if test="${not empty reply.lastModified}">
							<div class="pp-postby">Last Modified : ${reply.lastModified}</div>
						</c:if>
						<div class="clear"></div>
					</div>
					<c:if test="${! empty currentUser}">	
						<c:if test="${currentUser.userid == reply.userAnswer.userid}">	
							<div class="topic-detail">
								<ul class="post-buttons">
									<li class="p-post"><a href="updateReplyRedirect?answerId=${reply.answerid}&postId=${postDetails.postid}">Edit &nbsp; &nbsp;</a></li>
									<li class="p-post"><a href="deleteReply?answerId=${reply.answerid}&postId=${postDetails.postid}" onclick="return confirmDelete()">Remove &nbsp;</a></li>
								</ul>
							</div>	
						</c:if>		
					</c:if>	
					
					<div class="postbody">
										
						<p class="bigtext" style="overflow-wrap: break-word; padding-right: 15px;text-align: justify;">${reply.description}</p>
						<br>
						<div class="clear"></div>
						<hr style="margin-bottom: 2px; margin-top: 2px">
						<div class="topic-detail" style="margin-bottom:5px; margin-top:10px ; border: none;">
							<c:if test="${!empty currentUser}">
								
								<c:if test="${fn:length(reply.userVote) gt 0}">
									<c:forEach items="${reply.userVote}" var="votelist">
									<c:if test="${votelist.userAnswer.answerid eq reply.answerid}">  
										<input class="btn btn-primary btn-xs" onclick="removeVote(${reply.answerid})" type="button" value="Dislike" id = "removeVoteButton"></input>
									</c:if>
								</c:forEach>
								</c:if>
								
								<%-- <c:if test="${votelist.userVote.userid eq currentUser.userid}"> --%>
								<c:if test="${fn:length(reply.userVote) eq 0}"> 
									<input class="btn btn-primary btn-xs" onclick="addVote(${reply.answerid})" type="button" value="Like" id = "addVoteButton"></input>
								</c:if>
								&nbsp;
							</c:if>
							
								<!-- Like Counter part -->
								<c:set var="count" value="0" scope="page" />
								<c:forEach items="${votelist}" var="votelist">
									<c:if test="${votelist.userAnswer.answerid eq reply.answerid}"> 
										<c:set var="count" value="${count + 1}" scope="page"/>
									</c:if>
								</c:forEach>
								<c:out value="Total Likes :  ${count}"></c:out>
								<!-- Like Counter part end-->
								
									<!-- <input class="btn btn-primary btn-xs" onclick="removeVote()" type="button" value="Dislike" id = "removeVoteButton"></input> -->
								<%-- <c:if test="${(votelist.userVote.userid eq currentUser.userid) && (votelist.userAnswer.answerid eq reply.answerid)}">
								1
								</c:if> --%>
							
							
							<%-- <ul class="post-buttons">
								<li><span class="glyphicon glyphicon-thumbs-up"></span><a href="addvote?answerId=${reply.answerid}">Like </a>&nbsp; &nbsp; &nbsp;</li>
								<li><span class="glyphicon glyphicon-thumbs-down"></span><a href="removevote?answerId=${reply.answerid}">Dislike</a></li>								
							</ul> --%>
						</div>
						
					</div>
					<div class="clear"></div>
				</div>
		</c:forEach>
	</c:if>	
		
		
		
		
		<!-- ******************** Answer List End ************************ -->

	<!-- $$$$$$$$$$$$$$$$$$$$$$ Answer post starts here $$$$$$$$$$ -->
		<c:if test="${! empty currentUser && ! empty replylist}">
			<div class="topic-answer">
				<div class="postprofile">
					
					<div class="pp-title">
						<a href="#">${currentUser.username}</a>
						<div class="clear">${currentUser.role.name}</div>

					</div>
					<br>
					
					<div class="clear"></div>
				</div>
				
				<div class="postbody">
					<div class="author">
						<h3>Answers :</h3>
					</div>
						<form:form name ="addReplyForm" method="post" action="adduseranswer" modelAttribute="addReply" class="form-horizontal cus-form">
				
							<form:textarea path="description" placeholder="Enter Your Answer Here" id="description" rows="5" cols="100" name="description"/><br>
              				<form:input type="hidden" path="userPost.postid" value="${postDetails.postid}" />
              				<br>
				           	<input type="submit" name="Post Reply" onclick="return validateanswer();" value="Post Reply" class="btn btn-primary" /> &nbsp;
							<input name="Reset" type="reset" class="btn btn-default" value=" Reset ">
				         
				        </form:form>
		
					<br>

				</div>
				<div class="clear"></div>
			</div>
		</c:if>
		
		<c:if test="${noback == 'noback'}">
		
			<script type="text/javascript">
				history.pushState(null, null, null);
				window.addEventListener('popstate', function(event) {
			    window.location.assign("advanceSearchRedirect");
				});
			</script>
		
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

<!-- Validation for Post -->
	<script>  
		function validateanswer(){  
		var answer = document.addReplyForm.description.value;  
		
		
		
			if (answer == null || answer == "") {  
				  
				alert("Answer can't be Null.");
				return false;
				  	  
			} else if(answer.length > 5000) {
			
				alert("Answer must be less than 5000 character.");
				return false;
			
			} else {
				return true;
			};
		};  
</script>

<!-- Confirm alert for Delete Reply -->
<script type="text/JavaScript">

	function confirmDelete(){
		var agree = confirm("Are you sure you want to delete this Reply ?");
		
		if (agree){
		     return true ;
		} else {
	     	return false ;
		}
	}
</script>

	<c:if test="${!empty replylist}">
		<c:forEach items="${replylist}" var="reply">	
			<script type="text/javascript">
	
			function addVote(answerId) {
				window.location.href = "addvote?answerId="+answerId;
				//document.getElementById("addVoteButton").disabled = true ;
				//document.getElementById("removeVoteButton").disabled = false;
			}
			
			function removeVote(answerId) {
				window.location.href = "removevote?answerId="+answerId;
				//document.getElementById("removeVoteButton").disabled = true ;
				//document.getElementById("removeVoteButton").disabled = false ;
			}
			
				/* function changeVote()
				{
				    var elem = document.getElementById("voteButton");
				    if (elem.value=="Like"){
				    	elem.value = "Dislike";
				    	window.location.href = "addvote?answerId=${reply.answerid}";
				    } else {
				    	elem.value = "Like";
				    	window.location.href = "removevote?answerId=${reply.answerid}";
				    }
				} */
	
			</script>
		</c:forEach>
	</c:if>

<!-- end -->



</body>
</html>