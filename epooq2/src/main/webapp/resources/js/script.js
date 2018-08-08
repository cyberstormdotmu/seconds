var selectedChest = [];

$(document).ready(function() {
	
	$("#filterbutton").dropit();
	
	$("#myepooqlink").click(function(e) {
		$("#community-dialog").modal();
		return false;
	});
	
	$("#map-button").click(function(e) {
		count=0;
		var storyId=new Array();
		for(var i=0;i<=window.storyTextMesh.length;i++){
			if(window.storyTextMesh[i].position.z<camera.position.z){
				storyId[count]=window.storyTextMesh[i].material.map.image.id;
				if(count==2){
					break;
				}
				count++;
			}
		}
		$("#map-button").attr("href","mapView.html?storyId0="+storyId[0]+"&storyId1="+storyId[1]+"&storyId2="+storyId[2]);
		$("#map-button").click();
/*		alert("LENGTH "+window.selectedTextChestArray.length);
		alert("LENGTH "+window.selectedTextChestArray[window.selectedTextChestArray.length-1].object.material.map.image.id);
		alert("LENGTH "+window.selectedTextChestArray[window.selectedTextChestArray.length-2].material.map.image.id);
		alert("LENGTH "+window.selectedTextChestArray[window.selectedTextChestArray.length-3].material.map.image.id);*/
		/*for(var i=window.selectedTextChestArray.length-1;i >= 0;i--){
			count++;
			alert(i+"====="+selectedTextChestArray[i].material.map.image.id);
			if(count==2){
				break;
			}
		}*/
/*		alert("00 "+window.selectedTextChestArray[0].material.map.image.id);
		alert("11 "+window.selectedTextChestArray[1].material.map.image.id);
*/
/*		alert("SCRIPT selectedChestArray >>>>>>>>>>>"+window.selectedChestArray.length);
		alert("SCRIPT selectedChestArray >>>>>>>>>>>"+window.selectedChestArray[1].id);
		alert("SCRIPT selectedChestArray >>>>>>>>>>>"+window.selectedChestArray[2].id);
		alert("SCRIPT selectedChestArray >>>>>>>>>>>"+window.selectedChestArray[3].id);
		
		selectedChest=selectedChestArray;
		
		objects.push("$map-button");
		  var vector = new THREE.Vector3();



		  projector.unprojectVector( vector, camera );
		  raycaster.set( camera.position, vector.sub( camera.position ).normalize() );

		  var intersects = raycaster.intersectObjects(objects);

		  alert("intersects.length ;;;;;;; "+intersects.length);
		  if ( intersects.length > 0) {
		  }
		
		//$("#map-canvas").fadeIn(1000);
		$("#map-layer").fadeIn(1000);
		//$("#map-layer").toggle();
		$("#connection-layer").hide();
		
*/				
		/*
		$.ajax({
		    type: "POST", 
		    url: "/mapView.html",
		    data: "storytId1="+storyId[0]+",storytId2="+storyId[1]+",storytId3="+storyId[2],
		    success: function(data){  
		    	
		    },
		    error: function(X) { 
		    	
		    }       
		});*/

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

