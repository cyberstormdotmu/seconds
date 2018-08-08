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
<title>Contact Us</title>
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
			<li>Contact Us</li>
		</ol>
		
		<div class="clear"> </div>
		<div class="page-content">
			<div class="topic-detail" style="border: none;">
                <!--leftside start-->
				<div class="buttonbar">
						<div>
							<h3>Contact Us</h3>
						</div>			
				</div>
               
                <p style="font-size: 14px; padding-left: 15px;padding-right: 15px;">Thank you for considering TatvaSoft as your software development partner. We are eager to serve you with our Custom Software Development Services on technology platforms, like Microsoft, Java, PHP, Mobile, Big Data, BI, Open Source and others.</p>
                
                <p style="font-size: 14px; padding-left: 15px;padding-right: 15px;">Please send us your requirements and we'll get back to you at the earliest. </p>
                
                <div class="tabarea" style="text-align: justify!important;padding: 0 0 12px;">
                    <div id="horizontalTab" style="display: block; width: 100%; margin: 0px;">
                        
                        <div class="resp-tabs-container" style=" padding-left: 20px; padding-bottom: 0px !important;">
                            <h3 class="resp-accordion" role="tab" aria-controls="tab_item-0"><span class="resp-arrow"></span><img src="<%=request.getContextPath()%>/resources/images/fl-us.png" class="flag"> US</h3><div class="resp-tab-mid resp-tab-content" aria-labelledby="tab_item-0">
                                <div class="contactlist">
                                    <div class="con-col3">
                                        <p>
                                            <b>New Jersey</b><br>
                                            33 Wood Avenue South,<br>
                                            Suite 630, Iselin,<br>
                                            New Jersey, 08830<br>
                                            Tel: +1 909 680 3050
                                        </p>
                                    </div>
                                    <div class="con-col3">
                                        <p>
                                            <b>Los Angeles</b><br>
                                            Tel: +1 732 983 7742
                                        </p>
                                    </div>
                                    <div class="con-col3">
                                        <p>
                                            <b>New York</b><br>
                                            Tel: +1 732 491 3323
                                        </p>
                                    </div>
                                </div>
                                  
                                <div class="hidden-xs" style="min-height:22px;"></div>
                            </div>
                            <h3 class="resp-accordion" role="tab" aria-controls="tab_item-1"><span class="resp-arrow"></span><img src="<%=request.getContextPath()%>/resources/images/fl-Canada.png" class="flag"> Canada</h3><div class="resp-tab-mid resp-tab-content" aria-labelledby="tab_item-1">
                                <div class="contactlist">
                                    <div class="leftlist rtlpad">
                                        <p>
                                            <b>Toronto</b><br>
                                            4711 Yonge Street, 10th Floor, <br>
                                            Toronto,  ON, M2N 6K8<br>
                                            Tel : +1 647 978 7562
                                        </p>
                                    </div>
                                </div>
                                  
                                <div class="hidden-xs" style="min-height:44px;"></div>
                            </div>
                            <h3 class="resp-accordion" role="tab" aria-controls="tab_item-2"><span class="resp-arrow"></span><img src="<%=request.getContextPath()%>/resources/images/fl-Europe.png" class="flag"> Europe</h3><div class="resp-tab-mid resp-tab-content" aria-labelledby="tab_item-2">
                                <div class="contactlist">
                                    <div class="con-col3">
                                        <p>
                                            <b>UK</b><br>
                                            307B, Warnford Court, <br>
                                            29 Throgmorton Street, <br>
                                            London EC2N 2AT <br>
											Tel : +44 (0)207 947 4950
                                        </p>
                                    </div>
                                    <div class="con-col3">
                                        <p>
                                            <b>Sweden </b><br>
                                            Tel : +46 (0) 851 970 707
                                        </p>
                                    </div>
                                    <div class="con-col3">
                                        <p>
                                            <b>Finland  </b><br>
                                            Tel : +358 92 316 1011
                                        </p>
                                    </div>
                                </div>
                                  
                                <div class="hidden-xs" style="min-height:22px;"></div>
                            </div>
                            <h3 class="resp-accordion" role="tab" aria-controls="tab_item-3"><span class="resp-arrow"></span><img src="<%=request.getContextPath()%>/resources/images/fl-au.png" class="flag"> Australia</h3><div class="resp-tab-mid resp-tab-content" aria-labelledby="tab_item-3">
                                <div class="contactlist">
                                    <div class="leftlist rtlpad">
                                        <p>
                                            <b>Sydney </b><br>
                                            Suite 1A, Level 2,<br>
											802 Pacific Hwy Gordon, <br>
                                            Sydney NSW 2072,<br>
                                            Tel : +61 2 9844 5446<br>
                                        </p>
                                    </div>
                                    <div class="leftlist">
                                        <p>
                                            <b>Melbourne</b><br>
                                            Level 23, HWT Tower<br>
                                            40 City Road, Southbank<br>
                                            Melbourne VIC 3006<br>
                                            Tel : +61 3 9674 0440
                                        </p>
                                    </div>
                                </div>
                                
                                <div class="hidden-xs" style="min-height:22px;"></div>
                            </div>

                            <h3 class="resp-accordion resp-tab-active" role="tab" aria-controls="tab_item-4"><span class="resp-arrow"></span><img src="<%=request.getContextPath()%>/resources/images/fl-ind.png" class="flag"> India</h3><div class="resp-tab-mid resp-tab-content resp-tab-content-active" aria-labelledby="tab_item-4" style="display:block">
                                <div class="contactlist">

                                    <div class="leftlist rtlpad">
                                        <p>
                                            <b>Ahmedabad (Development Center)</b><br>
                                            9th Floor, Iscon Elegance,<br>
                                            Prahladnagar, SG Road,<br>
                                            Ahmedabad - 380 015,<br>
                                            Gujarat.<br>
                                            Tel: +91 79 4003 8222
                                        </p>

                                    </div>

                                    <div class="leftlist">
                                        <p>
                                            <b>Pune</b><br>
                                            Suite 306, 3rd Floor, <br>
                                            Lunkad Sky Station Building, Viman Nagar,<br>
                                            Pune - 411014,<br>
                                            Maharashtra.<br>
                                            Tel: +91 20 4120 2211
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>               
               
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