<html>
<head>
</head>
<body>
	<div class="modal-content">
		<div class="modal-header">
			<h3 class="modal-title">Upload Route File</h3>
		</div>
		<div class="modal-body">
			<form action="uploadRouteFile" method="post"
				enctype="multipart/form-data">
				<div class="form-group">
					<label class="lbl"> <input type="checkbox"
						style="outline: 1px solid #1e5180" name="isHeader"
						checked="checked"><span></span>Header Included?
						&nbsp;&nbsp;&nbsp;
					</label> <label class="control-label">&nbsp;</label> <input type="file"
						name="file" class="formfield" accept=".csv" />
				</div>
				<button type="submit" class="primary ic_submit btn btn-default">Upload
					Route File</button>
			</form>
		</div>
		<div class="modal-footer">
			<button class="btn btn-default" type="button" data-ng-click="close()">Close</button>
		</div>
	</div>
</body>
</html>