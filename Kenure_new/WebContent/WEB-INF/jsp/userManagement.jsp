<!DOCTYPE>
<html lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>BLU Tower</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/dropkick.css" rel="stylesheet" type="text/css" />
<script src="js/jquery.min.js" type="text/javascript"></script>
<link href="css/jquery-ui.css" rel="stylesheet" type="text/css" />
<!-- [if IE]>
  <script type="text/javascript" src="js/html5.js"></script>
  <![endif] -->

<script src="js/jquery-ui.min.js"></script>
<script src="js/angular.js"></script>
<script src="js/dropkick.js" type="text/javascript"></script>
<script src="js/easy-tabs.js" type="text/javascript"></script>
<script src="js/custom.js" type="text/javascript"></script>
<script src="js/angular-ui-router.js"></script>
<script type="text/javascript" src="js/angular-cookies.js"></script>
<script src="js/html5.js"></script>
<script src="app/app.js"></script>
<script src="app/controller/controller.js"></script>

<script>
	$(function() {
		/* $("#datepicker1").datepicker();
		$("#datepicker2").datepicker(); */

		$("#from").datepicker({
			defaultDate : "+1w",
			changeMonth : true,
			dateFormat : 'dd-mm-yy',
			numberOfMonths : 1,
			onClose : function(selectedDate) {
				$("#to").datepicker("option", "minDate", selectedDate);
			}
		});
		$("#to").datepicker({
			defaultDate : "+1w",
			changeMonth : true,
			dateFormat : 'dd-mm-yy',
			numberOfMonths : 1,
			onClose : function(selectedDate) {
				$("#from").datepicker("option", "maxDate", selectedDate);
			}
		});

	});
</script>

</head>


<body data-ng-app="myApp">
	<div id="wrapper" data-ng-controller="logincontroller" data-ng-init="init()">
		<!--wrapper Start-->
		<div id="header">
			<div class="logo">
				<a href="#"><img src="images/logo.jpg" alt="" /></a>
			</div>
			<a class="menu-toggle" href="#menu"> <i class="menu-icon"> <span
					class="line1"></span> <span class="line2"></span> <span
					class="line3"></span>
			</i>
			</a>
			<div class="hd-right">
				<div class="user_info">
					<a href="#" class="user"><span class="user-img"><img
							alt="" src="images/user.jpg"></span> {{adminUser.userName}} </a>
					<ul class="user_link">
						<li><a href="#">My Profile</a></li>
						<li><a href="#">My Task</a></li>
						<li><a href="logout">Logout</a></li>
					</ul>
				</div>
			</div>
		</div>
		<div class="leftbar">
			<ul class="menulist">
				<li><a href="#" ><i><img
							src="images/ic-dashboard.png" alt="" /></i>
						<p>Dashboard</p></a></li>
				<li><a href="#"><i><img
							src="images/ic-customer_list.png" alt="" /></i>
						<p>Consumer List</p></a></li>
				<li><a href="#"><i><img
							src="images/ic-customer_mgmt.png" alt="" /></i>
						<p>Customer Management &amp; Admin Control Panel (ACP)</p></a></li>
				<li><a href="#"><i><img
							src="images/ic-controlpanel.png" alt="" /></i>
						<p>Customer Control Panel (CCP)</p></a></li>
				<li><a href="#" class="active"><i><img src="images/ic-user_mgmt.png"
							alt="" /></i>
						<p>User Management</p></a></li>
			</ul>
		</div>
		<div id="content">
			<!--Content Start-->
			<h1>
				Customer Management
				<div class="rightbtns">
					<a href="#" class="primary ic_add pull-right">Add</a>
				</div>
			</h1>
			<div class="middle">
				<div class="boxpanel">
					<span class="heading">Search</span>
					<div class="box-body">
						<div class="filter_panel">
							<div class="form-group auto">
								<label class="control-label">Data plan</label> <input
									type="text" id="from"
									class="form-control calendar-input half marginright"> <input
									type="text" id="to" class="form-control calendar-input half">
							</div>
							<div class="form-group">
								<label class="control-label">Data plan (annual)</label> <input
									type="text" class="form-control">
							</div>
							<div class="form-group">
								<label class="control-label">Customer Name</label> <input
									type="text" class="form-control">
							</div>
							<div class="form-group auto">
								<label class="control-label">&nbsp;</label>
								<button class="primary ic_search">Search</button>
								<button class="default ic_reset">Reset</button>
							</div>
						</div>

					</div>
				</div>

				<div class="boxpanel">
					<span class="heading">Search Listing</span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<th>Customer Name</th>
									<th>Datplan (annual)</th>
									<th>Plan Active Date</th>
									<th>Plan Expiry Date</th>
									<th>Action</th>
								</tr>
								<tr data-ng-repeat="x in userList ">
									<td>{{x.userName}}</td>
									<td>44.21</td>
									<td>21/Jul/2016</td>
									<td>21/Sep/2017</td>
									<td class="action"><a href="#" title="edit"><img
											alt="" src="images/edit-ic.png"></a>&nbsp; <a href="#"
										title="delete"><img alt="" src="images/delete-ic.png"></a>
									</td>
								</tr>
								
							</table>
							<div class="grid-bottom">
								<div class="show-record col-sm-3">
									<span class="records">Records:</span> <select
										class="form-control">
										<option>20</option>
										<option>50</option>
									</select>
								</div>
								<div class="paging">
									<ul>
										<li><a href="#" class="prevar"></a></li>
										<li><a href="#">1</a></li>
										<li><a href="#" class="active">2</a></li>
										<li><a href="#">3</a></li>
										<li><a href="#">4</a></li>
										<li><a href="#">5</a></li>
										<li><a href="#" class="nxtar"></a></li>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>

			</div>
			<!--Content end-->
		</div>


		<div id="footer">
			<!--footer start-->
			<p class="copyright">
				&copy; 2016 Blu Tower.&nbsp;&nbsp;<a href="#">Privacy</a><span>|</span><a
					href="#">Terms</a>
			</p>
			<!--footer end-->
		</div>
	</div>
	<!--wrapper end-->

</body>

</html>