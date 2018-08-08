
<!DOCTYPE>
<!--  -->
<html lang="en">

<head>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<title>BLU Tower</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/dropkick.css" rel="stylesheet" type="text/css" />
<link href="css/toastr.css" rel="stylesheet" type="text/css" />
<!-- [if IE]>
  <script type="text/javascript" src="js/html5.js"></script>
  <![endif] -->

<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/angular.js"></script>
<script src="js/angular-ui-router.min.js"></script>
<script type="text/javascript" src="js/angular-cookies.js"></script>

<script src="js/dropkick.js" type="text/javascript"></script>
<script src="js/custom.js" type="text/javascript"></script>

<script src="js/angular-ui-router.js"></script>
<script src="js/html5.js"></script>
<script src="js/toastr.js"></script>
<script src="app/app.js"></script>
<script src="app/controller/controller.js"></script>
<script src="app/controller/usercontroller.js"></script>
<base href="/Kenure/">

</head>
<script type="text/javascript">
$(document).ready(function() {
	  if('${successMsg}'){
	   $('#successMsgdiv').show();
	  }else if('${errorMsg}'){
	   $('#errorMsgdiv').show();
	  }
	  
	  setTimeout(function() {
	   $('#successMsgdiv').hide();
	   $('#errorMsgdiv').hide();
	     }, 3000);
	 });
</script>
<body data-ng-app="myApp">
	<div id="wrapper" data-ng-controller="logincontroller">
		<!--wrapper Start-->
		<div id="header" data-ng-include data-src="'header'"></div>
		<div id="customerleftNavigationbar" data-ng-include data-src="'installerLeftNavigationbar'"></div>
		
		<div id="content">
			<!--Content Start-->
			<h1>Upload/Download Endpoint Installation File </h1>
			<div class="middle">
				<div class="status success" id="successMsgdiv" style="display: none">
     				<span>${successMsg}</span>
    			</div>
			    <div class="status error" id="errorMsgdiv" style="display: none">
			     	<span>${errorMsg}</span>
			    </div>
				<div class="boxpanel">
					<div class="box-body pad">
						<form action="downloadEndpointForInstaller" method="post">
							<div class="">
								<div class="col">
									<div class="form-horizontal">
										<div class="form-group">
											<label class="control-label">&nbsp;</label>
											<input type="hidden" value="${installer.installerId}" name="installerId">
											<!-- <label class="control-label">Header Included </label> 
											<label class="lbl"><input type="checkbox" style="outline: 1px solid #1e5180" name="includeHeader" checked="checked"><span></span>Header Included? &nbsp;&nbsp;&nbsp;</label>
											 --><button type="submit" class="primary ic_submit">DownLoad Endpoint File</button>
										</div>
									</div>
								</div>
							</div>
						</form>
						
						
							<form action="uploadEndpointForInstaller" method="post" enctype="multipart/form-data">
							<div class="">
								<div class="col">
									<div class="form-horizontal">
										<div class="form-group">
											<label class="control-label">&nbsp;</label>
											<input type="hidden" value="${installer.installerId}" name="installerId">
										 	<label class="lbl"><input type="checkbox" style="outline: 1px solid #1e5180" name="includeHeader" checked="checked"><span></span>Header Included? &nbsp;&nbsp;&nbsp;</label> 
											<input type="file"  name="file" class="formfield" accept=".csv"/> 
											<button type="submit" class="primary ic_submit">Upload Endpoint File</button>
										</div>
									</div>
								</div>
							</div>
						</form>
						
					</div>
				</div>
			</div>
		</div>
		<div id="footer" data-ng-include data-src="'footer'"></div>
	</div>
	<!--wrapper end-->

</body>

