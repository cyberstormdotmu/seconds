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
<title>List of Posts</title>
<link href="<%=request.getContextPath()%>/resources/css/bootstrap.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/resources/css/style.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/resources/css/menu.css" rel="stylesheet">

<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->
<script src="<%=request.getContextPath()%>/resources/js/jquery.min.js"></script>

<!-- <style type="text/css">
h3,tr,input,a {
    font-family: cursive;
    color: olive;
    font-size: 15px;
}

a {
    font-family: cursive;
    color: red;
}
</style> -->

</head>
<body>
<div id="wrap">
	 <jsp:include page="header.jsp"></jsp:include>
<ol class="breadcrumb">
          <li><span class="glyphicon glyphicon-home"></span></a></li>
          <li><a href="dashboard">Home</a></li>
        </ol>
 <center>
        <br><br>
        <h1>List Of Posts</h1>

        <br><br>
        <c:if test="${!empty myquelist}">
            <div style="margin: 30px;">

                    <table class="data table table-striped table-bordered table-hover table-condensed" border="2">
                        <tr>
                            <th class="tblhdr">Post</th>
                            <th class="tblhdr">Category</th>
                            <!-- <th class="tblhdr">Answers</th> -->
                            <th class="tblhdr">Update</th>
                            <th class="tblhdr">Delete</th>
                        </tr>
                        <c:forEach items="${myquelist}" var="post">
                            <%-- <c:if test="${user.role.roleid != 1}"> --%>
                                <tr>
                                    <td>${post.question}</td>
                                    <td>${post.category.categoryName}</td>
                                    <!-- <td>hello</td> -->
                                    <td><a href="updatePostRedirect?postId=${post.postid}">Edit</a></td>
                                    <td><a href="deletePost?postId=${post.postid}" onclick="return confirmDelete()">Delete</a></td>
                                </tr>
                            <%-- </c:if> --%>
                        </c:forEach>
                    </table>
                </div>
        </c:if>
        
        <c:if test="${empty myquelist}">
			<div class="buttonbar">
				<div class="left">
					<h4>&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="You haven't post any question yet."></c:out></h4>
				</div>			
			</div>
		</c:if>
        
        <br><br><br>
    </center>



     <jsp:include page="footer.jsp"></jsp:include>

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

<!-- Confirm alert for Delete Post -->
<script type="text/JavaScript">

	function confirmDelete(){
		var agree = confirm("Are you sure you want to delete this Post?");
		
		if (agree){
		     return true ;
		} else {
	     	return false ;
		}
	}
</script>

<!-- end -->



</body>
</html>