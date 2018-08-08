package fi.korri.epooq.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger log = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private StoryService storyService;

	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showClouds(HttpServletRequest request) {
	
		String communityId = "-1";
		
		/*
		if(request.getSession().getAttribute(SESSION_COMMUNITY) != null) {
			communityId = request.getSession().getAttribute(SESSION_COMMUNITY).toString();
		}
		*/
		
		ModelAndView model = new ModelAndView("home");
		model.addObject("selectedCommunity", communityId);
		//model.addObject("communities", getCommunities());

		List<Story> storyList= storyService.getAllStories();
		model.addObject("storyList",storyList);
		return model;
	}
	
	/*@RequestMapping(method = RequestMethod.POST)
	public String saveAnchorHandler(@RequestParam("file") MultipartFile file, WebRequest request) {
		
        Long pictureId = writePicture(file);

        String idString = request.getParameter("id");
		String title = request.getParameter("title");
		String place = request.getParameter("place");
		String dateString = request.getParameter("date");
		String content = request.getParameter("content");
		String communityIdString = request.getParameter("communityId");
		
		Long id = null;
		if(idString != null && !idString.equals("-1")) {
			id = Long.parseLong(idString);
		}
		
		Long communityId = null;
		if(communityIdString != null && !communityIdString.equals("-1")) {
			communityId = Long.parseLong(communityIdString);
		}
		
		log.info("Id: " + id + ", communityId: " + communityId);
		
		if(title != null) {
		
			DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
			Calendar calendar = Calendar.getInstance();
			
			try {
				calendar.setTime(format.parse(dateString));
			} catch (ParseException e) {
				log.error("Error parsing date: {}", dateString);
			}
			
			Anchor anchor = new Anchor();
			
			if(id != null) {
				anchor.setId(id);
			}
			
			anchor.setTitle(title);
			anchor.setPlace(place);
			anchor.setDate(calendar.getTime());
			anchor.setContent(content);
			anchor.setCommunityId(communityId);
			anchor.setPictureId(pictureId);
			
			writeAnchor(anchor);
		}

		
		return "redirect:home.html";
	}*/
}
