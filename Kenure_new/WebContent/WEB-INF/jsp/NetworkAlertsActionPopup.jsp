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
		<h3 class="modal-title">Alert Action Popup</h3>
	</div>
	<div class="modal-body">
		<div class="form-group">
			<ul>
				<li>&nbsp;&nbsp;&nbsp;<label class="checkbox"><input type="checkbox" data-ng-model="acknowledge"><span><h5 style="margin-top:5px;">&nbsp;Acknowledge</h5></span></label><br/></li>
				<li>&nbsp;&nbsp;&nbsp;<label class="checkbox"><input type="checkbox" data-ng-model="assignToTechnician"><span><h5 style="margin-top:5px;">&nbsp;Assign to Technician</h5></span></label></li>
			</ul>
		</div>
		<div class="form-group">
		<select  data-ng-model="technicianName" data-ng-init="getTechnicians()" class="form-control" required data-ng-disabled="!assignToTechnician">
		<option value="">--- Please Select Technician ---</option>
		<option data-ng-repeat="x in finalList track by $index" value="{{x.id}}" data-ng-if="x.status == true"><span>{{x.name}}</span></option>
		</select>
	  </div>
	
	</div>
	<div class="modal-footer" >
		<button class="btn btn-default" type="button" data-ng-click="updateNetworkMeter()">OK</button>&nbsp;&nbsp;&nbsp;&nbsp;
		<button class="btn btn-default" type="button" data-ng-click="close()">Close</button>
	</div>
</body>
</html>