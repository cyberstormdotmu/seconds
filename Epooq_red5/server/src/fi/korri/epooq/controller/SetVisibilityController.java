package fi.korri.epooq.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import fi.korri.epooq.Story;

@Controller
@RequestMapping(value = "/setVisibility")
public class SetVisibilityController extends AbstractBaseController {
	
	private static final Logger log = LoggerFactory.getLogger(SetVisibilityController.class);
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView setVisibilityHandler(WebRequest request) {
		
		String videoId = request.getParameter("videoId");
		String title = request.getParameter("title");
		String place = request.getParameter("place");
		String dateString = request.getParameter("date");
		
		if(title != null) {
		
			DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
			Calendar calendar = Calendar.getInstance();
			
			try {
				calendar.setTime(format.parse(dateString));
			} catch (ParseException e) {
				log.error("Error parsing date: {}", dateString);
			}
			
			writeStory(videoId, new Story(videoId, title, place, calendar.getTime()));
		}

		ModelAndView model = new ModelAndView();
		model.addObject("videoId", videoId);
		
		return model;
	}
}
