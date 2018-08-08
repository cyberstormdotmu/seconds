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
		<h3 class="modal-title">Set Meter Reading Interval</h3>
	</div>
	<div class="modal-body">
		<div class="form-group">
			<label class="control-label dropdown-toggle">Mins</label> <select
				class="form-control "  data-ng-disabled="hours || days || startDate" data-ng-model="mins">
				<option value="">-- Select --</option>
				<option>20</option>
				<option>30</option>
				<option>40</option>
				<option>50</option>
				</select>
		</div>
		<div class="form-group">
			<label class="control-label dropdown-toggle">Hours</label> <select
				class="form-control "  data-ng-disabled="mins || days || startDate" data-ng-model="hours">
				<option value="">-- Select--</option>
				<option data-ng-repeat="h in hoursList">{{h}}</option>
				</select>
		</div>
		<div class="form-group">
			<label class="control-label dropdown-toggle">Days</label> <select
				class="form-control "  data-ng-disabled="mins || hours || startDate" data-ng-model="days">
				<option value="">-- Select --</option>
				<option data-ng-repeat="d in daysList">{{d}}</option>
				</select>
		</div>
		<div class="form-group">
			<label class="control-label dropdown-toggle">Select Date</label> 
			<input type="text" class="form-control calendar-input"
				data-uib-datepicker-popup="{{format}}"
				placeholder="-- Click here to Select Date --"
				data-ng-model="startDate"
				data-is-open="popup1.opened" data-datepicker-options=""
				onkeypress="return false" data-close-text="Close"
				data-ng-click="open1()"
				data-alt-input-formats="altInputFormats" data-ng-disabled="hours || days || mins">
		</div>
		<h5 class="modal-title">Select any one of the given options</h5>
	</div>
	<div class="modal-footer" >
		<button class="btn btn-default" type="button" data-ng-click="update()">Update</button>&nbsp;&nbsp;&nbsp;&nbsp;
		<button class="btn btn-default" type="button" data-ng-click="close()">Close</button>
	</div>
</body>
</html>