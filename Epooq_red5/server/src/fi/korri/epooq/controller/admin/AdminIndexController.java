package fi.korri.epooq.controller.admin;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import fi.korri.epooq.controller.AbstractBaseController;

@Controller
@RequestMapping(value = "/admin/index")
public class AdminIndexController extends AbstractBaseController {

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getAdminCommunityPage() {
		
		Map<String, String> set = getSettings();
		
		ModelAndView model = new ModelAndView();
		model.addObject("communities", getCommunities());
		model.addObject("settings", set);
		model.addObject("keys", getAllKeys());

		return model;
	}
}
