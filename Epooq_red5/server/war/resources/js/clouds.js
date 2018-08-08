var camera, scene, renderer;
var cloudGeometry, cloudMaterial, mesh;
var mouseX = 0, mouseY = 0;
var goForward = false;
var goBackward = false;
var raycaster, projector;

var mouse = new THREE.Vector2(), INTERSECTED;
var mouseDown = false;

var cloudGroup, objectGroup;
var canvasDom;

var speed = 0;
var sliderBlock = false;

var chestTexture, chestMaterial;
var anchorTexture, anchorMaterial;
var keyTexture, keyMaterial;
var sliderMouseOut = false;

var textArray = new Array();

$(document).ready(function() {
	
	centerNav();
	
	canvasDom = $("#canvas");
	
	// Resize
	var resizeTimer = null;
	$(window).resize(function() {
		clearTimeout(resizeTimer);
		resizeTimer = setTimeout(onResize, 100);
	});
	
	$(window).mousemove(function(e) {
		onMousemove(e);
	});
	
	$(window).mousedown(function() {
		mouseDown = true;
	}).bind('mouseup mouseleave', function() {
		mouseDown = false;
	});
	
	$("#nav-slider").mousedown(function() {
		sliderBlock = true;
		sliderMouseOut = false;
	}).bind('mouseup mouseleave', function(e) {
		sliderBlock = false;
		
		// reset slider
		$("#nav-slider").slider('value',0);
		speedDecrease();
		sliderMouseOut = true;
	});
	
	$("#nav-slider").slider({
		orientation : "vertical",
		range : "min",
		min : -5,
		max : 5,
		value : 0,
		slide : function(event, ui) {
			if(!sliderMouseOut) {
				speed = -ui.value;
			} else {
				ui.value = 0;
				return false;
			}
		}
	});
	
	// Info close
	$("#info-close").click(function(e) {
		$("#info").hide();
		$("#add-anchor-link").hide();
		renderCommunity(-1);
		return false;
	});
	
	init();
	animate();

	// Objects
	renderCommunity(selectedCommunity);
});

function centerNav() {
	
	var windowHeight = $(window).height();
	var navHeight = $("#nav").height();
	
	$("#nav").css("top", parseInt(windowHeight/2 - navHeight/2)+"px");
	
}

var speedTimer;
function speedDecrease() {
	clearTimeout(speedTimer);
	
	if(speed < 0) {
		speed++;
	}
	
	if(speed > 0) {
		speed--;
	}
	
	if(Math.abs(speed) > 0) {
		speedTimer = setTimeout(speedDecrease, 100);
	}
}

function init() {

	// Camera
	camera = new THREE.PerspectiveCamera(45, canvasDom.width() / canvasDom.height(), 1, 5000);
	camera.position.x = 0;
	camera.position.y = 35;
	camera.position.z = -5000;

	// Scene
	scene = new THREE.Scene();
	objectGroup = new THREE.Object3D();

	// Clouds
	renderClouds();
	
	// Years
	renderYears();
	
	initObjectMaterials();
	
	projector = new THREE.Projector();
	raycaster = new THREE.Raycaster();

	// Renderer
	renderer = new THREE.WebGLRenderer({ antialias: false });
	renderer.setSize(canvasDom.width(), canvasDom.height());

	canvasDom.append(renderer.domElement);
}

function initObjectMaterials() {
	
	// Chest
	chestTexture = THREE.ImageUtils.loadTexture('resources/img/chest.png', new THREE.UVMapping());
	chestMaterial = new THREE.MeshBasicMaterial({
		map : chestTexture,
		transparent: true,
		depthTest: false
	});
	
	// Anchor
	anchorTexture = THREE.ImageUtils.loadTexture('resources/img/anchor.png', new THREE.UVMapping());
	anchorMaterial = new THREE.MeshBasicMaterial({
		map : anchorTexture,
		transparent: true,
		depthTest: false
	});
	
	// Key
	keyTexture = THREE.ImageUtils.loadTexture('resources/img/key.png', new THREE.UVMapping());
	keyMaterial = new THREE.MeshBasicMaterial({
		map : keyTexture,
		transparent: true,
		depthTest: false
	});	
}

function renderClouds() {
	// Cloud Geometry
	cloudGeometry = new THREE.Geometry();
	
	cloudGroup = new THREE.Object3D();
	var cloudTexture = THREE.ImageUtils.loadTexture('resources/img/cloud.png', new THREE.UVMapping());

	var cloudMaterial = new THREE.MeshBasicMaterial({
		map : cloudTexture,
		transparent: true,
		depthTest: false
	});

	var cloudMesh = new THREE.Mesh(new THREE.PlaneGeometry(64, 64));
	for (var i = 0; i < 20000; i++) {
		cloudMesh.position.x = Math.random() * 2000 - 1000;
		cloudMesh.position.y = -Math.random() * Math.random() * 100 - 15;
		cloudMesh.position.z = i;
		cloudMesh.rotation.z = Math.random() * Math.PI;
		cloudMesh.scale.x = cloudMesh.scale.y = Math.random() * Math.random()
				* 1.5 + 0.5;

		THREE.GeometryUtils.merge(cloudGeometry, cloudMesh);
	}

	mesh = new THREE.Mesh(cloudGeometry, cloudMaterial);
	mesh.position.z = -20000;
	cloudGroup.add(mesh);
	
	mesh = new THREE.Mesh(cloudGeometry, cloudMaterial);
	mesh.position.z = -40000;
	cloudGroup.add(mesh);
	
	scene.add(cloudGroup);
}

function renderCommunity(id) {
	
	var group = new THREE.Object3D();
	
	var geometry;
	
	var url = "api/getCommunity.html";
	
	if(id != -1) {
		url += "?id=" + id;
	}
	
	if(searchString != null && searchString != "") {
		if(url.indexOf("?") != -1) {
			url += "&";
		} else {
			url += "?";
		}
		url += "search=" + searchString;
	}
	
	var get = $.getJSON(url);
	get.fail(function() {
		alert("Ei yhteyttä palvelimeen");
	});
	get.done(function(data) {
		
		selectedCommunity = id;
		
		// Chest
		$.each(data[0].stories, function(i, l) {
			
			var obj = data[0].stories[i];
			
			var date = new Date(obj.date);

			geometry = new THREE.PlaneGeometry( 183, 124 );
			object = new THREE.Mesh( geometry, chestMaterial );
					
			object.type = "chest";
			object.id = obj.id;
			object.title = obj.title;
			object.place = obj.place;
			object.date = date;
				
			object.position.x = Math.random() * 200 - 100;
			object.position.y = 35;
			object.position.z = (2020 - date.getFullYear()) * -100;
			object.scale.x = object.scale.y = 0.5;
				
			group.add(object);
			
			// Chest text
			var canvas = document.createElement('canvas');
			canvas.width = 200;
			canvas.height = 100;
			var context = canvas.getContext('2d');
			context.font = '32px Signika Negative';
			context.fillStyle = '#ffffff';
			context.strokeStyle = '#ffffff';
			
			context.shadowColor = "#76a0ca";
			context.shadowOffsetX = 0;
			context.shadowOffsetY = 0;
			context.shadowBlur = 1;
			
			wrapText(context, obj.title, 0, 30, 200, 25);
			
			var texture = new THREE.Texture(canvas);
			texture.needsUpdate = true;
			var textMaterial = new THREE.MeshBasicMaterial({
				map : texture,
				transparent: true,
				depthTest: false
			});
			var shape = new THREE.PlaneGeometry(200, 100);
			var text = new THREE.Mesh(shape, textMaterial);
			
			text.position.x = 200;
			text.position.y = 0;
			text.position.z = i + 5;
			text.rotation.z = 0 * Math.PI;
			
			text.parentz = object.position.z;
			
			// Add to list of meshes
			textArray.push(text);
			
			object.add(text);
		});
		
		// Anchor
		$.each(data[0].anchors, function(i, l) {
			
			var obj = data[0].anchors[i];
			
			var date = new Date(obj.date);

			// Anchor
			geometry = new THREE.PlaneGeometry( 276, 279 );
			object = new THREE.Mesh( geometry, anchorMaterial );
					
			object.type = "anchor";
			object.title = obj.title;
			object.id = obj.id;
			object.place = obj.place;
			object.date = date;
			object.pictureId = obj.pictureId;
			object.content = obj.content;
				
			object.position.x = Math.random() * 200 - 100;
			object.position.y = 35;
			object.position.z = (2020 - date.getFullYear()) * -100;
			object.scale.x = object.scale.y = 0.5;
				
			group.add(object);
			
			// Anchor text
			var canvas = document.createElement('canvas');
			canvas.width = 200;
			canvas.height = 100;
			var context = canvas.getContext('2d');
			context.font = '32px Signika Negative';
			context.fillStyle = '#ffffff';
			context.strokeStyle = '#ffffff';
			
			context.shadowColor = "#76a0ca";
			context.shadowOffsetX = 0;
			context.shadowOffsetY = 0;
			context.shadowBlur = 1;
			
			wrapText(context, obj.title, 0, 30, 200, 25);
			
			var texture = new THREE.Texture(canvas);
			texture.needsUpdate = true;
			var textMaterial = new THREE.MeshBasicMaterial({
				map : texture,
				transparent: true,
				depthTest: false
			});
			var shape = new THREE.PlaneGeometry(200, 100);
			var text = new THREE.Mesh(shape, textMaterial);
			
			text.position.x = 150;
			text.position.y = 0;
			text.position.z = i + 5;
			text.rotation.z = 0 * Math.PI;
			
			text.parentz = object.position.z;
			
			// Add to list of meshes
			textArray.push(text);
			
			object.add(text);
		});
		
		// Key
		$.each(data[0].keys, function(i, l) {
			
			var obj = data[0].keys[i];
			
			var date = new Date(obj.date);

			geometry = new THREE.PlaneGeometry( 183, 124 );
			object = new THREE.Mesh( geometry, keyMaterial );
					
			object.type = "key";
			object.id = obj.id;
			object.question = obj.question;
			object.date = date;
			object.pictureId = obj.pictureId;
				
			object.position.x = Math.random() * 200 - 100;
			object.position.y = 35;
			object.position.z = (2020 - date.getFullYear()) * -100;
			object.scale.x = object.scale.y = 0.5;
				
			group.add(object);
		});
		
		// Don't show info bar when in public community
		if(data[0].id != -1) {
			
			$("#info-content").text(communitySelectedLabel.replace("[COMMUNITY]", data[0].name));
			$("#info").show();
			$("#add-anchor-link").show();
			$("#add-anchor-link").css('display', 'inline-block');
		}
		
		scene.remove(objectGroup);
		objectGroup = group;
		scene.add(objectGroup);
	});
}

function wrapText(context, text, x, y, maxWidth, lineHeight) {
    var words = text.split(' ');
    var line = '';

    for(var n = 0; n < words.length; n++) {
      var testLine = line + words[n] + ' ';
      var metrics = context.measureText(testLine);
      var testWidth = metrics.width;
      if (testWidth > maxWidth && n > 0) {
        context.fillText(line, x, y);
        line = words[n] + ' ';
        y += lineHeight;
      }
      else {
        line = testLine;
      }
    }
    context.fillText(line, x, y);
  }

function renderYears() {
	yearGroup = new THREE.Object3D();
	for ( var i = 0; i > -20000; i = i - 1000) {
		var canvas = document.createElement('canvas');
		canvas.width = 200;
		canvas.height = 200;
		var context = canvas.getContext('2d');
		context.font = '64px Signika Negative';
		context.fillStyle = '#ffffff';
		context.strokeStyle = '#ffffff';
		
		context.shadowColor = "#76a0ca";
		context.shadowOffsetX = 0;
		context.shadowOffsetY = 0;
		context.shadowBlur = 1;
		
		context.fillText(parseInt(2020 - (-1 * i) / 100), 25, 50);
		
		var texture = new THREE.Texture(canvas);
		texture.needsUpdate = true;
		var cover = new THREE.MeshBasicMaterial({
			map : texture,
			transparent: true,
			depthTest: false
		});
		var shape = new THREE.PlaneGeometry(100, 100);
		var banner = new THREE.Mesh(shape, cover);
		
		banner.position.x = 0;
		banner.position.y = 75;
		banner.position.z = i;
		banner.rotation.z = 0 * Math.PI;
		banner.scale.x = banner.scale.y = 2;
		yearGroup.add(banner);
	}
	scene.add(yearGroup);
}

function animate() {

	// note: three.js includes requestAnimationFrame shim
	requestAnimationFrame(animate);
	
	render();
}

function render() {
	
	
    if(speed != 0) {
    	if(textArray.length > 0 && textArray[0].material.opacity == 1) {
    		for(var i = 0; i < textArray.length; i++) {
    			textArray[i].material.opacity = 0;
    		}
    	}
    	
        camera.position.y += - mouseY * 0.01;
        if (camera.position.y > 50) {
        	camera.position.y = 50;
        }
        
        if (camera.position.y < 25) {
        	camera.position.y = 25;
        }

        camera.position.z = (camera.position.z + 8*speed);
    } else {
    	if(textArray.length > 0 && textArray[0].material.opacity == 0) {
	    	for(var i = 0; i < textArray.length; i++) {
	    		if(isNearEnoughCamera(camera, textArray[i].parentz)) {
	    			textArray[i].material.opacity = 1;
	    		}
	    	}
    	}
    }
    
	
	// find intersections
	var vector = new THREE.Vector3( mouse.x, mouse.y, 1 );
	projector.unprojectVector( vector, camera );
	
	raycaster.set( camera.position, vector.sub( camera.position ).normalize() );

	var intersects = raycaster.intersectObjects( objectGroup.children, true );
	
	if ( mouseDown && sliderBlock == false && intersects.length > 0) {
		
		if ( INTERSECTED != intersects[ 0 ].object ) {
			INTERSECTED = intersects[ 0 ].object;
			
			showModal(INTERSECTED);
		}
	} else {
		INTERSECTED = null;
	}

	renderer.render( scene, camera );
}

function isNearEnoughCamera(camera, z) {
	return (Math.abs(camera.position.z - z) < 750);
}

function onResize(event) {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(canvasDom.width(), canvasDom.height());
    
    centerNav();
}

function onMousemove(event) {
	event.preventDefault();
	
    mouseX = (event.clientX - (window.innerWidth/2)) * 0.3;
    mouseY = (event.clientY - (window.innerHeight/2)) * 0.2;
    
	mouse.x = 	( event.clientX / window.innerWidth ) * 2 - 1;
	mouse.y = - ( event.clientY / window.innerHeight ) * 2 + 1;
}

function showModal(obj) {
	
	if(obj.type == "chest") {

		$("#nav-slider").slider('value',0);
		speedDecrease();
		
		$("#content-title").text(obj.title);
		$("#content-time").text(unixTimeToString(obj.date));
		$("#content-place").text(obj.place);
		
		$("#content-dialog").modal();
		
		videoId = obj.id;
		initFlashVideo("playback");
	}
	
	if(obj.type == "anchor") {
		showDialog(obj.title, obj.place, obj.date, obj.pictureId, obj.content, true, obj);
	}
	
	if(obj.type == "key") {
		showDialog(obj.question, null, null, obj.pictureId, null, false, null);
	}
}

function showDialog(title, place, date, pictureId, content, editLink, obj) {
	$("#nav-slider").slider('value',0);
	speedDecrease();

	// Title
	$("#anchor-show-title").text(title);
	
	// Place
	if(place != null) {
		$("#anchor-show-place").show();
		$("#anchor-show-place").text(place + ", ");
	} else {
		$("#anchor-show-place").hide();
	}
	
	// Date
	if(date != null) {
		$("#anchor-show-time").show();
		var curr_date = date.getDate();
		var curr_month = date.getMonth() + 1; //Months are zero based
		var curr_year = date.getFullYear();
		$("#anchor-show-time").text(curr_date + "." + curr_month + "." + curr_year);
	} else {
		$("#anchor-show-time").hide();
	}
	
	// Picture
	if(pictureId != -1) {
		$("#anchor-show-picture").show();
		$("#anchor-show-picture").attr("src", "picture.html?id="+pictureId);
	} else {
		$("#anchor-show-picture").hide();
		//$("#anchor-show-picture").attr("src", "resources/img/anchor.png");
	}
	
	// Content
	if(content != null) {
		$("#anchor-show-content").show();
		$("#anchor-show-content").text(content);
	} else {
		$("#anchor-show-content").hide();
	}
	
	// Edit link
	if(editLink) {
		$("#anchor-show-edit-link").show();
		$("#anchor-show-edit-link").unbind("click");
		$("#anchor-show-edit-link").click(function(e) {
			setTimeout(function(){addAnchor(obj);}, 400);
			return false;
		});
	} else {
		$("#anchor-show-edit-link").hide();
	}
	
	$("#anchor-show-dialog").modal();
}

function unixTimeToString(unixtime) {
	var d = new Date(unixtime);
    var curr_date = d.getDate();
    var curr_month = d.getMonth() + 1;
    var curr_year = d.getFullYear();
    
    return curr_date + "." + curr_month + "." + curr_year;
}


function addAnchor(obj) {
	
	var id = -1;
	
	if(obj != null) {
		
		id = obj.id;
		
		var d = new Date(obj.date);
		$("#anchor-edit-title").attr("value", obj.title);
		$("#anchor-edit-date").attr("value", unixTimeToString(obj.date));
		$("#anchor-edit-place").attr("value", obj.place);
		$("#anchor-edit-content").text(obj.content);
	}
	
	$("#anchor-edit-id").attr("value", id);
	$("#anchor-edit-community-id").attr("value", selectedCommunity);
	$("#anchor-edit-dialog").modal();
}