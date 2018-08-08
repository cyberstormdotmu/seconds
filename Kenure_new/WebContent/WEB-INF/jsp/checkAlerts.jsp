<html>
<head>
</head>
<body>
	<div class="modal-header">
		<h3 class="modal-title">Latest Alerts</h3>
	</div>
	<div class="modal-body">
		<div class="form-group">
			<div data-ng-repeat="item in alerts.split(',')">{{item}}</div>
		</div>
		<!-- Commented below known code -->
		<!-- <div class="form-group">
			<label class="control-label">Leak :</label> <label class="control-label">{{leak}}</label><input
				type="text" class="form-control"
				data-ng-model="leak" data-ng-trim="false"
				data-ng-change="leak = leak.split(' ').join('')"
				disabled><input type="hidden" data-ng-model="meterTransactionId" />
		</div>
		<div class="form-group">
			<label class="control-label">Tamper :</label> <input
				type="text" class="form-control"
				data-ng-model="tamper" data-ng-trim="false"
				data-ng-change="tamper = tamper.split(' ').join('')"
				disabled>
		</div>
		<div class="form-group">
			<label class="control-label">Backflow :</label> <input
				type="text" class="form-control"
				data-ng-model="backFlow" data-ng-trim="false"
				data-ng-change="backFlow = backFlow.split(' ').join('')"
				disabled>
		</div>
		<div class="form-group">
			<label class="control-label">Battery :</label> <input
				type="text" class="form-control"
				data-ng-model="battery" data-ng-trim="false"
				data-ng-change="battery = battery.split(' ').join('')"
				disabled>
		</div> -->
		
	</div>
	<div class="modal-footer">
		<span data-ng-if="!isAlertAcknowledged">
			<button class="btn btn-default" type="button" data-ng-click="acknowledge()">Acknowledge</button>
			&nbsp;&nbsp;&nbsp;&nbsp;
		</span>
		<span data-ng-if="isAlertAcknowledged">
			<button class="btn btn-default" type="button" data-ng-click="acknowledge()" disabled>Acknowledge</button>
			&nbsp;&nbsp;&nbsp;&nbsp;
		</span>
		<button class="btn btn-default" type="button" data-ng-click="close()">Close</button>
	</div>
</body>
</html>