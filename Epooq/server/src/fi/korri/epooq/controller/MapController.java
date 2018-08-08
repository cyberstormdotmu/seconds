package fi.korri.epooq.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import fi.korri.epooq.model.Story;
import fi.korri.epooq.model.StoryPage;
import fi.korri.epooq.service.StoryService;

@Controller
public class MapController {

	@Autowired
	private StoryService storyService;

	private final Logger log = Logger.getLogger(MapController.class.getName());

	@RequestMapping(value = "/mapView", method = RequestMethod.POST)
	public ModelAndView showMap(@RequestParam("storyId0") String storyId0,@RequestParam("storyId1") String storyId1,@RequestParam("storyId2") String storyId2 ) {
	
		Story story1 = null;
		Story story2 = null;
		Story story3 = null;
		boolean chestAvailable;
		if(Integer.parseInt(storyId0) != -1)
		{
			story1=storyService.getStoryById(Long.parseLong(storyId0));
			chestAvailable=true;
		}
		if(Integer.parseInt(storyId1) != -1)
		{
			story2=storyService.getStoryById(Long.parseLong(storyId1));
			chestAvailable=true;
		}
		if(Integer.parseInt(storyId2) != -1)
		{
			story3=storyService.getStoryById(Long.parseLong(storyId2));
			chestAvailable=true;
		}else{
			chestAvailable=false;
		}
	
		List<Story> listStories = new ArrayList<Story>();
		listStories.add(story1);
		listStories.add(story2);
		listStories.add(story3);
		log.debug("Map View with all stories and three timeline stories: "+storyId0+" "+storyId1+" "+storyId2);	

		ModelAndView model = new ModelAndView("mapView");
		
		model.addObject("model", listStories);
		model.addObject("chestAvailable",chestAvailable);
		return model;
		
	

}
}
