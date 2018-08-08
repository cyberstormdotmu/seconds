function initFlashVideo(id) {
	var flashvars = {};
	var params = {};
	var attributes = {
		id : id,
		name : id,
		scale: "noborder",
		allowScriptAccess : "always"
	};

	swfobject.embedSWF("resources/flash/epooqClient.swf", "playerContainer", 600, 338, "9.0.0", "resources/flash/expressInstall.swf", flashvars, params, attributes);
}