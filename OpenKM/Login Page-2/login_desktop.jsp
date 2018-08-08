<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Locale" %>
<%@ page import="com.openkm.core.Config" %>
<%@ page import="com.openkm.dao.LanguageDAO" %>
<%@ page import="com.openkm.dao.bean.Language" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.openkm.com/tags/utils" prefix="u" %>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="css/style-login.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="js/utils.js"></script>
	<!--[if IE]>
		<script type="text/javascript" src="js/html5.js"></script>
	<![endif]-->
	<script> 
		$(document).ready(function(){
		
			$("#img").attr("src", $("#selectedlang").find("img").attr("src"));
			$("#lab").text($("#selectedlang a").text());
			$("#j_language").val($("#selectedlang").find("img").attr("id"));
		
			$("#flip").click(function(){
				$("#langlink").slideToggle(200); 	//slow 600,  fast 200,  _default 400
			});
			
			$("body").click(function(event){
				var className = $(event.target).attr("class");
				if(className==undefined || !$(event.target).hasClass("temp"))
				{
					$("#langlink").slideUp(200); 	//slow 600,  fast 200,  _default 400	  		
				}
			});
			
			$("#langlink li a").click(function(){
				console.log($(this));
				$("#img").attr("src", $(this).find("img").attr("src"));
				$("#lab").text($(this).text());
			});
		});
	</script>
	<script type="text/javascript">
		if (getUserAgent().startsWith('ie')) addCss('css/ie.css');
	</script>
	<%
		Locale locale = request.getLocale();
		Cookie[] cookies = request.getCookies();
		String preset = null;
    
		if (cookies != null) {
			for (int i=0; i<cookies.length; i++) {
				if (cookies[i].getName().equals("lang")) {
					preset = cookies[i].getValue();
				}
			}
		}
    
		if (preset == null || preset.equals("")) {
			preset = locale.getLanguage() + "-" + locale.getCountry();
		}
	%>
	<title><%=Config.TEXT_TITLE%></title>
</head>

<body>
	<u:constantsMap className="com.openkm.core.Config" var="Config"/>
<!-- wrapper start -->
<div id="login-wrapper">
  
  <!-- content start -->
  <section id="login-content"> 	
    <div class="login-leftside">
    	<table>
			<tr>
				<td class="logo-ems"><img src="images/ems.png" alt=""></td>
				<td class="logo-moh"><img src="images/moh.png" alt=""></td>
			</tr>
        </table>
        <div class="dms-login">
        	<h1 class="logintitle">Document Management System</h1>
            <form name="loginform" method="post" action="j_spring_security_check" onsubmit="setCookie()">
				<c:if test="${not empty param.error}">
					<div style="color:red; font-family:calibri,arial; font-size:24px;" class="frm-group">Authentication error</div>
					<c:if test="${Config.USER_PASSWORD_RESET && Config.PRINCIPAL_ADAPTER == 'com.openkm.principal.DatabasePrincipalAdapter'}">
						(<a href="password_reset.jsp">Forgot your password?</a>)
					</c:if>
				</c:if>
				<div class="frm-group">
					<% if (Config.SYSTEM_MAINTENANCE) { %>
						<table>
							<tr><td style="color:red; font-family:calibri,arial; font-size:24px;">System under maintenance</td></tr>
						</table>
						<input name="j_username" id="j_username" type="hidden"  class="form-field" 
								value="<%=Config.SYSTEM_LOGIN_LOWERCASE?Config.ADMIN_USER.toLowerCase():Config.ADMIN_USER%>" placeholder="Username" />
					<% } else { %>
						<input name="j_username" id="j_username" type="text" <%=Config.SYSTEM_LOGIN_LOWERCASE?"onchange=\"makeLowercase();\"":"" %> placeholder="Username" class="form-field"/>
					<% } %>
				</div>
				<div class="frm-group">
					<input name="j_password" id="j_password" type="password" placeholder="Password" class="form-field" />
				</div>
				<div class="frm-group">
					<table>
						<tr>
							<td class="language-drop">
								<div class="language-main">
									<input name="j_language" id="j_language" type="hidden" />
									<%
										List<Language> langs = LanguageDAO.findAll();
										String whole = null;
										String part = null;
										
										// Match whole locale
										for (Language lang : langs) {
											String id = lang.getId();
											
											if (preset.equalsIgnoreCase(id)) {
											whole = id;
											} else if (preset.substring(0, 2).equalsIgnoreCase(id.substring(0, 2))) {
											part = id;
											}
										}
										
										out.print("<a class=\"temp languagelink\" id=\"flip\">");
										out.print("<img src=\"\" alt=\"\" height=\"14px\" width=\"18px\" id=\"img\" class=\"temp\" />");
										out.print("<label id=\"lab\" class=\"temp\" ></label> ");
										out.print("<span class=\"temp\"></span>");
										out.print("</a>");
										
										out.print("<ul class=\"lang-block\" id=\"langlink\">");
										
										// Select selected
										for (Language lang : langs) {
											String id = lang.getId();
											String selected = "";
											
											if (whole != null && id.equalsIgnoreCase(whole)) {
											selected = "selected";
											} else if (whole == null && part != null && id.equalsIgnoreCase(part)) {
											selected = "selected";
											} else if (whole == null && part == null && Language.DEFAULT.equals(id)) {
											selected = "selected";
											}
											
											// out.print("<option "+selected+" value=\""+id+"\">"+lang.getName()+"</option>");
											if(selected!="")
												out.print("<li id=\"selectedlang\" >");
											else
												out.print("<li>");
											out.print("<a><img id=\""+id+"\" alt=\"\" width=\"18px\" height=\"14px\" src=\"data:"+lang.getImageMime()+";base64,"+lang.getImageContent()+"\" />");
											out.print(lang.getName()+"</a></li>");
										}
										
										out.print("</ul>");
									%>
								
									<!--<a href="#" class="languagelink" id="flip"><img src="images/eng.png" alt="">English <span></span></a>
									<ul class="lang-block" id="langlink">
										<li><a href="#"><img src="images/arb.png" alt="">Arabic</a></li>
										<li><a href="#"><img src="images/eng.png" alt="">English</a></li>
									</ul>-->
								</div>
							</td>
							<td style="width:80px;">
								<input name="submit" type="submit" class="signin" value="Sign in" />
							</td>
						</tr>
					</table>
				</div>
            </form>
        </div>
		
		<% if (Config.SYSTEM_DEMO) { %>
			<jsp:include flush="true" page="login_demo_users.jsp"/>
		<% } %>
		
    </div>
    <div class="login-rightside"><img src="images/body-image.jpg" alt=""></div>
	<div class="clear"></div>
  </section>
  <!-- content end -->
  
  <!-- footer start -->
  <footer id="login-footer">
  	<div class="copyright">
    	Copyright &copy;2015 Emergency Medical Department | Technical Support Section<br>
    	<a href="http://www.ems.gov.kw/" target="_blank"> www.ems.gov.kw</a> | <a href="mailto:support@ems.gov.kw">support@ems.gov.kw</a> | Tel.: 24792221 | Fax: 24765615
    </div>
    <div class="foot_bottom">
   	  <div class="f-social">
        <a href="https://www.facebook.com/emskwt" class="facebook float" target="_blank"></a>
        <a href="https://twitter.com/emskuwait" class="twitter float" target="_blank"></a>
        <a href="https://www.youtube.com/emskw" class="youtube float" target="_blank"></a>
        <a href="https://instagram.com/EMSKuwait" class="instagram float" target="_blank"></a>
      </div>
      <a href="mailto:support@infofixsolution.com" class="infofixlogo"><img src="images/infofix2.png" alt=""></a>
      <p>System Powered by</p>
    </div>
  </footer>
  <!-- footer end -->
  
</div>
<!-- wrapper end -->

	<script type="text/javascript">
		function makeLowercase() {
			var username = document.getElementById('j_username'); 
			username.value = username.value.toLowerCase();
		}

		function setCookie() {
			var exdate = new Date();
			var value = document.getElementById('j_language').value;
			exdate.setDate(exdate.getDate() + 7);
			document.cookie = "lang=" + escape(value) + ";expires=" + exdate.toUTCString();
		}
	</script>

</body>
</html>
