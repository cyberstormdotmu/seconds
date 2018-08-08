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

<div id="wrap">
 <jsp:include page="header.jsp"></jsp:include>
	
	<!-- Content Start -->
	<section class="page-body">
    	<ol class="breadcrumb">
          <li><span class="glyphicon glyphicon-home"></span></li>
          
        </ol>
        <div class="page-content">

        	 
            <br> 
        
        	<div class="big-grid">
            	<div class="col-maincontent">                                      
                    
                	<div class="category-wrapper">
                    	<div class="category-header">
                        	Primary Category
                        </div>
                        <div id="collapseOne" class="category-inner collapse in">
                        	<ul class="forums-list">
                            	<li class="itemlist-forums">
                                	<div class="item-inner">
                                      <div class="forum-image"><img src="<%=request.getContextPath()%>/resources/images/hr.png" alt=""></div>
                                      <div class="item-col-main">
                                      	<a class="item-title" href="postlist?categoryId=1">Human Resources</a>
										<span class="item-info">Questionnaire about Organization Human Resources Department.</span>
                                      </div>
                                      
                                      <div class="item-col-stats">
										<span class="item-stat">
                                            <span class="item-stat_count">${hrposts}</span>
                                            <span class="item-stat_label">Posts</span>
                                        </span>
                                        <!-- <span class="item-stat">
                                            <span class="item-stat_count">74</span>
                                            <span class="item-stat_label">Answers</span>
                                        </span> -->
                                      </div>
                                </li>
                                <li class="itemlist-forums">
                                	<div class="item-inner">
                                      <div class="forum-image"><img src="<%=request.getContextPath()%>/resources/images/network.png" alt=""></div>
                                      <div class="item-col-main">
                                      	<a class="item-title" href="postlist?categoryId=2">Network</a>
										<span class="item-info">Questionnaire about Network issues.</span>
                                      </div>
                                      
                                      <div class="item-col-stats">
										<span class="item-stat">
                                            <span class="item-stat_count">${networkposts}</span>
                                            <span class="item-stat_label">Posts</span>
                                        </span>
                                        <!-- <span class="item-stat">
                                            <span class="item-stat_count">8</span>
                                            <span class="item-stat_label">Answers</span>
                                        </span> -->
                                      </div>
                                </li>
                                <li class="itemlist-forums">
                                	<div class="item-inner">
                                      <div class="forum-image"><img src="<%=request.getContextPath()%>/resources/images/technical.png" alt=""></div>
                                      <div class="item-col-main">
                                      	<a class="item-title" href="postlist?categoryId=3">Technical</a>
										<span class="item-info">Questionnaire about Technical issues.</span>
                                      </div>
                                       
                                      <div class="item-col-stats">
										<span class="item-stat">
                                            <span class="item-stat_count">${technicalposts}</span>
                                            <span class="item-stat_label">Posts</span>
                                        </span>
                                        <!-- <span class="item-stat">
                                            <span class="item-stat_count">9</span>
                                            <span class="item-stat_label">Answers</span>
                                        </span> -->
                                      </div>
                                </li>
                            </ul>
                        </div>
                    </div>                    
                    
                    <div class="category-wrapper">
                    	<div class="category-header">
                        	Another Category
                        </div>
                        <div id="collapseTwo" class="category-inner collapse in">
                        	<ul class="forums-list">
                            	<li class="itemlist-forums">
                                	<div class="item-inner">
                                      <div class="forum-image"><img src="<%=request.getContextPath()%>/resources/images/faq.png" alt=""></div>
                                      <div class="item-col-main">
                                      	<a class="item-title" href="postlist?categoryId=4">General Questions</a>
										<span class="item-info">Post General Queries here.</span>
                                      </div>
                                       
                                      <div class="item-col-stats">
										<span class="item-stat">
                                            <span class="item-stat_count">${generalposts}</span>
                                            <span class="item-stat_label">Posts</span>
                                        </span>
                                        <!-- <span class="item-stat">
                                            <span class="item-stat_count">4</span>
                                            <span class="item-stat_label">Answers</span>
                                        </span> -->
                                      </div>
                            	</li>
                            </ul>
                        </div>
                    </div>
                    
                </div>
                <div class="col-sidebar">
                	<div class="panel panel-default">
                      <div class="panel-heading"><span class="glyphicon glyphicon-signal"></span> Statistics</div>
                      <div class="panel-body">
                        <table class="statistics_data">
                          <tr>
                            <td>Total Posts</td>
                            <td class="sd_value">${totalPosts}</td>
                          </tr>
                          <tr>
                            <td>Total Answers</td>
                            <td class="sd_value">${totalAnswers}</td>
                          </tr>
                          <tr>
                            <td>Total Members</td>
                            <td class="sd_value">${totalUsers}</td>
                          </tr>
                        </table>

                      </div>
                    </div>
                </div>
            </div>
        
        
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
<!-- end -->



</body>
</html>