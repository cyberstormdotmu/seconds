package fi.korri.epooq.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import fi.korri.epooq.controller.AbstractBaseController;

@Controller
@RequestMapping(value = "/admin/settings")
public class AdminSaveSettingsController extends AbstractBaseController {

	public static final String BIRTH_YEAR = "birthYear";
	
	@RequestMapping(method = RequestMethod.POST)
	public String saveSettings(WebRequest request) {
		
		String birthYear = request.getParameter(BIRTH_YEAR);
		
		writeSettings(BIRTH_YEAR, birthYear);
		
		return "redirect:index.html";
	}
}
