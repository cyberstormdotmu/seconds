import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpHost;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnRoutePNames;



public class NVPApiCall {


    static String url =
        "https://api-3t.sandbox.paypal.com/nvp";

   public static void main(String[] args) {
	   
	   HttpHost proxy = new HttpHost("192.168.0.251", 8080);
	   
       //Instantiate an HttpClient
       HttpClient client = new HttpClient();
       
       //Instantiate a GET HTTP method
       PostMethod method = new PostMethod(url);
/*       method .getParams().setParameter("http.socket.timeout", 1000000000);*/
       method.setRequestHeader("Content-type",
               "text/xml; charset=ISO-8859-1");

       //Define name-value pairs to set into the QueryString
       NameValuePair nvp1= new NameValuePair("USER","harnishpatel25-facilitator_api1.gmail.com");
       NameValuePair nvp2= new NameValuePair("PWD","1388658244");
       NameValuePair nvp3= new NameValuePair("SIGNATURE","AQU0e5vuZCvSg-XJploSa.sGUDlpAoXjibytRxyOuK7SZ1KuqJSoA.XM");
       NameValuePair nvp4= new NameValuePair("VERSION","2.3");
       NameValuePair nvp5= new NameValuePair("AMT","10");
       NameValuePair nvp6= new NameValuePair("PAYMENTACTION","Authorization");
       NameValuePair nvp9= new NameValuePair("METHOD","SetExpressCheckout");

       method.setQueryString(new NameValuePair[]{nvp1,nvp2,nvp3,nvp4,nvp5,nvp6,nvp9});
       HttpParams params= client.getHttpConnectionManager().getParams();
       
       
       
/*       client.getHttpConnectionManager().getParams().setConnectionTimeout(1000000000);
       client.getHttpConnectionManager().getParams().setSoTimeout(1000000000);
*/       

       try{
    	   client.getHostConfiguration().setProxy("192.168.0.251", 8080);
    	   client.getParams().setParameter("http.socket.timeout", 10000);
    	   client.getParams().setParameter("http.connection-manager.timeout", 10000);
    	   client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
           int statusCode = client.executeMethod(method);

           System.out.println("Status Code = "+statusCode);
           System.out.println("QueryString>>> "+method.getQueryString());
           System.out.println("Status Text>>>"
                 +HttpStatus.getStatusText(statusCode));

           //Get data as a String
           System.out.println(method.getResponseBodyAsString());

/*           //OR as a byte array
           byte [] res  = method.getResponseBody();

           //write to file
           FileOutputStream fos= new FileOutputStream("donepage.html");
           fos.write(res);
*/
           //release connection
           method.releaseConnection();
       }
       catch(IOException e) {
           e.printStackTrace();
       }
   }
   
}

