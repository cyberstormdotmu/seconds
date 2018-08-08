<%@page import="javax.swing.text.Document"%>
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
<title>Search</title>
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

<!-- TatvaSoft - set current user from scope -->

<div id="wrap">
 <jsp:include page="header.jsp"></jsp:include>
		
  <!-- Content Start -->
	<section class="page-body">
    	<ol class="breadcrumb">
          <li><span class="glyphicon glyphicon-home"></span></li>
          <li><a href="dashboard">Home</a></li>
          <li>Advance Search</li>
          <li>${Hello}</li>
        </ol>
        <div class="page-content">
        	<h2 class="title bord">Search</h2>
            <form class="form-horizontal cus-form sr-form" method="POST" action="getSearchResult" id="searchpost" name="searchpost">
              <div class="category-header botspac">Search Post</div>
              <div class="form-group">
                <label class="col-sm-4 control-label">Search by keywords:<p>Enter keywords for searching post which contains that keywords.</p></label>
                <div class="col-sm-8">
                <input type="text" id="keyword" name="keyword" class="form-control fc"></div>
              </div>
              <!-- <div class="form-group">
                <label class="col-sm-4 control-label">Search within:</label>
                <div class="col-sm-8" style="margin-top:-6px;">
                	<span class="chk-radio"><input name="search" type="radio" checked>In question text only</span>
                    <span class="chk-radio"><input name="search" type="radio">In description text only</span>
                </div>
              </div> -->
             
              
             
              <div class="form-group">
                <label class="col-sm-4 control-label">Search in Category:<p>Select category which belongs to your question.</p></label>
                <div class="col-sm-8">
                	<select class="form-control fc" id="category_id" name="category_id">
		    			<option value="0" selected>All Category</option>
						<option value="1">Human Resources</option>
				    	<option value="2">Network</option>
				    	<option value="3">Technical</option>
				    	<option value="4">General</option>
					</select>
                </div>
              </div>
              
              <div class="form-group">
                <label class="col-sm-4 control-label">Sort by Post Time :</label>
                <div class="col-sm-8">
                  <select class="form-control fc" id="createDate" name="createDate">
                      <option selected="selected" value="asce">Ascending</option>
                      <option value="desc">Descending</option>
                  </select>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-4 control-label">Limit results to previous:</label>
                <div class="col-sm-8">
                	<select class="form-control fc" id="sortlimit" name="sortlimit" >
                      <option selected="selected" value="0">All results</option>
                      <option value="5">1 Day</option>
                      <option value="1">1 Week</option>
                      <option value="2">1 month</option>
                      <option value="3">6 months</option>
                      <option value="4">1 year</option>
                    </select>
                </div>
              </div>
              <!-- <div class="form-group">
                <label class="col-sm-4 control-label">Return first:</label>
                <div class="col-sm-8">
                	<select class="form-control fc">
                      <option value="-1">All available</option>
                      <option selected="selected" value="25">25</option>
                      <option value="50">50</option>
                      <option value="100">100</option>
                      <option value="200">200</option>
                      <option value="300">300</option>
                      <option value="400">400</option>
                      <option value="500">500</option>
                      <option value="600">600</option>
                      <option value="700">700</option>
                      <option value="800">800</option>
                      <option value="900">900</option>
                      <option value="1000">1000</option>
                    </select>
                    
                </div>
              </div> -->
              <div class="buttonbar">
                <div class="right"><input name="Search" type="submit" id="search-post-button" class="btn btn-primary" onclick="return validateform();" value=" Search "> &nbsp;
                <input name="Reset" type="reset" class="btn btn-default" value=" Reset "></div>
              </div>
            </form>
       
       <c:if test="${empty resultlist}">
       		<div class="buttonbar">
				<div class="left">
					<h4><c:out value="No such Post found."></c:out></h4>
				</div>			
			</div>
       </c:if>
              
       <c:if test="${!empty resultlist}">
		<c:forEach items="${resultlist}" var="post">
			<div class="topic-detail" style="height: 100px">
				<div class="postprofile" style="height: 70px">
					<div class="pp-title">
						<a href="#">${post.userPost.username}</a>
						<div>${post.userPost.role.name}</div>
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
       
       
       </div>        
    </section>
    <!-- Content End --> 	

   
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

<!-- Validation for search -->
	<script type="text/javascript">  
		function validateform(){  
		var keyword = document.searchpost.keyword.value;  
		
			if (keyword == null || keyword == "" || 3 > keyword.length || keyword.length > 100){  
			  
				alert("keyword must be between 3 characters and 20 characters.");
				return false;
			
			} else {
				return true;
				
			};
	};
	</script>

<!-- end -->



</body>
</html>