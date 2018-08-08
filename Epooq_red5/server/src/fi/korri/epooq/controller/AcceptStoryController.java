package fi.korri.epooq.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/acceptStory")
public class AcceptStoryController extends AbstractBaseController {
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView acceptStoryHandler(
			@RequestParam("videoId") String videoId) {
		ModelAndView model = new ModelAndView();
		model.addObject("videoId", videoId);

		return model;
	}
}
