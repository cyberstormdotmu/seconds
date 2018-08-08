package com.tatva.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tatva.domain.GlobalAttribute;
import com.tatva.service.IGlobalAttributeService;
import com.tatva.utils.DateUtil;
import com.tatva.validator.BookingSlotValidator;

@Controller
@RequestMapping("/bookingSlot")
public class BookingSlotController {

	@Autowired
	private IGlobalAttributeService globalAttributeService;
	
	@RequestMapping (value="/addBookingSlotForm.mpa")
	public ModelAndView addBookingSlotForm(@ModelAttribute GlobalAttribute glblAttribute,BindingResult result)throws Exception  {
		List<GlobalAttribute> list =globalAttributeService.list();
		GlobalAttribute globalAttribute=new GlobalAttribute();
		for (GlobalAttribute gAttribute : list) {
			if("PARALLEL_APT".equalsIgnoreCase(gAttribute.getAttributeName())){
				gAttribute.setTempValue(gAttribute.getNewValue());
				gAttribute.setSlots(gAttribute.getSlots());
				gAttribute.setApplyDateString(DateUtil.convertDateToString(gAttribute.getApplyDate()));
				globalAttribute=gAttribute;
			}
		}
		ModelAndView modelAndView=new ModelAndView("/page.bookingSlot","globalAttribute",globalAttribute);
		return modelAndView;
	}
	
	/**
	 * Method for calculating the time slot according to parallel appointments & number of counter
	 * @param globalAttribute
	 * @param model
	 * @param result
	 * @param redirectAttributes
	 * @return Booking Slot View Page 
	 * @throws Exception
	 */
	@RequestMapping (value="/addBookingSlot.mpa")
	public ModelAndView addBookingSlot(@ModelAttribute GlobalAttribute globalAttribute,Model model,BindingResult result,RedirectAttributes redirectAttributes) throws Exception {
		ModelAndView modelAndView=new ModelAndView("redirect:addBookingSlotForm.mpa");
		globalAttribute.setApplyDate(DateUtil.convertDateFromStringtoDate(globalAttribute.getApplyDateString()));
		new BookingSlotValidator().validate(globalAttribute,result);
		if (result.hasFieldErrors() || result.hasErrors()) {
			List<GlobalAttribute> list =globalAttributeService.list();
			for (GlobalAttribute gAttribute : list) {				
				if("PARALLEL_APT".equalsIgnoreCase(gAttribute.getAttributeName())){
					gAttribute.setTempValue(gAttribute.getNewValue());
					gAttribute.setSlots(gAttribute.getSlots());
					gAttribute.setApplyDateString(DateUtil.convertDateToString(gAttribute.getApplyDate()));
					globalAttribute.setNewValue(gAttribute.getNewValue());
				}
			}
            return new ModelAndView("/page.bookingSlot","globalAttribute",globalAttribute);
		}
		globalAttribute.setOldValue(globalAttribute.getTempValue());
		globalAttributeService.insertGlobalAttribute(globalAttribute);	
		redirectAttributes.addFlashAttribute("messages", "Booking Slot has been updated successfully");
		return modelAndView;
	}
}
