<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />


<title>Maritime Port Authority</title>
<link rel="stylesheet" href="../css/screen.css" type="text/css" media="screen" title="default" />
<link rel="stylesheet" href="../css/screen-style.css" type="text/css" media="screen" title="default" />
<link rel="stylesheet" href="../css/style.css" type="text/css" />
<!--[if IE]>
<link rel="stylesheet" media="all" type="text/css" href="css/pro_dropline_ie.css" />
<![endif]-->

<!--  jquery core -->
<script src="../js/jquery/jquery-1.4.1.min.js" type="text/javascript"></script>
 
<!--  checkbox styling script -->
<script src="../js/jquery/ui.core.js" type="text/javascript"></script>
<script src="../js/jquery/ui.checkbox.js" type="text/javascript"></script>
<script src="../js/jquery/jquery.bind.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	$('input').checkBox();
	$('#toggle-all').click(function(){
 	$('#toggle-all').toggleClass('toggle-checked');
	$('#mainform input[type=checkbox]').checkBox('toggle');
	return false;
	});
});
</script>

<!--  styled select box script version 1 -->
<script src="../js/jquery/jquery.selectbox-0.5.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function() {
	$('.styledselect').selectbox({ inputClass: "selectbox_styled" });
});
</script>
 

<script language="javascript">

document.onmousedown=disableclick;
function disableclick(event)
{
  if(event.button==2)
   {
     return false;    
   }
}

</script>

<!--  styled select box script version 2 --> 
<script src="../js/jquery/jquery.selectbox-0.5_style_2.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function() {
	$('.styledselect_form_1').selectbox({ inputClass: "styledselect_form_1" });
	$('.styledselect_form_2').selectbox({ inputClass: "styledselect_form_2" });
});
</script>

<!--  styled select box script version 3 --> 
<script src="../js/jquery/jquery.selectbox-0.5_style_2.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function() {
	$('.styledselect_pages').selectbox({ inputClass: "styledselect_pages" });
});
</script>

<!--  styled file upload script --> 
<script src="../js/jquery/jquery.filestyle.js" type="text/javascript"></script>
<script type="text/javascript" charset="utf-8">
$(function() {
	$("input.file_1").filestyle({ 
	image: "images/forms/upload_file.gif",
	imageheight : 29,
	imagewidth : 78,
	width : 300
	});
});
</script>

<!-- Custom jquery scripts -->
<script src="../js/jquery/custom_jquery.js" type="text/javascript"></script>
 
<!-- Tooltips -->
<script src="../js/jquery/jquery.tooltip.js" type="text/javascript"></script>
<script src="../js/jquery/jquery.dimensions.js" type="text/javascript"></script>
<script type="text/javascript">
$(function() {
	$('a.info-tooltip ').tooltip({
		track: true,
		delay: 0,
		fixPNG: true, 
		showURL: false,
		showBody: " - ",
		top: -35,
		left: 5
	});
});
</script> 

<!--  date picker script -->
<link rel="stylesheet" href="../css/datePicker.css" type="text/css" />
<script src="../js/jquery/date.js" type="text/javascript"></script>
<script src="../js/jquery/jquery.datePicker.js" type="text/javascript"></script>
<script type="text/javascript" charset="utf-8">

</script>

<!-- MUST BE THE LAST SCRIPT IN <HEAD></HEAD></HEAD> png fix -->
<script src="../js/jquery/jquery.pngFix.pack.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function(){
$(document).pngFix( );
});
</script>
</head>
<body oncontextmenu="return false">
<noscript>
  <div id="divJavaScript" class="DisabledJavaScript">        
   This Application works best with JavaScript enabled so please Enable it and then referesh the page !
  </div>
  <style type="text/css">
  
  	.b1{
  	display: none;
  	}
  	
  	.DisabledJavaScript 
  {  
  position: fixed; z-index: 101; top: 0; left: 0; right: 0; background: #ffc; text-align: center;line-height: 1.5; overflow: hidden;-webkit-box-shadow: 0 0 5px black; -moz-box-shadow:0 0 5px black;
  box-shadow: 0 0 5px black;font-weight:bold ; font-size :12px; color:#c04545; vertical-align: middle; padding-bottom:5px; padding-top:5px; 
  }
  	
  	
 </style>
 </noscript>
 <div class = "b1">
<table width="100%">
<tr><td><tiles:insertAttribute name="header"/></td></tr>
<tr><td><tiles:insertAttribute name="body"/></td></tr>
<tr><td></td></tr>
<tr><td><tiles:insertAttribute name="footer"/></td></tr>
</table>
</div>
</body>
</html>