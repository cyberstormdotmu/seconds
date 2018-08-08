<html>
<head>
</head>
<body>
	<div class="modal-header">
		<h3 class="modal-title">Notes</h3>
	</div>
	<div class="modal-body">
		<div class="form-group">
			<label class="control-label">Add Note Text :</label> <label
				class="control-label">{{textNote}}</label><input type="text"
				class="form-control" data-ng-model="textNote" data-ng-trim="false"
				data-ng-change="textNote = textNote.split(' ').join('')"> <input
				type="hidden" data-ng-model="consumerMeterId" />
		</div>

		<div class="form-group">
			<!-- <input type="file" name="file" fileread="files"/> -->
			<input type="file" data-file-model="myFile" />
			<!-- <button data-ng-click = "uploadFile()" style="color: green; font-weight: 600;">Upload New Note</button> -->
		</div>



		<div data-ng-if="originalFileName">
			<div class="form-group" style="float: right; margin-right: 12px;">
				<label class="control-label">Remove previously uploaded Note ?
				</label> <input type="checkbox" data-ng-model="removeUpload"
					style="outline: 1px solid #1e5180"> <span></span>
			</div>
			<div class="form-group">
				<a target="_self" href="{{filePath}}"
					download="{{originalFileName}}" style="margin-left: 400px;">Download
					Uploaded Note</a>
			</div>
		</div>
		<div class="status success" data-ng-show="noteUploadCompleted">
			<span>Note successfully updated !</span>
		</div>
		<!-- Known code for file image preview -->
		<!-- <div data-ng-if="filePath.indexOf('.txt') == -1">
			<img alt="" src="{{filePath}}" style="height: 350px; width: 570px;">
		</div>
		<div data-ng-if="filePath.indexOf('.txt') !== -1">
			<iframe src="{{filePath}}" style="height: 350px; width: 570px;"></iframe>
		</div> -->

	</div>
	<div class="modal-footer">
		<button class="btn btn-default" type="button"
			data-ng-click="uploadFile()">Save Changes</button>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<button class="btn btn-default" type="button" data-ng-click="close()">Close</button>
	</div>
</body>
</html>