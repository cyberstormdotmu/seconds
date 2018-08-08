package fi.korri.epooq.controller;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/recordVideo")
public class RecordVideoController {
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView recordVideoHandler() {

		ModelAndView model = new ModelAndView("recordVideo");
		model.addObject("videoId", UUID.randomUUID().toString());
		
		return model;
	}
}
