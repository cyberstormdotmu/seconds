var scene,camera,renderer;
var canvasDom;
var mouseX = 0, mouseY = 0;
var mouse=new THREE.Vector2();
var sliderBlock = false;
var sliderMouseOut=false;
var speed = 0;
var cloudGeometry, cloudMaterial;
var cloudGroupe,objectGroupe;
var textArray = new Array();
var storiesList=new Array();

$(document).ready(function(){

	centerNav();

	canvasDom=$("#canvas");

	//resize browser window
	var resizeTimer=null;
	$(window).resize(function(){

		clearTimeout(resizeTimer);
		resizeTimer=setTimeout(onResize, 0);
	});

	$(window).mousemove(function(e){
		onMousemove(e);
	});

	$(window).mousedown(function(){
		mouseDown=true;
	}).bind('mouseup mouseleave',function(){
		mouseDown=false;
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
				console.log("Speed="+speed);
			} else {
				ui.value = 0;
				return false;
			}
		}
	});

	init();
	animate();
	
	
	document.addEventListener( 'mousedown', onDocumentMouseDown, false );

});


function onDocumentMouseDown( event ) {

	event.preventDefault();

	var vector = new THREE.Vector3( ( event.clientX / window.innerWidth ) * 2 - 1, - ( event.clientY / window.innerHeight ) * 2 + 1, 0.5 );
	projector.unprojectVector( vector, camera );

	var raycaster = new THREE.Raycaster( camera.position, vector.sub( camera.position ).normalize() );

	var intersects = raycaster.intersectObjects( objects );

	if ( intersects.length > 0 ) {

		intersects[ 0 ].object.material.color.setHex( Math.random() * 0xffffff );

		var particle = new THREE.Sprite( particleMaterial );
		particle.position = intersects[ 0 ].point;
		particle.scale.x = particle.scale.y = 16;
		scene.add( particle );

	}

	/*
	// Parse all the faces
	for ( var i in intersects ) {

		intersects[ i ].face.material[ 0 ].color.setHex( Math.random() * 0xffffff | 0x80000000 );

	}
	*/
}


function centerNav(){

	var windowHeight=$(window).height();
	var navHeight=$("#nav").height();

	$("#nav").css("top", parseInt(windowHeight/2 - navHeight/2)+"px");

}
var speedTimer;
function speedDecrease(){

	clearTimeout(speedTimer);

	if(speed<0){
		speed++;
	}

	if(speed>0){
		speed--;
	}
	if(Math.abs(speed) > 0) {
		speedTimer = setTimeout(speedDecrease, 100);
	}
}
function init(){

	//initialize camera
	//PerspectiveCamera(fiels of view, aspect ratio, near,far)
	camera=new THREE.PerspectiveCamera(45, canvasDom.width() / canvasDom.height(), 1, 2000);

	camera.position.x=0;
	camera.position.y=35;
	camera.position.z=-5000;

	//create scene
	scene=new THREE.Scene();
	objectGroup = new THREE.Object3D();

	//clouds
	renderClouds();

	//years
	renderYears();

	//renderer
	renderer=new THREE.WebGLRenderer();
	renderer.setSize(canvasDom.width(), canvasDom.height());
	canvasDom.append(renderer.domElement);
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

function renderYears() {


	
	$.ajax({
		   url:"story/list.html",
		   success:function(result){
			   storiesList = jQuery.parseJSON(result);
			   yearGroup = new THREE.Object3D();
				var distanceYear=1000;
			
				for ( var i = 0; i > -20000; i = i - distanceYear) {
			
					// to print years
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
			
					year=parseInt(2020 - (-1 * i) / 100);
					context.fillText(year, 25, 50);
			
			
			
					var texture = new THREE.Texture(canvas);
					texture.needsUpdate = true;
					var cover = new THREE.MeshBasicMaterial({
						map : texture,
						transparent: true,
						depthTest: false
					});
			
					var shape = new THREE.PlaneGeometry(100, 100);
					var banner = new THREE.Mesh(shape, cover);
			
					banner.position.x = -5;
					banner.position.y = 75;
					banner.position.z = i;
					banner.rotation.z = 0 * Math.PI;
					banner.scale.x = banner.scale.y = 0.5;
					yearGroup.add(banner);
					var numberOfStories=getNumberOfStoriesForYear(year);
					if(numberOfStories!=0){
			
						var distanceStory=distanceYear/numberOfStories;
						var startLimit=i+1000;
						var endLimit=i;
						var temp=0;
						for(var j=0;j<storiesList.length;j++){
							if((storiesList[j].year>year  && storiesList[j].year<(year+10)) || year==(storiesList[j].year)){
								var storyImageTexture = THREE.ImageUtils.loadTexture('resources/img/chest.png', new THREE.UVMapping());
								storyImageTexture.id=year;
								storyImageTexture.needsUpdate = true;
								var storyImageMaterial = new THREE.MeshBasicMaterial({
									map : storyImageTexture,
									transparent: true,
									opacity:0.8,
									depthTest: false
								});
								var storyImagePlaneGeometry = new THREE.PlaneGeometry(183,124);
								var storyImageMesh= new THREE.Mesh(storyImagePlaneGeometry, storyImageMaterial);
			
								var x=getRandomPoints(-100,100);
								var y=getRandomPoints(0,50);
								var z=i;
								if(!(year==(storiesList[j].year))){
									startLimit=startLimit-temp;
									endLimit=startLimit-distanceStory;
									temp=distanceStory;
									z=getRandomPoints(endLimit,startLimit);
								}
			
								storyImageMesh.position.x = x;
								storyImageMesh.position.y = y;
								storyImageMesh.position.z = z-1;
								storyImageMesh.rotation.z = 0 * Math.PI;
								storyImageMesh.scale.x = storyImageMesh.scale.y = 0.4;
								yearGroup.add(storyImageMesh);
								
			
								//text on chest
			
								var storyTextCanvas = document.createElement('canvas');
								storyTextCanvas.id="canvas"+year;
								var storyTextContext = storyTextCanvas.getContext('2d');
			
								canWidth=350;
								canheight=220;
								storyTextCanvas.width = canWidth;
								storyTextCanvas.height = canheight;
								storyTextContext.font = 'bold 36px Signika Negative';
								storyTextContext.fillStyle = "white";
								storyTextContext.globalAlpha=0.8;
			
							
								storyTextContext.fillRect((canWidth/2)-100,(canheight/2)-35,220,70);
			
								storyTextContext.fillStyle = "red";
								
								storyTextContext.fillText(storiesList[j].title,(canWidth/2)-100,(canheight/2));
								storyTextContext.fill();
			
			
								var storyTextTexture = new THREE.Texture(storyTextCanvas);
								storyTextTexture.needsUpdate = true;
								var storyTextMaterial= new THREE.MeshBasicMaterial({
									map : storyTextTexture,
									transparent: true,
									depthTest: false
								});
								var storyTextShape = new THREE.PlaneGeometry(200,75);
								storyTextShape.computeBoundingBox();
								var storyTextMesh = new THREE.Mesh(storyTextShape, storyTextMaterial);
			
			
								storyTextMesh.position.x = x;
								storyTextMesh.position.y = y;
								storyTextMesh.position.z = z;
								storyTextMesh.rotation.z = 0 * Math.PI;
								storyTextMesh.scale.x = storyTextMesh.scale.y = 0.2;
								yearGroup.add(storyTextMesh);
							}
						}
					}
			
				}
				scene.add(yearGroup);
		   },
		   error:function(result){
			   console.log(result);
		   }
	   });
}

function getNumberOfStoriesForYear(year){
	var count=0;
	for(var j=0;j<storiesList.length;j++){
		if((storiesList[j].year>year  && storiesList[j].year<(year+10)) || year==(storiesList[j].year)){
			count++;
		}
	}
	return count;
}

function getRandomPoints(min, max) {
	return Math.floor(Math.random() * (max - min + 1)) + min;
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
		if (camera.position.y > 60) {
			camera.position.y = 60;
		}

		if (camera.position.y < 35) {
			camera.position.y = 35;
		}

		camera.position.z = (camera.position.z + 12*speed);
	}
	renderer.render(scene, camera);
}

function onResize(event){
	camera.aspect=window.innerWidth/window.innerHeight;
	camera.updateProjectionMatrix();
	renderer.setSize(canvasDom.width(), canvasDom.height());

	centerNav();
}

function onMousemove(event){

	event.preventDefault();
	mouseX=(event.clientX-(window.innerWidth /2))*0.3;
	mouseY=(event.clientY-(window.innerHeight/2))*0.2;

	mouse.x=(event.clientX/window.innerWidth)*2-1;
	mouse.y=(event.clientY/window.innerHeight)*2+1;

}

