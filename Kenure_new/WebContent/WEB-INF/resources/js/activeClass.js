window.onload = setActive;

function setActive() {
	//alert("Hello");	
  aObj = document.getElementById('leftbar').getElementsByTagName('a');

  //alert("Hello11");

  for(i=0;i<aObj.length;i++) { 
	
    if(document.location.href.indexOf(aObj[i].href)>=0) {
    
    	//alert(aObj[i].href);
    	
      aObj[i].className='active';
    }
  }
}