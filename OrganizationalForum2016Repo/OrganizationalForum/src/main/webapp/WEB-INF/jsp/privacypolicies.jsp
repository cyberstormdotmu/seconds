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
<title>Privacy Statements</title>
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
		
	<section class="page-body">
		<ol class="breadcrumb">
			<li><span class="glyphicon glyphicon-home"></span></li>
			<li><a href="dashboard">Home</a></li>
			<li>Privacy Statement</li>
		</ol>
		
		<div class="clear"> </div>
		<div class="page-content">
			<div class="topic-detail" style="border: none;">
                <!--leftside start-->
				<div class="buttonbar">
						<div>
							<h3>Privacy Statement</h3>
						</div>			
				</div>
               
                <p style="font-size: 14px; padding-left: 15px;padding-right: 15px;">1. TatvaSoft is very sensitive &amp; serious about privacy issues, and has no objection to viewers who accessing its website without providing any personal information. But in some circumstances TatvaSoft may request you to give your personal information.</p>
                
                <p style="font-size: 14px; padding-left: 15px;padding-right: 15px;">2. TatvaSoft is committed to protect your information from other companies, individuals or any illegal usages. TatvaSoft uses your information to provide you the needed solution/information, to contact you or to communicate with you only. We do not and will not provide any of your personal or business information to other companies or individuals without your specific request and permission.</p>
                
                <p style="font-size: 14px; padding-left: 15px;padding-right: 15px;">3. In general, you can visit our site for software development services without providing any personal information. You realise and understand that there is no compulsion on you to provide us with your personal information. You also understand that we are under no obligation to verify the source from which the personal information about you is provided to us.</p>
                
                <p style="font-size: 14px; padding-left: 15px;padding-right: 15px;">4. We conduct some times online research surveys to better understand the currant business environment, at that time we only use your information after your permission is given.</p>
                
                <p style="font-size: 14px; padding-left: 15px;padding-right: 15px;">5. TatvaSoft - a software development company, periodically collects only domain information and not the email addresses of its webs site's visitors as a part of its analysis process such as web site usability, performance and effectiveness. This specific data gives us the information about which users visit our site, what parts of the site they visit often &amp; how often they visit our site. TatvaSoft uses this useful information to improve its productivity, efficiency and customer satisfaction level. This information (domain) is collected automatically and requires no action from your side.</p>
 
                <p style="font-size: 14px; padding-left: 15px;padding-right: 15px;">For more details please contact us.</p>
                
        
				
		
			</div>	




			<!-- ********************Content Part End************************* -->
		</div>
	</section>
   
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