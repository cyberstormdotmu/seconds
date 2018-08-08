package com.tatva.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tatva.domain.UserMaster;
import com.tatva.service.IUser;
import com.tatva.utils.DateUtil;
import com.tatva.utils.MPAContext;
import com.tatva.validator.UserValidator;

@Controller
@RequestMapping("/user")
public class UserController 
{
	@Autowired
	private IUser user;
	
	@Autowired
	UserValidator userValidator;
	
	@SuppressWarnings("unused")
	@InitBinder  
    private void initBinder(WebDataBinder binder) {  
		binder.setValidator(userValidator);
    }

	List<UserMaster> userList = null;
	UserMaster usermaster = new UserMaster();
	public  String userIdRel = null;
	public  String firstNameRel = null;
	public  String middleNameRel = null;
	public  String lastNameRel = null;
	public  String joiningDateRel = null;
	public  String roleRel = null;
	public  String counterRel = null;
	
	
	
	/**
	 * Implemtneting the search functionality for Users
	 * @param userMaster
	 * @param userIdRel
	 * @param firstNameRel
	 * @param middleNameRel
	 * @param lastNameRel
	 * @param joiningDateRel
	 * @param roleRel
	 * @param counterRel
	 * @param session
	 * @return Grid View Page for List Users
	 */
	@RequestMapping(value = "/searchUser.mpa", method = RequestMethod.POST)
	 public ModelAndView searchUser(@ModelAttribute("searchUser") UserMaster userMaster,
	         @RequestParam("userIdRel") String userIdRel,
	         @RequestParam("firstNameRel") String firstNameRel,
	         @RequestParam("middleNameRel") String middleNameRel,
	         @RequestParam("lastNameRel") String lastNameRel,
	         @RequestParam("joiningDateRel") String joiningDateRel,
	         @RequestParam("roleRel") String roleRel,
	         @RequestParam("counterRel") String counterRel,
	           HttpSession session)
	 {

		String ddy = userMaster.getDateOfJoiningString();
		Date tempDate = DateUtil.convertDateFromStringtoDate(ddy);
		userMaster.setDateOfJoining(tempDate);
		
		this.usermaster = userMaster;
		userList  = null;
		this.userIdRel = userIdRel;
		this.firstNameRel = firstNameRel;
		this.middleNameRel = middleNameRel;
		this.lastNameRel = lastNameRel;
		this.joiningDateRel = joiningDateRel;
		this.roleRel = roleRel;
		this.counterRel = counterRel;
		
	// Date joiningDateRel = DateUtil.convertDateFromStringtoDate(joiningDateRel1);
	  userList =  user.searchUser(userMaster,
					 				userIdRel, 
					 				firstNameRel, 
					 				middleNameRel,
					 				lastNameRel,
					 				joiningDateRel,
					 				roleRel,
					 				counterRel
					 				);
	  session.setAttribute("searchUserSize", userList.size());	  
	  return new ModelAndView("redirect:listUsers.mpa");
	 }
	
	
	@RequestMapping(value="/availableUser.mpa",method = RequestMethod.POST,headers = "Accept=*/*" , produces = "application/json")
	public @ResponseBody List<String> availableUser(HttpServletRequest request)throws Exception{
		
		
		String enteredUserId = request.getParameter("userId");
		String flagresult = null;
		flagresult = user.availableUser(enteredUserId);
		List<String> list = new ArrayList<>();
		list.add(flagresult);
	//	JSONObject json = new JSONObject();

	//	json.put("flagresult", flagresult);
			
		
		//	return json.toString();
		
		return list;
		
	}
	
	
	//==============
	
	@RequestMapping(value="/flagUpdateUser.mpa",method = RequestMethod.POST,headers = "Accept=*/*" , produces = "application/json")
	public @ResponseBody List<String> flagUpdateUser(HttpServletRequest request)throws Exception{
		
		
		String flagUserId = request.getParameter("flagUserId");
		String flagUserRole = request.getParameter("flagUserRole");
		
		MPAContext.flagUpdateUser = flagUserId;
		MPAContext.flagUpdateRole = flagUserRole;
		String flagresult = "setFlag";
		List<String> list = new ArrayList<>();
		list.add(flagresult);
		
		return list;
		
	}
	
	//===================
	
	
	@RequestMapping(value="/flagUpdateProfile.mpa",method = RequestMethod.GET)
	public String flagUpdateProfile(HttpServletRequest request)throws Exception{
		
		
		MPAContext.flagUpdateUser = MPAContext.currentUser;
		MPAContext.flagUpdateRole=MPAContext.currentRole;
		
		
		return "redirect:updateUserForm.mpa";
		
	}


	//=============

	/**
	 * Method for to Navigate Update User Form
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateUserForm.mpa")
	public ModelAndView updateUserForm(HttpServletRequest request) throws Exception {
		
		String userId = MPAContext.flagUpdateUser;
		String userRole = MPAContext.flagUpdateRole;
		String tempParam=request.getParameter("tempParam");
		if(!StringUtils.isEmpty(userId)){
			UserMaster userMaster=user.getById(userId,userRole);
			if(userMaster!=null){
				if(!StringUtils.isEmpty(tempParam)){
					request.setAttribute("tempParam","tempParam");
				}
				userMaster.setDateOfJoiningString(DateUtil.convertDateFromDatetoString(userMaster.getDateOfJoining()));
				ModelAndView mav = new ModelAndView("/page.updateUser","userMaster", userMaster);
				return mav;
			}else{
				request.setAttribute("temp", "temp");
				ModelAndView modelAndView=listUsers(request);
				modelAndView.addObject("messages", " Such User Does Not Exists");
				modelAndView.setViewName("/page.listUsers");
				return modelAndView;
			}
		}else{
			ModelAndView modelAndView=listUsers(request);
			modelAndView.setViewName("/page.listUsers");
			return modelAndView;
		}
	}
	
	
	/**
	 * Method for updating User
	 * @param userMaster
	 * @param result
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateUser.mpa")
	public ModelAndView updateUser(@ModelAttribute("userMaster") @Valid UserMaster userMaster, BindingResult result,HttpServletRequest request) throws Exception {
		if (result.hasErrors()) {
			return new ModelAndView("/page.updateUser","userMaster",userMaster);
		}

		userMaster.setDateOfJoining(DateUtil.convertDateFromStringtoDate(userMaster.getDateOfJoiningString()));
		UserMaster userMasters=user.getById(userMaster.getUserId());
		userMaster.setPassword(userMasters.getPassword());
		user.updateUser(userMaster);
		
		ModelAndView modelAndView=listUsers(request);
		modelAndView.addObject("messages", "User updated successfully");
		modelAndView.setViewName("/page.listUsers");
		request.setAttribute("temp", "temp");
		return modelAndView;
	}
	
	/**
	 * Method for navigating to the Create user Page
	 * @return Create user View Page
	 * @throws Exception
	 */
	@RequestMapping(value = "/createUserForm.mpa")
	public ModelAndView insertUserForm() throws Exception {
		ModelAndView mav = new ModelAndView("/page.insertUser","userMaster", new UserMaster());
		return mav;
	}
	
	/**
	 * Method for creating user
	 * @param userMaster
	 * @param result
	 * @param redirectAttributes
	 * @return message according to added user & navigating to list page
	 * @throws Exception
	 */
	@RequestMapping(value = "/createUser.mpa")
	public ModelAndView insertUser(@ModelAttribute("userMaster") @Valid UserMaster userMaster, BindingResult result,RedirectAttributes redirectAttributes) throws Exception {
		if (result.hasErrors()) {
			return new ModelAndView("/page.insertUser","userMaster",userMaster);
		}
		ModelAndView modelAndView=new ModelAndView("redirect:listUsers.mpa");
		user.insertUser(userMaster);
		redirectAttributes.addFlashAttribute("messages", "User has been added successfully");
		return modelAndView;
	}
	
	/**
	 * Method for deleting the user
	 * @param userId
	 * @param request
	 * @return List User View Page
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteUser.mpa")
	public ModelAndView deleteUser(HttpServletRequest request)throws Exception {
		
		String userId=request.getParameter("userId");
		user.deleteUser(userId);
		//List<UserMaster> userMasterList = user.selectAllUsers();
		request.setAttribute("temp", "temp");
		ModelAndView modelAndView=listUsers(request);
		//modelAndView.getModel().put("userMasterList",userMasterList);
		modelAndView.addObject("messages", "User deleted successfully");
		modelAndView.setViewName("/page.listUsers");
		return modelAndView;
	}

	/**
	 * 
	 * @param request
	 * @return List User
	 * @throws Exception
	 */
	@RequestMapping(value = "/listUsers.mpa")
	public ModelAndView listUsers(HttpServletRequest request){
		HttpSession session = request.getSession();
		
		String temp=request.getParameter("temp");
		
		if(temp!=null) {
		
			session.setAttribute("page", 1);
			session.setAttribute("userRows", 10);
			session.setAttribute("searchUserSize", null);
			userList = null;
		}
		
		
		  int noOfRecords=0;
		  if(session.getAttribute("searchUserSize")!=null){
		   session.setAttribute("page", 1);
		   noOfRecords=userList.size();
		  } else{
		   noOfRecords = user.getTotalUsers();
		  }
		
		int page;
		
		if(StringUtils.isNotEmpty(request.getParameter("page")))
		{
			page = Integer.parseInt(request.getParameter("page"));
		}
		else if(session.getAttribute("page") != null)
		{
			page = (int)session.getAttribute("page");
		}
		else
		{
			page = 1;
		}
		
		int recordsPerPage = 10;
		String pageStr = request.getParameter("page");
		String recordsPerPageStr = request.getParameter("recordsPerPage");

		session.setAttribute("page", page);
		if(StringUtils.isNotEmpty(recordsPerPageStr))
		{
			recordsPerPage = Integer.parseInt(recordsPerPageStr);
			session.setAttribute("userRows", recordsPerPage);
		}
		
		if(session.getAttribute("userRows") != null)
		{
			recordsPerPage = (int)session.getAttribute("userRows");
		}
		if(StringUtils.isNotEmpty(pageStr))
		{
			page = Integer.parseInt(pageStr);
		}
		
		
		List<UserMaster> userMasterList = new ArrayList<UserMaster>();
		
		if((userList == null || userList.isEmpty() ) && session.getAttribute("searchUserSize")==null)
		{
			userMasterList = user.listUsers((page - 1) * recordsPerPage, recordsPerPage);
		}
		else
		{
			if(userList.size() > 0 ){
			userMasterList = user.getListPage(userList, (page - 1) * recordsPerPage, recordsPerPage);
			}
			
			
			//userMasterList = userList;
		}
		
		for (UserMaster userMaster : userMasterList) {
			userMaster.setDateOfJoiningString(DateUtil.convertDateFromDatetoString(userMaster.getDateOfJoining()));
		}
		
		
		int noOfPages = (int)Math.ceil(noOfRecords * 1.0 / recordsPerPage);
		int start = (page - 1) * recordsPerPage;
		session.setAttribute("userMasterList", userMasterList);
		request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("start", start);
        ModelAndView mav=new ModelAndView("/page.listUsers");
        mav.addObject("userMasterList", userMasterList);
        mav.addObject("searchUser",new UserMaster());
        return mav;

	}
	
	
	/**
	 * Method For Ordering User
	 * @param request
	 * @return Ordered List Grid
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/orderUserList.mpa")
	public ModelAndView orderUserList(HttpServletRequest request)throws Exception 
	{
		
		Map<String, String[]> requestParamMap = request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = requestParamMap.entrySet();
		
		Iterator<Entry<String, String[]>> it = entrySet.iterator();
		
		String property = null;
		String orderValue = null;
		if(it.hasNext())
		{
			Map.Entry<String, String[]> entry = it.next();
			property = entry.getKey();
			orderValue = entry.getValue()[0];
		}
		
		HttpSession session = request.getSession();
		int page = (int)session.getAttribute("page");
		int recordsPerPage = 10;
		if(session.getAttribute("userRows") != null)
		{
			recordsPerPage = (int)session.getAttribute("userRows");
		}
		if(StringUtils.isEmpty(orderValue) || StringUtils.equals(orderValue, null) || StringUtils.equals(orderValue, "DSC"))
		{
			orderValue = "ASC";
		}
		
		else if(StringUtils.equals(orderValue, "ASC"))
		{
			orderValue = "DSC";
		}
		
		List<UserMaster> allUserList = null;
		if(session.getAttribute("searchUserSize")!=null){
			
			allUserList = user.listSearchOrder(0,userList.size(),property,orderValue,usermaster,userIdRel,firstNameRel,middleNameRel,lastNameRel,joiningDateRel,roleRel,counterRel);
		}
		else{
		 allUserList = user.listOrderdUser(0, user.getTotalUsers(), property, orderValue);
		}
		this.userList = allUserList;
		request.setAttribute(property+"Order", orderValue);		
		return listUsers(request);
		 
	}
}