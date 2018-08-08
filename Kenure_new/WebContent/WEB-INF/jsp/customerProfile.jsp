<!DOCTYPE>
<!--  -->
<html lang="en">

<head>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BLU Tower</title>
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/dropkick.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/toastr.css" rel="stylesheet" type="text/css" />

<script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/angular.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular-cookies.js"></script>

<script src="${pageContext.request.contextPath}/js/dropkick.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/custom.js" type="text/javascript"></script>

<!-- <script src="js/angular-ui-router.js"></script> -->

<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/controller.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/customercontroller.js"></script>

</head>

<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="customercontroller"
		data-ng-init="customerProfileInit()">
		<!--wrapper Start-->
		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>

		<div id="content">
			<!--Content Start-->
			<h1>Customer Profile</h1>
			<div class="middle">

				<div class="status success" data-ng-show="isSuccess">
					<span>{{success}}</span>
				</div>

				<div class="boxpanel">
					<span class="heading">Customer Profile</span>
					<div class="status success" data-ng-show="updatedSuccessfully">
						<span>record updated successfully</span>
					</div>
					<div class="box-body pad">

						<div class="two_col">
							<div class="col">
								<form method="post" name="updateCustomerForm">
									<div class="form-horizontal">

										<div class="form-group">
											<label class="control-label required">Customer First
												Name :</label> <input type="text" class="form-control"
												data-ng-model="firstName" maxlength="45" required>
										</div>

										<div class="form-group">
											<label class="control-label required">Customer Last
												Name :</label> <input type="text" class="form-control"
												data-ng-model="lastName" maxlength="45" required>
										</div>

										<div class="form-group">
											<label class="control-label">Username :</label> <input
												type="text" class="form-control" data-ng-model="userName"
												disabled>
										</div>

										<div class="form-group">
											<label class="control-label required">Phone1:</label> <input
												type="text" class="form-control"
												data-ng-model="cell_number1" maxlength="15" required>
										</div>

										<div class="form-group">
											<label class="control-label">Phone2:</label> <input
												type="text" class="form-control"
												data-ng-model="cell_number2" maxlength="15">
										</div>

										<div class="form-group">
											<label class="control-label">Phone3:</label> <input
												type="text" class="form-control"
												data-ng-model="cell_number3" maxlength="15">
										</div>

										<div class="form-group">
											<label class="control-label required">Email1:</label> <input
												type="email" class="form-control" data-ng-model="email1"
												data-ng-trim="false" maxlength="50" name="email1"
												data-ng-pattern="emailFormat" required>
										</div>

										<div class="form-group"
											data-ng-show="updateCustomerForm.email1.$error.pattern">
											<label class="control-label">&nbsp;</label> <label
												class="form-control status error">Please Insert
												Valid Email Address.</label>
										</div>

										<div class="form-group">
											<label class="control-label">Email2:</label> <input
												type="email" class="form-control" data-ng-model="email2"
												data-ng-trim="false" maxlength="50"
												data-ng-change="email2 = email2.split(' ').join('')">
										</div>

										<div class="form-group" data-ng-show="invalidemail2">
											<label class="control-label">&nbsp;</label> <label
												class="form-control status error">Please Insert
												Valid Email Address.</label>
										</div>

										<div class="form-group">
											<label class="control-label">Email3:</label> <input
												type="email" class="form-control" data-ng-model="email3"
												data-ng-trim="false" maxlength="50"
												data-ng-change="email3 = email3.split(' ').join('')">
										</div>

										<div class="form-group" data-ng-show="invalidemail3">
											<label class="control-label">&nbsp;</label> <label
												class="form-control status error">Please Insert
												Valid Email Address.</label>
										</div>

										<div class="form-group">
											<label class="control-label required">Address1:</label> <input
												type="text" class="form-control " maxlength="255"
												data-ng-model="address1" required>
										</div>

										<div class="form-group">
											<label class="control-label">Address2:</label> <input
												type="text" class="form-control" maxlength="255"
												data-ng-model="address2">
										</div>

										<div class="form-group">
											<label class="control-label">Address3:</label> <input
												type="text" class="form-control" maxlength="255"
												data-ng-model="address3">
										</div>

										<div class="form-group">
											<label class="control-label required">Street Name:</label> <input
												type="text" class="form-control" maxlength="255"
												data-ng-model="streetName" required>
										</div>

										<div class="form-group">
											<label class="control-label required">Zipcode :</label> <input
												type="text" class="form-control" maxlength="10"
												data-ng-model="zipcode" required>
										</div>

										<div class="form-group" data-ng-hide="isNormal">
											<label class="control-label">Data Plan:</label> <input
												type="text" class="form-control" data-ng-model="mbPerMonth"
												disabled>
										</div>

										<div class="form-group" data-ng-hide="isNormal">
											<label class="control-label">Plan Start Date:</label> <input
												type="text" class="form-control calendar-input"
												data-ng-model="dataPlanActivatedDate" disabled>
										</div>

										<div class="form-group" data-ng-hide="isNormal">
											<label class="control-label">Plan End Date:</label> <input
												type="text" class="form-control calendar-input"
												data-ng-model="dataPlanExpiryDate" disabled>
										</div>

										<div class="form-group" >
											<label class="control-label">&nbsp;</label>
											<button class="primary ic_submit"
												data-ng-click="updateCustomerProfile()"
												data-ng-model="updateCustomerProfile" type="submit">Update</button>
										</div>

									</div>

								</form>



							</div>
						</div>

					</div>
				</div>

			</div>
			<!--Content end-->
		</div>


		<div id="footer" data-ng-include data-src="'../footer'"></div>
	</div>
	<!--wrapper end-->
</body>
</html>

