<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:genericpage title="">
	<jsp:attribute name="header">
    	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/three.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/three.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/script.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.simplemodal.1.4.4.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/minified/jquery-ui.min.js"></script>
		<script src="http://maps.googleapis.com/maps/api/js?key=AIzaSyDY0kkJiTPVd2U7aTOAwhc9ySH6oHxOIYM&sensor=false"></script>
		<style>
				body {
					margin: 0px;
					padding: 0px;
				}
				
				.dimmer {
					background: #000;
					position: absolute;
					opacity: .5;
					top: 0;
					z-index: 25000;
				}
				
				#loading {
					background: #000;
					position: absolute;
					opacity: .5; 
				    top: 0; 
				    z-index:25000;
				}
				
				#viewstoryoverlayInner {
					position: absolute;
					/*width:auto;
					height:auto;*/
					z-index: 26000;
				}
		</style>
			<spring:message code="label.communityselected" var="community_placeholder" />
		</jsp:attribute>
		<jsp:body>
			<!-- iterate timeline story list from controller -->
			<c:set var="i" value="0"></c:set>
			<c:forEach var="element" items="${model}"> 
				
				<input type="hidden" id="storyId${i}" value="${element.id}" />
				<input type="hidden" id="title${i}" value="${element.title }" />
				<input type="hidden" id="lattitude${i}" value="${element.lattitude }" />	
				<input type="hidden" id="longitude${i}" value="${element.longitude }" />
			<c:set var="i" value="${i + 1}" />
			</c:forEach>
		
			<!-- map to be loaded here -->
			<div id="map_canvas" style="height: 90%; margin-top: 75px;"></div>
			
			 <div id="nav" style="float:right;position:absolute;top:60%; ">
 				
 				<!-- back to story timeline  -->
 				<a id="connection-button" href="home.html">
 				<img src="resources/img/sidos_nappi.png"></a>
 				</div >
 							
		<span id="viewstoryoverlayOuter"> 
			<span id="viewstoryoverlayInner" style="display: none;">
			<!-- external content to be loaded here --> 
			</span>
		</span>
		
	<!-- 	
			<div id="loading">
		<img alt="Loading..." src="resources/img/loading1.gif" style="position: fixed; top: 50%; left: 50%;">
	</div> -->
	
		<script type="text/javascript">
			//close story page
			$(document).on("click", "a.close-gr", function()
			{
				closePopup();					
			});
			//remove dimmer
			function closePopup()
			{
				$(".dimmer").remove();
				$("#viewstoryoverlayInner").html("");
				$("#viewstoryoverlayInner").hide();
			} 
		</script> 
		
		<script type="text/javascript">
			//get story by id	
			 function getStory(id){
				 	var storyId=id;
					viewStory(storyId);
			}

			function viewStory(storyId) {
		
				$("#viewstoryoverlayInner").load("story/get.html?"+"storyId="+storyId);
				$('#viewstoryoverlayInner').css({
					  margin: 'auto',
				  	  position: 'absolute',
				  	  height: '78%',
				  	  top: 0, left: 0, bottom: 0, right: 0
					});
				window.setTimeout(alignPopupCenter, 800);
				var dimmer = document.createElement("div");
				dimmer.style.width =  window.innerWidth + 'px';
				dimmer.style.height = window.innerHeight + 'px';
				dimmer.className = 'dimmer';
				document.body.appendChild(dimmer);
			}
			
			function alignPopupCenter()
			{
				 $("#viewstoryoverlayInner").fadeIn(500);
				 $("#viewstoryoverlayInner").show();
			}

			window.onresize = 
				function()
				{
					var dimmer = document.getElementsByClassName("dimmer")[0];
					dimmer.style.width =  window.innerWidth + 'px';
					dimmer.style.height = window.innerHeight + 'px';

					alignPopupCenter();
				};
		
			$(document).ready(function() {
				
				initialize(); 
				
			});

			/* $(document)
			.ajaxStart(function (){
				// show loading div 
				$("#loading").show();
			})
			.ajaxStop(function (){
				// hide loading div
				$("#loading").hide();
			}); */
			
				var zoomLevel;
				function initialize() {
				
					// load three chest from story timeline to map
					var latArray=[];
					var lngArray=[];
					var storyTitle=[];
					var chestMsg=[];
					var storyId=[];
					var chestMarker;
					
					 	
					 for(var j=0;j<3;j++){
						 if(!(document.getElementById("lattitude"+j).value == "" || document.getElementById("lattitude"+j).value == null)){
						 var id=document.getElementById("storyId"+j).value;
						 var title=document.getElementById("title"+j).value;
						 latArray.push(document.getElementById("lattitude"+j).value);
						 lngArray.push(document.getElementById("longitude"+j).value);
						 chestMsg.push('<div style="border-bottom:2px solid gray;line-height:1.35;overflow:hidden;white-space:nowrap;font-family:Signika Negative;"><a  onclick="javascript:getStory('+id+')">'+title+'</a>');
						 storyTitle.push(document.getElementById("title"+j).value);
						 storyId.push(document.getElementById("storyId"+j).value);
						 
						 }
					}
					
					var lat1=latArray[0];
					var lat2=latArray[1];
					var lat3=latArray[2];
					 
					//center location of map
				 	var avgLatTemp = 0;
					var avgLngTemp = 0;
					 for(var i=0; i< latArray.length; i++)
		             {
						
						var avgLatTemp=avgLatTemp+parseFloat(latArray[i]);
						var avgLngTemp=avgLngTemp+parseFloat(lngArray[i]);
		             }
					
					var avgLat=(avgLatTemp/latArray.length);
					var avgLng=(avgLngTemp/latArray.length); 
					
					//calculate distance between chest by left,right,top,bottom point
					var smallestLat = latArray[0];
					var largetstLat = latArray[0];
		            
					var smallestLng = lngArray[0];
					var largetstLng = lngArray[0];
		               
		                for(var i=0; i< latArray.length; i++)
		                {		
		                        if(latArray[i] > largetstLat)
		                        	largetstLat = latArray[i];
		                        else if (latArray[i] < smallestLat)
		                        	smallestLat = latArray[i];
		                       
		                        
		                }
		              
			            for(var i=0; i<lngArray.length; i++)
			            {
			               	if(lngArray[i] > largetstLng)
			                   largetstLng = lngArray[i];
			                else if (lngArray[i] < smallestLng)
			                    smallestLng = lngArray[i];
			            }
			        
			            
			        var left=smallestLng;
			        var right=largetstLng;
			        var top=largetstLat;
			        var bottom=smallestLat;
			        
			    	//decide zoom level horizontal and vertical difference
			  		var Hdiff=right-left;
			  		var vDiff=top-bottom;
			  		
			  		
					var diff = 0;
			  		
			  		if(Hdiff < 2*vDiff)
			  		{
			  			diff = 2*vDiff;
			  		}
			  		else
			  		{
			  			diff = Hdiff;
			  		}
					
				  	if(diff <= 360 && diff >= 180){
				  		console.log("inside if zoom-3");
				  		zoomLevel=3;
				
				  	}else if(diff <= 180 && diff >= 90){
				  		console.log("inside else zoom-4");
				  		zoomLevel=3; 
				  	}else if(diff <= 90 && diff >= 45){
				  		console.log("inside else zoom-5");
				  		zoomLevel=4;
				  	}else{
				  		console.log("inside else zoom-6");
				  		zoomLevel=6;
				  	} 
				  
				  	var oldMarkers = [];
				  	//chest image 
				  	var image = {
				     	    url:'resources/img/small_chest.png',
				     	 	size: new google.maps.Size(50, 30),
				     };
				  	//marker image
				  	var markerImg={
				  			url:'resources/img/blue-marker.png'	
				  	};
				  	
				  	//google map center,default zoomlevel,maptype
				  	var chestAvailable=${chestAvailable}
				  	
				  	if(chestAvailable){
				  		
				  	var map_options = {
					      center: new google.maps.LatLng(avgLat,avgLng),
					      zoom: zoomLevel,
					      streetViewControl:true,
					      zoomControlOptions:{position:google.maps.ControlPosition.RIGHT},
					      panControlOptions:{position:google.maps.ControlPosition.RIGHT},
					      mapTypeId: google.maps.MapTypeId.ROADMAP
					};
				  	}else{
				  		
				  		var map_options = {
							      center: new google.maps.LatLng(0,0),
							      zoom: 3,
							      streetViewControl:true,
							      zoomControlOptions:{position:google.maps.ControlPosition.RIGHT},
							      panControlOptions:{position:google.maps.ControlPosition.RIGHT},
							      mapTypeId: google.maps.MapTypeId.ROADMAP
							};
				  	}
				  	//load map on map-canvas
				    var google_map = new google.maps.Map(document.getElementById("map_canvas"), map_options);
				    var info_window = new google.maps.InfoWindow({
				        content: 'loading',
				    });
				    
				    //place chest marker for timeline story location
			 	  	var u=0;
					for(item in storyTitle){
						 chestMarker = new google.maps.Marker({
							        map:       google_map,
							        animation: google.maps.Animation.DROP,
							        draggable:true,
							        icon:image,
							        title:     storyTitle[u],
							        position:  new google.maps.LatLng(latArray[u],lngArray[u]),
							      	html:      chestMsg[u],
							      	 maxWidth: 200,
							      	customInfo: storyId[u]							      
					    	});
						oldMarkers.push(chestMarker);
						
						//dispaly message box on mouseover of chest marker
						google.maps.event.addListener(chestMarker, 'mouseover', function() {
							info_window.setContent(this.html);
					        info_window.open(google_map, this);
					    });
						//open story page on click of chest marker
							google.maps.event.addListener(chestMarker, 'click', function() {
					   		getStory(this.customInfo);   
					    });
							u++;
					} 
					
				
					//get all stories
					var storiesList= new Array();
			    	$.ajax({
			    		  type: "POST",
			    	      url:"story/list.html",
			    	      success:function(result){
			    	      storiesList = jQuery.parseJSON(result);
				    var title = [];
				    var lat = [];
				    var lng = [];
			        var msg = [];
			        var storyIdMarker = [];
			        
			        if(chestAvailable){
			        	 
			        	  for(var j=0;j<storiesList.length;j++){	
						    	if(!(storiesList[j].lattitude == "" || storiesList[j].lattitude == null)){
						    		if(!(storiesList[j].lattitude == lat1 || storiesList[j].lattitude == lat2 || storiesList[j].lattitude == lat3)){
						        	title.push(storiesList[j].title);
									lat.push(storiesList[j].lattitude);
									lng.push(storiesList[j].longitude);
									storyIdMarker.push(storiesList[j].id);
									msg.push('<div style="border-bottom:2px solid gray;line-height:1.35;overflow:hidden;white-space:nowrap;font-family:Signika Negative;"><a onclick="javascript:getStory('+storiesList[j].id+');">'+(storiesList[j].title)+'</a></div>');	
						    		}
						    	}
						    }
			        }else{
			        	 
				    for(var j=0;j<storiesList.length;j++){	
				    	if(!(storiesList[j].lattitude == "" || storiesList[j].lattitude == null)){
				    		//if(!(storiesList[j].lattitude == lat1 || storiesList[j].lattitude == lat2 || storiesList[j].lattitude == lat3)){
				        	title.push(storiesList[j].title);
							lat.push(storiesList[j].lattitude);
							lng.push(storiesList[j].longitude);
							storyIdMarker.push(storiesList[j].id);
							msg.push('<div style="border-bottom:2px solid gray;line-height:1.35;overflow:hidden;white-space:nowrap;font-family:Signika Negative;"><a onclick="javascript:getStory('+storiesList[j].id+');">'+(storiesList[j].title)+'</a></div>');	
				    		//}
				    	}
				    }
			        }
				   
				    //place normal marker on all stories
				    var i = 0;
				    for (item in title ) {
				    	
				     	marker = new google.maps.Marker({
				        map:       google_map,
				        animation: google.maps.Animation.DROP,
				        draggable:true,
				        icon:markerImg,
				        title:     title[i],
				        position:  new google.maps.LatLng(lat[i],lng[i]),
				        html:      msg[i],
				        customInfo: storyIdMarker[i]
				     	});  	
				        oldMarkers.push(marker);
				        //display message box on mouseover of marker
				        google.maps.event.addListener(marker, 'mouseover', function() {
				            	
				               info_window.setContent(this.html);
				               info_window.open(google_map, this);
				        });
				        //open story page on click of marker
				        google.maps.event.addListener(marker, 'click', function() {
				          
				        	getStory(this.customInfo);   
				        });
				        i++;  
				     
				  }  
				  
				  function addOverlayImages(zoom)
				  {
					for(var i=0; i < overlayList.length; i++)
			  		{
			  	    		var overlay = overlayList[i];
			  	    		var image = imageList[i];
					        
				        	var adminZoomLevel=image.level;
			  	    		
			  	    		if( zoom  >=  adminZoomLevel){
					        	
					        	addOverlay(overlay);

			        		}else {
			        			removeOverlay(overlay);		
			        		}
				  	}
				 }
				 //add image to map 
			     function addOverlay(overlay) {
		  	    	overlay.setMap(google_map);
		  	    }
			     
			    //remove image from map 
		  	    function removeOverlay(overlay) {
		  	    	overlay.setMap(null);
		  	    }
		  	    
		  	  //get all admin image  
		  	  var overlayList = [];
				var imageList = new Array();	
			      $.ajax({
			  	      url:"admin/listImages.html",
			  	      success:function(result){
			  	        imageList = jQuery.parseJSON(result);
			  	        
			  	        //iterate each image object to overlayList
			  	     	for(var i=0; i <imageList.length; i++)
				        {
				        	var image = imageList[i];
				        	var adminZoomLevel=image.level;
				        	var startLat = image.startLat;
				        	var startLong = image.startLong;
				        	var endLat = image.endLat;
				        	var endLong = image.endLong;
				        	var file = image.file;
				     			
				        		//create image area
				        		bounds = new google.maps.LatLngBounds(new google.maps.LatLng(
										endLat, endLong), new google.maps.LatLng(
										startLat, startLong));
						        	
						        	overlay = new google.maps.GroundOverlay(
											"${pageContext.request.contextPath}/" + file, bounds);
						        	//add image object to overlayList
						        	overlayList.push(overlay);
				        }
						//load image whose zoom level is same or less than current zoom of map  		  	  
			  	     	addOverlayImages(zoomLevel);
						
						//zoom level change
			  	     	google.maps.event.addListener(google_map, 'zoom_changed', function() {
			  	    		addOverlayImages(google_map.getZoom());
			  	    	});
			  	   
			  	        
			  	    },
			        error:function(result){
			        	
			      	}
			      });
			
				 },
			       error:function(result){
			     }
			     }); 
			    	
			    
			    } 
</script> 
		
    
	

		

	</jsp:body>
</t:genericpage>


