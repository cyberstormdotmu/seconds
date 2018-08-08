          
            <html>
                <body>
                <script type="text/javascript">
                function SubmitOrderToPaypal()
                {
                    var xmlhttp;
                    var strUrl;

                    strCmd = "https://www.sandbox.paypal.com/webscr&cmd=_express-checkout&token=tokenValue&AMT=amount
&CURRENCYCODE=currencyID&RETURNURL=return_url&CANCELURL=cancel_url";

                    if (window.XMLHttpRequest)
                      {
                      // code for IE7+, Firefox, Chrome, Opera, Safari
                      xmlhttp=new XMLHttpRequest();
                      }
                    else if (window.ActiveXObject)
                      {
                      // code for IE6, IE5
                      xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
                      }
                    else
                      {
                      alert("Your browser does not support XMLHTTP!");
                      }
                    xmlhttp.onreadystatechange=function()
                    {
                    if(xmlhttp.readyState==4)
                      {
                      document.myForm.time.value=xmlhttp.responseText;
                      }
                    }
                    xmlhttp.open("POST",strURL,true);
                    xmlhttp.onreadystatechange=handleResponse;
                    xmlhttp.send(null);
                }

            function handleResponse()
            {
            if (xmlhttp.readyState==4)
              {
               // TODO: How to parse Paypal response? Is this how response from Paypal is communicated? 
document.getElementById("txtPaypalResponse").innerHTML=xmlhttp.responseText;
              }
            }
                </script>

                <form name="myForm">
<input type="image" name="submit" onclick="SubmitOrderToPaypal();" border="0"
src="https://www.paypal.com/en_US/i/btn/btn_buynow_**.gif"
alt="PayPal - The safer, easier way to pay online">
                </form>
                </body>
            </html>