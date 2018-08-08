package fi.korri.epooq.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import fi.korri.epooq.service.StoryService;

@Controller
@RequestMapping(value = "/acceptStory")
public class AcceptStoryController {
	private final Logger log = Logger.getLogger(AcceptStoryController.class.getName());
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView acceptStoryHandler(
			@RequestParam("videoId") String videoId) {
		log.debug("accept story");
		ModelAndView model = new ModelAndView();
		model.addObject("videoId", videoId);

		return model;
	}
}
