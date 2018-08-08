package fi.korri.epooq.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/chooseCommunity")
public class ChooseCommunityController extends AbstractBaseController {
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView chooseCommunityHandler(
			@RequestParam("videoId") String videoId
			) {
		
		ModelAndView model = new ModelAndView();
		model.addObject("videoId", videoId);
		model.addObject("communities", getCommunities());

		return model;
	}
}
