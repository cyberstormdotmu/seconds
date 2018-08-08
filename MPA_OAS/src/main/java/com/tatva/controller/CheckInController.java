package com.tatva.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tatva.service.ICheckinService;

@Controller
@RequestMapping("/checkin")
public class CheckInController {

	@Autowired
	private ICheckinService checkinService;
	
	
	/**
	 * form for checkin process
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(value="/checkinForm.mpa",method=RequestMethod.GET)
	public String checkinForm()throws Exception {
		return "/page.checkin";
	}
	
	
	/**
	 * Verification for checked-in or not
	 * @param model
	 * @param passport
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/checkinForm.mpa",method=RequestMethod.POST)
	public String checkin(Model model,@RequestParam(value="passport")String passport)throws Exception {
		
		boolean flag=checkinService.checkin(passport);
		String msg;
		if(flag){
			msg="You Are Checked In";
			model.addAttribute("message", msg);
			return "/page.checkin";
		}
		else{
			msg="This Appointment Can't Check in";
			model.addAttribute("message", msg);
			return "/page.checkin";
		}
			
	}
}
