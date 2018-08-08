<html>
<head>
</head>
<body>
	<div class="modal-header">
		<h3 class="modal-title">Set Meter Reading Interval</h3>
	</div>
	<div class="modal-body">
		<div class="form-group">

							<label class="control-label required">Password : </label> <input
								type="password" class="form-control" name="password"
								data-ng-model="password"
								data-ng-change="password = password.split(' ').join('')"
								data-ng-trim="false" required>
						</div>
						<div class="forgot-psw">
							<label class="checkbox"><input type="checkbox"><span></span>Remember
								me</label> <a href="forgetPasswordForm" title="Forgot Password?">Forgot
								Password?</a>
						</div>

	</div>
	<div class="modal-footer" >
		<button class="btn btn-default" type="button" data-ng-click="update()">Update</button>&nbsp;&nbsp;&nbsp;&nbsp;
		<button class="btn btn-default" type="button" data-ng-click="close()">Close</button>
	</div>
</body>
</html>