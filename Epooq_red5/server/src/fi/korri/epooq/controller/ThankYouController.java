package fi.korri.epooq.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fi.korri.epooq.Story;

@Controller
@RequestMapping(value = "/thankYou")
public class ThankYouController extends AbstractBaseController {
	
	private static final Logger log = LoggerFactory.getLogger(ThankYouController.class);
	
	@RequestMapping(method = RequestMethod.POST)
	public void thankYouHandler(
			@RequestParam("videoId") String videoId,
			@RequestParam("community") String cidString
			) {
		
		Story metadata = getStoryById(videoId);
		if(metadata != null) {
			if(cidString != null && !"".equals(cidString)) {
				metadata.setCommunityId(Long.parseLong(cidString));
			}
			
			metadata.setReady(true);
			
			writeStory(videoId, metadata);
		} else {
			log.error("Cannot find video with id: " + videoId);
		}
	}
}
