package fi.korri.epooq.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin/")
public class RootController {
	
	@RequestMapping(method = RequestMethod.GET)
	public String root() {
		return "forward:index";
	}
	
}
