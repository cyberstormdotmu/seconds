package hello;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digiwise.reader.JavaListener;

@RestController
public class GreetingController {

    //private static final String template = "Hello, %s!";
    //private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value="/getSocialData", method=RequestMethod.POST)
    public DijiWiseResultsArray getTwitterData(@RequestParam(value="parent_id") String parent_id,
    		@RequestParam(value="child_user_id") String child_user_id,
    		@RequestParam(value="access_token") String access_token,
    		@RequestParam(value="oauth_token_secret") String oauth_token_secret,
    		@RequestParam(value="social_media_type") String social_media_type,
    		@RequestParam(value="social_site_user_id") String social_site_user_id) {
    	
    	DijiWiseResultsArray array=new DijiWiseResultsArray();
		try {
			 array = JavaListener.getResult( parent_id, child_user_id,access_token,oauth_token_secret,social_media_type,social_site_user_id, array);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("End of the webservice call: " + new Date());

		return array;
    }
    
    @RequestMapping(value="/getMessage", method=RequestMethod.GET)
    public void getMesssage() {
    	System.out.println("getMessage GET METHOD");
    }
    
    @RequestMapping(value="/getMessage", method=RequestMethod.POST)
    public void getPostMesssage() {
    	System.out.println("getPostMesssage POST METHOD");
    }
    
    @RequestMapping(value = "/storeObj.rest", method = RequestMethod.POST)
    public String storeObj(@RequestParam  String firstName) {
     
     /*User myUser = user ;*/
     System.out.println(firstName);
     /*System.out.println(user.getUserName());*/
   //  myUser.setFirstName("test from java");
     return firstName;
    }
    
}
