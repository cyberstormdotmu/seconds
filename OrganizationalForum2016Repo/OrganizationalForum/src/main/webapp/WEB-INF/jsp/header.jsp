<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.sql.*" %>
<%ResultSet resultset =null;%>

<%-- <%
    try{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connection = 
        	 DriverManager.getConnection
            ("jdbc:mysql://192.168.20.152:3306/orgnizational_forum?user=dhaval&password=dhaval");

       Statement statement = connection.createStatement() ;

       resultset =statement.executeQuery("select * from category") ;
%> --%>

<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>Dashboard</title>
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
<c:set var="currentUser" value="${sessionScope.currentLoggedinUser}"/>

<div id="wrap">
	<!-- Header Start -->
	<header class="sticky-header">
    	<div class="sticky-bar">
        
          <div class="topmenu">
          	<a class="menu-toggle" href="#menu">
              <i class="menu-icon">
                <span class="line1"></span>
                <span class="line2"></span>
                <span class="line3"></span>
              </i>
            </a>
            <nav id="menu" class="menu">
                <ul class="mob_menu">
                  <li class="top"><a href="dashboard" class="toplink">FORUM</a></li>
                  <li class="top"><a href="advanceSearchRedirect" class="toplink">Advance Search</a></li>
                  
                  <c:if test="${empty currentUser}">
                  	<li class="top"><a href="contactus" class="toplink" >Contact Us</a></li>
                  </c:if>
                  
                  <c:if test="${! empty currentUser}">
                  <li class="top has-submenu"><a href="#" class="toplink">Menu<img src="<%=request.getContextPath()%>/resources/images/ar_bot.png" alt=""></a>
                  
                    	<ul class="sub-menu">
                    
             	  		 <%	request.getAttribute("admin"); %>
  
  						<li><a href="mypostlist?userId=${currentUser.userid}">My Posts</a></li>
  						
						<c:if test="${currentUser.role.roleid eq 1}">
					  		<li><a href="allpostlist">All Posts</a></li>
					  		<li><a href="listuser">User List</a></li>
					  	</c:if>  
   	                   		<li><a href="contactus">Contact Us</a></li>	
   	                   </c:if>
   	                   
                      
                    </ul>
                  </li>
                  
                </ul>
            </nav>
          </div>
       
       <div class="user-area">
       <c:if test="${! empty currentUser}">  
            <div class="dropdown pull-right">
            	<a class="rl" href="logout">Logout</a>
            </div>
          	<div class="rlink"><a href="editprofile" class="rl">Edit Profile</a></div>
        	<div class="rlink" style="margin-top:14px;color:#208fc8;font-weight:bold;">WelCome ${currentUser.username }</div>
       </c:if>
       <c:if test="${empty currentUser}">		
       		<div class="dropdown pull-right">
            	<a class="rl" href="loginRedirect">Login</a>
            </div>
       		<div class="rlink" style="margin-top:14px;color:#208fc8;font-weight:bold;">WelCome </div>
        </c:if>  
        </div>
                    
        </div>
    </header>
  
    <header class="main-header">
      <h1>Tatvasoft Orgnizational Forum</h1>
      <div class="searchbar">
      	
      	<select class="form-control" id="dynamic_select">
		    <option value="" selected>Select Category</option>
		
		    <option value="<%=request.getContextPath()%>/postlist?categoryId=1">Human Resources</option>
		    <option value="<%=request.getContextPath()%>/postlist?categoryId=2">Network</option>
		    <option value="<%=request.getContextPath()%>/postlist?categoryId=3">Technical</option>
		    <option value="<%=request.getContextPath()%>/postlist?categoryId=4">General</option>
		
		</select>
		
		
      </div>
    </header>    
    <!-- Header End -->

   
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

<script type="text/javascript">

$(function(){
    // bind change event to select
    $('#dynamic_select').on('change', function () {
        var url = $(this).val(); // get selected value
        if (url) { // require a URL
            window.location = url; // redirect
        }
        return false;
    });
  });

</script>

<!-- end -->



</body>
</html>