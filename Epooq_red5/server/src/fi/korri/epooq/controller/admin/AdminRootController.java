package fi.korri.epooq.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class AdminRootController {
	
	private static final Logger log = LoggerFactory.getLogger(AdminRootController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String root() {
		log.error("root()");
		return "forward:index";
	}
	
}
