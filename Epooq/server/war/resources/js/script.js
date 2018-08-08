var selectedChest = [];

$(document).ready(function() {
	
	$("#filterbutton").dropit();
	
	$("#myepooqlink").click(function(e) {
		/*$("#community-dialog").modal();
		return false;*/
	});
	
	$("#map-button").click(function(e) {
		e.preventDefault();

		count=0;
		var storyId=new Array();
		for(var i=0;i<window.storyTextMesh.length;i++){
			if(window.storyTextMesh[i].position.z<camera.position.z){
				storyId[count]=window.storyTextMesh[i].material.map.image.id;
				if(count==3){
					break;
				}
				count++;
			}
		}
		if(count < 3)
		{
			for(var i = count; i < 4; i++)
			{
				storyId[i] = -1;
			}
		}
		
		document.getElementById("storyId0").value = storyId[0];
		document.getElementById("storyId1").value = storyId[1];
		document.getElementById("storyId2").value = storyId[2];
		
		document.getElementById('storyform').submit();
		//$.post("mapView.html", { storyId0: storyId[0], storyId1: storyId[1], storyId2: storyId[2] } );

	});

	$("#connection-button").click(function(e) {
	
		$("#connection-layer").fadeIn(1000);
		//$("#connection-layer").toggle();
		$("#map-layer").hide();
		return true;
	});
	
	var searchTimer = null;
	$("#search-input").keyup(function(e) {
		clearTimeout(searchTimer);
		searchTimer = setTimeout(doSearch, 300);
	});
	
});

function doSearch() {
	searchString = $("#search-input").val();
}

$("#login_button").click(function(e) {
	alert("hello");
	return false;
});

