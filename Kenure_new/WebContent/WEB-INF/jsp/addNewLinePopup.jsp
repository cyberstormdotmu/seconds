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
		<h3 class="modal-title">New Endpoint Parameters</h3>
	</div>
	<div class="modal-body">
		<form name="addNewEndpointForm">
			<div class="two_col">
				<div class="col" style="width: 50%;">
					<div class="form-horizontal">
						<div class="form-group">
							<label class="control-label">Consumer Account No:</label><input type="text" class="form-control" data-ng-model="fileLine.col0">
						</div>
						<div class="form-group">
							<label class="control-label">Street Name:</label><input type="text" class="form-control" data-ng-model="fileLine.col1">
						</div>
						<div class="form-group">
							<label class="control-label">Address2:</label><input type="text" class="form-control" data-ng-model="fileLine.col2">
						</div>
						<div class="form-group">
							<label class="control-label">Address3:</label><input type="text" class="form-control" data-ng-model="fileLine.col3">
						</div>
						<div class="form-group">
							<label class="control-label">Address4:</label><input type="text" class="form-control" data-ng-model="fileLine.col4">
						</div>
						<div class="form-group">
							<label class="control-label">Zipcode:</label><input type="text" class="form-control" data-ng-model="fileLine.col5">
						</div>
						<div class="form-group">
							<label class="control-label">District Meter ID:</label><input type="text" class="form-control" data-ng-model="fileLine.col6">
						</div>
						<div class="form-group">
							<label class="control-label">Last Meter Reading:</label><input type="text" class="form-control" data-ng-model="fileLine.col7">
						</div>
						<div class="form-group">
							<label class="control-label">Reading Timestamp:</label><input type="text" class="form-control" data-ng-model="fileLine.col8">
						</div>
						<div class="form-group">
							<label class="control-label">k-value:</label><input type="text" class="form-control" data-ng-model="fileLine.col9">
						</div>
					</div>
				</div>
				<div class="col" style="width: 50%; float:right;">
					<div class="form-horizontal">
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
		<button class="btn btn-default" type="button" data-ng-click="add()">Add</button>&nbsp;&nbsp;&nbsp;&nbsp;
		<button class="btn btn-default" type="button" data-ng-click="close()">Close</button>
	</div>
</body>
</html>