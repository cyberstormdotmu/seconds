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
<title>Terms and Conditions</title>
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
			<li>Terms and Conditions</li>
		</ol>
		
		<div class="clear"> </div>
		<div class="page-content">
			<div class="topic-detail" style="border: none;">
                <!--leftside start-->
				<div class="buttonbar">
						<div>
							<h3>Terms and Conditions</h3>
						</div>			
				</div>
               
                <p style="font-size: 14px; padding-left: 15px;padding-right: 15px;">1. Please read these Terms and Conditions carefully before using www.tatvasoft.com site which is in software outsourcing business.</p>
                
                <p style="font-size: 14px; padding-left: 15px;padding-right: 15px;">2. The user must agree and accept all the "Terms and Conditions" before using www.tatvasoft.com web sites.</p>
                
                <p style="font-size: 14px; padding-left: 15px;padding-right: 15px;">3. TatvaSoft - A software outsourcing company, reserves the right to modify "Terms and Conditions" at any time without informing the user.</p>
                
                <p style="font-size: 14px; padding-left: 15px;padding-right: 15px;">4.This web site www.tatvasoft.com is owned and operated by TatvaSoft only. All content present on this site is the property of TatvaSoft including all the graphics, images, logos, software, trademarks, video, text, audio and animation used on this Site.</p>
                
                <p style="font-size: 14px; padding-left: 15px;padding-right: 15px;">5. TatvaSoft name and logo is the trademark of TatvaSoft only. These trademarks & logo may not be used in any manner. You may not copy, reproduce, republished, use, adapt, modify and alter these contents of this website without the permission of TatvaSoft. You must not use any contact information and e-mail addresses which may be found on this web site to send any unsolicited commercial information.</p>
                
                <p style="font-size: 14px; padding-left: 15px;padding-right: 15px;">6. The link of the third party web site address may found on this web sites, so we are not responsible for the contents of such sites or whole web site (third party) at all. The Third party web site is not under control of TatvaSoft at all. You at your own risk may access to such other sites via the links contained on this web site.</p>
                
                <p style="font-size: 14px; padding-left: 15px;padding-right: 15px;">7. TatvaSoft shall have no responsibility for any damage to your computer system or loss of data that results from the download of any information from this Site.
				
				For more details please contact us.</p>
        
				
		
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