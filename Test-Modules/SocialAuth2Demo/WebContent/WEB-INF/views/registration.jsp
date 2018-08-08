<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false"%>
    <style type="text/css">
        .style1{text-align: justify;}
    </style>
    <div id="main">
        <div class="alert alert-info">
        	<strong>Step 1&nbsp;:&nbsp;</strong>Show require providers through which you want to do registration. Click on any icon for registration.
        </div>
        <div>
	       	<table cellpadding="10" cellspacing="10" align="center">
	       	
				<tr>
					<td>
						<a href="socialauth.do?id=facebook"><img src="images/facebook_icon.png" alt="Facebook" title="Facebook" border="0"></img></a>
					</td>
					<td>
						<a href="socialauth.do?id=twitter"><img src="images/twitter_icon.png" alt="Twitter" title="Twitter" border="0"></img></a>
					</td>
					<td>
						<a href="socialauth.do?id=google"><img src="images/gmail-icon.jpg" alt="Gmail" title="Gmail" border="0"></img></a>
					</td>
					<td>
						<a href="socialauth.do?id=yahoo"><img src="images/yahoomail_icon.jpg" alt="YahooMail" title="YahooMail" border="0"></img></a>
					</td>
					<td>
						<a href="socialauth.do?id=hotmail"><img src="images/hotmail.jpeg" alt="HotMail" title="HotMail" border="0"></img></a>
					</td>
					<td>
						<a href="socialauth.do?id=linkedin"><img src="images/linkedin.gif" alt="Linked In" title="Linked In" border="0"></img></a>
					</td>
					<td>
						<a href="socialauth.do?id=foursquare"><img src="images/foursquare.jpeg" alt="FourSquare" title="FourSquare" border="0"></img></a>
					</td>
					<td>
						<a href="socialauth.do?id=myspace"><img src="images/myspace.jpeg" alt="MySpace" title="MySpace" border="0"></img></a>
					</td>
					<td>
						<a href="socialauth.do?id=mendeley"><img src="images/mendeley.jpg" alt="Mendeley" title="Mendeley" border="0"></img></a>
					</td>
					<td>
						<a href="socialauth.do?id=yammer"><img src="images/yammer.jpg" alt="Yammer" title="Yammer" border="0"></img></a>
					</td>
					<td>
						<a href="socialauth.do?id=googleplus"><img src="images/googleplus.png" alt="Google Plus" title="Google Plus" border="0"></img></a>
					</td>
					<td>
						<a href="socialauth.do?id=instagram"><img src="images/instagram.png" alt="Instagram" title="Instagram" border="0"></img></a>
					</td>
					<td>
						<a href="socialauth.do?id=flickr"><img src="images/flickr_icon.jpg" alt="Flickr" title="Flickr" border="0"></img></a>
					</td>
					<td>
						<a href="socialauth.do?id=github"><img src="images/github.png" alt="GITHub" title="GITHub" border="0"></img></a>
					</td>
				</tr>
			</table>
           	<br />
	        <br />
	        <br />
	        <br />
	        <br />
	        <br />
	        <br />
	        <br />
	        <br />
             <p class="additional">
            </p>
        </div>
        
        <div id="fblikediv">
   			<img src="<%=request.getContextPath() %>/images/facebook-share.PNG" id="fblikeimg" onMouseOver="javascript:return renderFbLike();">
		</div>
        
        
      <script type="text/javascript">

        function renderFbLike() {
            var parent = document.getElementById('fblikediv');
        var child = document.getElementById('fblikeimg');
        parent.removeChild(child);
            
        var html = "<iframe src="//www.facebook.com/plugins/like.php?href=https%3A%2F%2Fdevelopers.facebook.com%2Fdocs%2Fplugins%2F&amp;width&amp;layout=standard&amp;action=like&amp;show_faces=true&amp;share=true&amp;height=80&amp;appId=439662192827842" scrolling="no" frameborder="0" style="border:none; overflow:hidden; height:80px;" allowTransparency="true"></iframe>";
            document.getElementById('fblikediv').innerHTML = html;
   		 }

        </script>
        
    </div>
    