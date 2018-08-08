package fi.korri.epooq.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger log = LoggerFactory
			.getLogger(MapController.class);

	@RequestMapping(value = "/mapView", method = RequestMethod.GET)
	public ModelAndView showMap(@RequestParam("storyId0") String storyId0,@RequestParam("storyId1") String storyId1,@RequestParam("storyId2") String storyId2 ) {
				
		Story story1=storyService.getStoryById(Long.parseLong(storyId0));
		Story story2=storyService.getStoryById(Long.parseLong(storyId1));
		Story story3=storyService.getStoryById(Long.parseLong(storyId2));
		List<Story> listStories = new ArrayList<Story>();
		listStories.add(story1);
		listStories.add(story2);
		listStories.add(story3);
		/*List<Story> listStories = storyService.getAllStories();
		List<Double> listLongitude = new ArrayList<Double>();
		List<Double> listLattitude = new ArrayList<Double>();
		List<StoryPage> storyPageList=new ArrayList<StoryPage>();
		
		List<StoryPage> page=new ArrayList<StoryPage>();
	
		for(int i=0;i<listStories.size();i++){
			
			storyPageList=listStories.get(i).getStoryPages();
			
			for(int j=0;j<storyPageList.size();j++){
				if(storyPageList.get(j).getStoryContent().getType().contains("image")){
				
					
					page.add(storyPageList.get(j));
				}
			}
		
		}
		for (int i = 0; i < listStories.size(); i++) {
			String temp = null;
				temp =listStories.get(i).getLattitude();
			if((temp != null) && (!(temp.equals("")))){
			
				listLattitude.add(Double.parseDouble(temp));
			}
		}
		for (int i = 0; i < listStories.size(); i++) {
			
			String temp = null;
			temp =  listStories.get(i).getLongitude();
			
			if((temp != null) && (!(temp.equals("")))){
				listLongitude.add(Double.parseDouble(temp));
			}
		
			}

		Collections.sort(listLongitude);

		Double largeLongitude = listLongitude.get(0);
		Double smallLongitude = listLongitude.get(listLongitude.size() - 1);
		Double avgLongitude = (largeLongitude + smallLongitude) / 2;
		
		Collections.sort(listLattitude);

		Double largeLattitude = listLattitude.get(0);
		Double smallLattitude = listLattitude.get(listLattitude.size() - 1);
		Double avgLattitude = (largeLattitude + smallLattitude) / 2;

		Double leftPoint = listLongitude.get(0);
		Double rightPoint = listLongitude.get(listLongitude.size() - 1);

		Double topPoint = listLattitude.get(listLattitude.size() - 1);
		Double bottomPoint = listLattitude.get(0);*/

		ModelAndView model = new ModelAndView("mapView");
		
		model.addObject("model", listStories);

		return model;
	}

}
