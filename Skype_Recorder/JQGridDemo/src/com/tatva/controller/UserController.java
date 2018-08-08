package com.tatva.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.xml.ws.RequestWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tatva.json.JqGridData;
import com.tatva.model.Person;
import com.tatva.model.User;
import com.tatva.model.Vehicle;
import com.tatva.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/returnHome")
	public String returnHome()
	{
		return "home";
	}
	
	@RequestMapping(value="/index")
	public String index()
	{
		System.out.println("Index**********************");
		return "index";
	}
	
	@RequestMapping(value="/login")
	public String login()
	{
		System.out.println("Login map");
		return "login";
	}
	

	@RequestMapping(value="/authenticateUser")
	public String authenticateUser(@RequestParam(value="userName")String userName,
			@RequestParam(value="password")String password, HttpServletRequest request)
	{
		User user=null;
		System.out.println("User Name:"+userName);
		System.out.println("Password:"+password);
		user=userService.authenticateUser(userName, password);
		System.out.println("After Authenticate Method");
		if(user!= null)
		{
			System.out.println(user.getFirstName());
			System.out.println(user.getLastName());
			System.out.println(user.getPassword());
			System.out.println(user.getRoleId());
			System.out.println(user.getUserId());
			System.out.println(user.getUserName());
			System.out.println("*******************");
			
			if(user.getActive().equals("T"))
			{
				System.out.println("User che....");
				HttpSession session = request.getSession();
				session.setAttribute("user", user);
				return "home";
			}
			else
			{
				return "inActive";
			}
		}
		else
		{
			System.out.println("User Nathi");
			return "login";
		}
		
	}
	
	@RequestMapping(value="/listUser1")
	public String listUser1()
	{
		return "listUser";
	}
	
	@RequestMapping(value="/listUser",method=RequestMethod.POST,headers="Accept=*/*")         
    public @ResponseBody  JqGridData<User> getAll(HttpServletRequest request){
		 	System.out.println("Get All Called");
	        int rows = Integer.parseInt(request.getParameter("rows"));
	        int page = Integer.parseInt(request.getParameter("page"));
	        String sidx = request.getParameter("sidx");
	        String sord = request.getParameter("sord");
	        List<User> list = null;
	        list = userService.getUsers(rows,page,sidx,sord);
	        JqGridData<User> response = new JqGridData<>();
	        response.setRows(list);
	        int count = userService.getNoOfRecords();
	        int total = count%rows == 0 ? (int)Math.ceil(count/rows) : (int)Math.ceil(count/rows)+1;
	        response.setTotal(total);
	        response.setRecords(count);
	        response.setPage(page);
	        return response;
	    }
	
	
	
	@RequestMapping(value= "/edit")
	 public ModelAndView doEdit(HttpServletRequest request){
	  
	  Integer userId = 0;
	  String firstName = null;
	  String lastName = null;
	  String userName = null;
	  Integer roleId=0;
	  String oper = null;
	  Enumeration<String> paramNames = request.getParameterNames();
	  while (paramNames.hasMoreElements()) {
	   String s = paramNames.nextElement();
	   if("userId".equalsIgnoreCase(s)){
	    userId = Integer.parseInt(request.getParameter("userId"));
	    
	   }else if("firstName".equalsIgnoreCase(s)){
	    
	    firstName = request.getParameter("firstName");
	    
	   }else if("lastName".equalsIgnoreCase(s)){
	    
	    lastName = request.getParameter("lastName");
	    
	   }else if("userName".equalsIgnoreCase(s)){
	    
	    userName = request.getParameter("userName");
	    
	   }else if("roleId".equalsIgnoreCase(s)){
	    
		   roleId = Integer.parseInt(request.getParameter("roleId"));;
	    
	   }else if("oper".equalsIgnoreCase(s)){
	    oper = request.getParameter("oper");
	    
	   }
	   
	  }
	  
	  Map<String , String> model =new HashMap<String , String>();
	  
	  if("edit".equalsIgnoreCase(oper)){
	   User user = new User();
	   user.setUserId(userId);
	   user.setFirstName(firstName);
	   user.setLastName(lastName);
	   user.setUserName(userName);
	   user.setRoleId(roleId);
	   userService.updateUser(user);
	  // usermasterdao1.updateUser1(usermaster);
	   
	  }
	  else if("add".equalsIgnoreCase(oper))
	  {
		  User user=new User();
		  user.setFirstName(firstName);
		  user.setLastName(lastName);
		  user.setUserName(userName);
		  user.setRoleId(roleId);
		  userService.addUser(user);
	  }
	  else if("del".equalsIgnoreCase(oper))
	  {
		  userService.deleteUser(userId);
	  }
	  
	  return new ModelAndView("listUser" , model);
	  
	 }

	
	
	
	
	/*@RequestMapping(value="/listUser")
	public String listUser(Model model)
	{
		List users = userService.listUser();
        model.addAttribute("users", users);
        return "listUser";
	}

	@RequestMapping(value="/editUser/{userId}")
	public String editUser(@PathVariable("userId")int userId,Model model)
	{
		User user=userService.editUser(userId);
		model.addAttribute("user", user);
		System.out.println("**********till Controller***************");
		return "editUser";
	}
	
	@RequestMapping(value="/updateUser", method = RequestMethod.POST)
	public String updateUser(@ModelAttribute("user")User user)
	{
		userService.updateUser(user);
		return "redirect:/listUser";
		
	}

	@RequestMapping(value="/deleteUser/{userId}")
	public String deleteUser(@PathVariable("userId")int userId)
	{
		userService.deleteUser(userId);
		System.out.println("deleted");
		return "redirect:/listUser";
	}
*/	
	@RequestMapping(value="/signUp")
	public String signUp(Model model)
	{
		model.addAttribute("user", new User());
		return "signUp";
	}

	
	@RequestMapping(value="/registerUser",method=RequestMethod.POST)
	public String registerUser(@ModelAttribute("user") @Valid User user,BindingResult result)
	{
		if (result.hasErrors()) {
			return "signUp";
		} else {
			userService.addUser(user);
			return "redirect:/index";
		}
	}
	
	@RequestMapping(value="/viewProfile")
	public String userProfile(Model model,HttpServletRequest request)
	{
		System.out.println("Sushant Banugariya");	
		User user=(User)(request.getSession().getAttribute("user"));
		model.addAttribute("user", user);
		
		return "editUser";
	}

	@RequestMapping(value="/logout")
	public String logout(HttpServletRequest request)
	{
		request.getSession().invalidate();
		return "index";
	}
	
	@RequestMapping(value="/listUserTemp")
	public String list(Model model)
	{
		List users = userService.listUser();
        model.addAttribute("users", users);
        return "listUserTemp";
	}
	
	@RequestMapping(value="list/{userId}")
	public @ResponseBody List<Vehicle> list2(@PathVariable("userId")int userId)
	{
		System.out.println("Sushant Banugariya");	
		List<Vehicle> listVehicle=userService.listVehicle(userId);
		return listVehicle;
	}
	
	 @RequestMapping(value="listget", method = RequestMethod.POST)
	  public @ResponseBody Person get(@RequestParam("fname") String fname) {
		 
	      System.out.println("Sushant"+fname);
	      Person person = new Person();
	      System.out.println(person.getFname() + " " + person.getLname());
	      return person;
	  }
	 
	
}