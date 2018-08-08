package fi.korri.epooq.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import fi.korri.epooq.model.Story;
import fi.korri.epooq.service.StoryService;

@Controller
@RequestMapping("/home")
public class HomeController {

	private final Logger log = Logger.getLogger(HomeController.class.getName());
	
	
	@Autowired
	private StoryService storyService;
	
	//Redirect to index.jsp
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView showCloudsVideo(HttpServletRequest request) {
		return new ModelAndView("index");
	}

	//Redirect to home.jsp
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showClouds(HttpServletRequest request) {
		log.debug("Epooq Home");
		
		
		String communityId = "-1";
		
		ModelAndView model = new ModelAndView("home");
		model.addObject("selectedCommunity", communityId);
		//model.addObject("communities", getCommunities());

		List<Story> storyList= storyService.getAllStories();
		model.addObject("storyList",storyList);
		return model;
	}
	
}
