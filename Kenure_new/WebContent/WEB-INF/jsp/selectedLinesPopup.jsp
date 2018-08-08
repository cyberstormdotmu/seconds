<html>
<head>
</head>
<body>
	<div class="status success" data-ng-show="isSuccess">
    	<span>{{successMessage}}</span>
    </div>
    <div class="status error" data-ng-show="isError">
    	<span>{{errorMessage}}</span>
    </div>
	<div class="modal-header">
		<h3 class="modal-title">Endpoint Parameters</h3>
	</div>
	<div class="modal-body">
		<form name="updateSelectedLinesForm">
			<div class="two_col">
				<div class="col" style="width: 50%;">
					<div class="form-horizontal">
						<div class="form-group">
							<label class="control-label">k-value:</label><input type="text" class="form-control" data-ng-model="fileLine.col9">
						</div>
						<div class="form-group">
							<label class="control-label">Direction:</label><input type="text" class="form-control" data-ng-model="fileLine.col10">
						</div>
						<div class="form-group">
							<label class="control-label">Utility Code:</label><input type="text" class="form-control" data-ng-model="fileLine.col11">
						</div>
						<div class="form-group">
							<label class="control-label">Usage Threshold:</label><input type="text" class="form-control" data-ng-model="fileLine.col12">
						</div>
						<div class="form-group">
							<label class="control-label">Usage Interval:</label><input type="text" class="form-control" data-ng-model="fileLine.col13">
						</div>
					</div>
				</div>
				<div class="col" style="width: 50%; float: right;">
					<div class="form-horizontal">
						<div class="form-group">
							<label class="control-label">Left Billing Digit:</label><input type="text" class="form-control" data-ng-model="fileLine.col14">
						</div>
						<div class="form-group">
							<label class="control-label">Right Billing Digit:</label><input type="text" class="form-control" data-ng-model="fileLine.col15">
						</div>
						<div class="form-group">
							<label class="control-label">Decimal Position:</label><input type="text" class="form-control" data-ng-model="fileLine.col16">
						</div>
						<div class="form-group">
							<label class="control-label">Leakage Threshold:</label><input type="text" class="form-control" data-ng-model="fileLine.col17">
						</div>
						<div class="form-group">
							<label class="control-label">BackFlow Limit:</label><input type="text" class="form-control" data-ng-model="fileLine.col18">
						</div>
					</div>
				</div>
			</div>
		</form>

	</div>
	<div class="modal-footer" >
		<button class="btn btn-default" type="button" data-ng-click="update()">Update</button>&nbsp;&nbsp;&nbsp;&nbsp;
		<button class="btn btn-default" type="button" data-ng-click="close()">Close</button>
	</div>
</body>
</html>