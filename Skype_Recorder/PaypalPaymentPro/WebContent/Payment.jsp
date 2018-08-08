<script src="js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script>
 
  
$(document).ready(function(){
$("#expressCheckoutForm").submit(function() {
	var dddd='';
	    $.ajax({
	        type: "POST",
	    	url : 'https://api-3t.sandbox.paypal.com/nvp',
			async: true,
			success: function(data){
				dddd=data;
				var answer=confirm("Are you sure want to delete this User?"+ data);				
			},
			error: function(data){
				var answer=confirm("errrorrrrrrrrr?"+data);
				alert(data);				
			}
		});
	    alert('dddd -'+dddd);
	});
});    
 
</script>
<form id="expressCheckoutForm" >
<input type="hidden" name="USER" value="harnishpatel25-facilitator_api1.gmail.com"/>
<input type="hidden" name="PWD" value="1388658244"/>
<input type="hidden" name="SIGNATURE" value="AQU0e5vuZCvSg-XJploSa.sGUDlpAoXjibytRxyOuK7SZ1KuqJSoA.XM"/>
<input type="hidden" name="VERSION" value="2.3"/>
<input type="hidden" name="AMT" value="10"/>
<input type="hidden" name="RETURNURL" value="http://www.google.com"/>
<input type="hidden" name="CANCELURL" value="http://www.tatvasoft.com"/>
<input type="hidden" name="PAYMENTACTION" value="Authorization"/>
<input type="submit" name="METHOD" value="SetExpressCheckout" >
</form>