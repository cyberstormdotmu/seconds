<html>
<head>
</head>
<body>

	<div class="modal-body" data-ng-init="init()">
		<div style="margin: 0 260px;">
			<span><b><font size="4">Consumer Account Number : {{consumerAccountNo}}</font>
			<br>
			<font size="3">Graph Records For Date : {{displayDate}}</font>	
			</b>
			</span>
		</div>
		<div id="chart_div"></div>
	</div>
	<div class="modal-footer">
		<button class="btn btn-default" type="button" data-ng-click="close()">Close</button>
	</div>
</body>
</html>