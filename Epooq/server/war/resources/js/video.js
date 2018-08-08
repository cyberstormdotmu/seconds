function initFlashVideo(id) {
	var flashvars = {};
	var params = {
			from : "video"
	};
	var attributes = {
		id : id,
		name : id,
		scale: "noborder",
		allowScriptAccess : "always"
	};

	swfobject.embedSWF("/epooq/resources/flash/epooqClient.swf", "playerContainer", 600, 338, "9.0.0", "/epooq/resources/flash/expressInstall.swf", flashvars, params, attributes);
}

function initFlashAudio(id) {
	var flashvars = {};
	var params = {
			from : "audio"
	};
	var attributes = {
		id : id,
		name : id,
		scale: "noborder",
		allowScriptAccess : "always"
	};

	swfobject.embedSWF("/epooq/resources/flash/epooqClient.swf", "playerContainer", 600, 338, "11.2", "/epooq/resources/flash/expressInstall.swf", flashvars, params, attributes);
}

