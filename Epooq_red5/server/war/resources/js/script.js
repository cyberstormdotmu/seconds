$(document).ready(function() {
	
	$("#filterbutton").dropit();
	
	$("#myepooqlink").click(function(e) {
		$("#community-dialog").modal();
		return false;
	});
	
	$("#map-button").click(function(e) {
		$("#map-layer").toggle();
		$("#connection-layer").hide();
		return false;
	});

	$("#connection-button").click(function(e) {
		$("#connection-layer").toggle();
		$("#map-layer").hide();
		return false;
	});
	
	var searchTimer = null;
	$("#search-input").keyup(function(e) {
		clearTimeout(searchTimer);
		searchTimer = setTimeout(doSearch, 300);
	});
	
});

function doSearch() {
	searchString = $("#search-input").val();
	renderCommunity(selectedCommunity);
}
