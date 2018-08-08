<!DOCTYPE>
<!--  -->
<html lang="en">

<head>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BLU Tower</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<!-- [if IE]>
  <script type="text/javascript" src="js/html5.js"></script>
  <![endif] -->

<script src="js/custom.js" type="text/javascript"></script>


<script src="js/html5.js"></script>
<script>localStorage.path = "${pageContext.request.contextPath}";</script>
<script src="app/app.js"></script>
<script>
function onClickMenu(clicked_id) {
	localStorage.activeAdminMenu = clicked_id;
}

window.onload = (function(){
	
	if (localStorage.activeAdminMenu == "") {
		var a = document.getElementById("dashboard");
		if (a != null) {
			a.classList.add("active");
		}
	} else {
		var a = document.getElementById(localStorage.activeAdminMenu);
		if (a != null) {
			a.classList.add("active");
		}
	}

})();
</script>


</head>

<body>

	<div class="leftbar" id="leftbar">
		<ul class="menulist">

			<li class="nav">
				<a class="menu" id="dashboard"  href="installerDashboard" onclick="onClickMenu(this.id)">
					<i><img src="images/ic-dashboard.png" alt="" /></i> Dashboard
				</a>
			</li>

			<li class="nav">
				<a class="menu" id="endpointfilemanagement"	href="uploadDownloadEndpointRedirect" onclick="onClickMenu(this.id)">
					<i><img src="images/ic-upload_download.png" alt="" /></i>Upload / Download Endpoint File 
				</a>
			</li>

			<li class="nav">
				<a class="menu" id="dcfileManagement" href="uploadDownloadDCRedirect"  onclick="onClickMenu(this.id)">
					<i><img src="images/ic-upload_download.png" alt="" /></i>Upload / Download DataCollector File 
				</a>
			</li>

		</ul>
	</div>

</body>
</html>