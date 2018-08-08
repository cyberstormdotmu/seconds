<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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

<!-- TatvaSoft - set current user from scope -->
<c:set var="currentUser" value="${sessionScope.currentLoggedinUser}"/>

</head>
<body>

<div id="wrap">
 <!-- Content Start -->
	<footer class="page-footer">
      <div class="footer-top">
          <div class="ft-right">
          		<a href="https://www.facebook.com/TatvaSoft" class="facebook" title="Facebook"></a>
          		<a href="https://www.linkedin.com/company/tatvasoft" class="twitter" title="Twitter"></a>
          		<a href="https://plus.google.com/+tatvasoft" class="gplus" title="Google Plus"></a>
          		
          </div>
      </div>
      <div class="footer-middle">
        <div class="row">
        	<div class="col-xs-6 col-sm-2">
            	<h3>Forum</h3>
                <ul>
                	<li><a href="contactus">Contact us</a></li>
                	<li><a href="mailto:info@tatvasoft.com">Email : info@tatvasoft.com</a></li>
                	<li><a href="">Contact : +91 79 4003 8222</a></li>
                </ul>
            </div>
            
            <div class="clearfix visible-xs-block"></div>
            <div class="col-xs-6 col-sm-2">
            	<h3>Navigation</h3>
                <ul>
                	<li><a href="loginRedirect">Login</a></li>
                    <li><a href="register">Register</a></li>
                    <li><a href="dashboard">Home</a></li>
                    <c:if test="${not empty currentUser}">
                    	<li><a href="mypostlist?userId=${currentUser.userid}">My Posts</a></li>
                    </c:if>
                    <li><a href="advancesearch">Advance Search</a></li>
                </ul>
            </div>
            <div class="col-xs-6 col-sm-2">
            	<h3>Social</h3>
                <ul>
                	<li><a href="https://www.linkedin.com/company/tatvasoft">LinkedIn</a></li>
                    <li><a href="https://www.facebook.com/TatvaSoft">Facebook</a></li>
                    <li><a href="https://twitter.com/tatvasoft">Twitter</a></li>
                    <li><a href="https://plus.google.com/+tatvasoft">Google+</a></li>
                </ul>
            </div>
            <div class="clearfix visible-xs-block"></div>
            <div class="col-xs-12 col-sm-4">
            	<h3>About us</h3>
                <p>TatvaSoft is a CMMi Level 3 and Microsoft Gold Certified Software Development company that offers custom software development services on diverse technology platforms, like Microsoft, Java, PHP, Open Source, BI, and Mobile.</p>
            </div>
        </div>
      </div>
      <div class="footer-bottom">
        <div class="fb-left">Copyright &copy; Tatvasoft. All rights reserved.</div>
        <div class="fb-right"> Design by <a href="#">Tatvasoft.com</a></div>
      </div>
    </footer>
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