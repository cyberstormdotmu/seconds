package fi.korri.epooq.controller.api;

import java.util.Calendar;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import fi.korri.epooq.Anchor;
import fi.korri.epooq.Community;
import fi.korri.epooq.Key;
import fi.korri.epooq.Story;
import fi.korri.epooq.controller.AbstractBaseController;
import fi.korri.epooq.controller.admin.AdminSaveSettingsController;

@Controller
@RequestMapping(value = "/api/getCommunity")
public class ApiGetCommunityController extends AbstractBaseController {

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getCommunity(WebRequest webRequest, HttpServletRequest request) {

		String cIdString = webRequest.getParameter("id");
		Long communityId = null;

		Community community = null;
		
		if (cIdString != null) {
			communityId = Long.parseLong(cIdString);
			community = getCommunityById(communityId);
			request.getSession().setAttribute(SESSION_COMMUNITY, communityId.toString());
		} else {
			request.getSession().removeAttribute(SESSION_COMMUNITY);
		}

		String search = webRequest.getParameter("search");
		
		StringBuilder json = new StringBuilder();

		json.append("{");

		long id = -1l;
		String name = "Julkinen";
		if(community != null) {
			id = community.getId();
			name = community.getName();
		}
		json.append("\"id\": \"").append(id).append("\",");
		json.append("\"name\": \"").append(name).append("\",");
		
		// Stories
		Set<Story> stories = getStoriesByCommunity(communityId, search);
		json.append("\"stories\": [");
		int storyCount = 0;
		if (stories != null) {
			for (Story story : stories) {
				if (storyCount > 0) {
					json.append(",");
				}
				json.append(generateStoryJSON(story));
				storyCount++;
			}
		}
		json.append("],");
		
		// Anchors
		Set<Anchor> anchors = getAnchorsByCommunity(communityId, search);
		json.append("\"anchors\": [");
		int anchorCount = 0;
		if (anchors != null) {
			for (Anchor anchor : anchors) {
				if (anchorCount > 0) {
					json.append(",");
				}
				json.append(generateAnchorJSON(anchor));
				anchorCount++;
			}
		}
		json.append("],");
		
		// Keys
		Set<Key> keys = getKeysByCommunity(communityId);
		json.append("\"keys\": [");
		int keyCount = 0;
		if (keys != null) {
			for (Key key : keys) {
				if (keyCount > 0) {
					json.append(",");
				}
				json.append(generateKeyJSON(key));
				keyCount++;
			}
		}
		json.append("]");
		
		json.append("}");

		ModelAndView model = new ModelAndView();
		model.addObject("body", json.toString());

		return model;
	}

	private String generateStoryJSON(Story story) {

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		sb.append("\"id\": ").append("\"").append(story.getId()).append("\",");
		sb.append("\"title\": ").append("\"").append(story.getTitle()).append("\",");
		sb.append("\"date\": ").append(story.getDateUnixTime()).append(",");
		sb.append("\"place\": ").append("\"").append(story.getPlace()).append("\"");

		sb.append("}");

		return sb.toString();
	}
	
	private String generateAnchorJSON(Anchor anchor) {
		StringBuilder sb = new StringBuilder();

		sb.append("{");

		sb.append("\"id\": ").append("\"").append(anchor.getId()).append("\",");
		sb.append("\"title\": ").append("\"").append(anchor.getTitle()).append("\",");
		sb.append("\"date\": ").append(anchor.getDateUnixTime()).append(",");
		sb.append("\"place\": ").append("\"").append(anchor.getPlace()).append("\",");
		
		if(anchor.getPictureId() == null || anchor.getPictureId() == 0) {
			sb.append("\"pictureId\": ").append("\"-1\",");
		} else {
			sb.append("\"pictureId\": ").append("\"").append(anchor.getPictureId()).append("\",");
		}
		
		sb.append("\"content\": ").append("\"").append(anchor.getContent()).append("\"");

		sb.append("}");

		return sb.toString();
	}
	
	private String generateKeyJSON(Key key) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("{");

		sb.append("\"id\": ").append("\"").append(key.getId()).append("\",");
		sb.append("\"question\": ").append("\"").append(key.getQuestion()).append("\",");

		// Get test user birthyear
		int year = 0;
		String byString = getSettings().get(AdminSaveSettingsController.BIRTH_YEAR);
		if(byString != null) {
			year = Integer.parseInt(byString);
		}
		// Combine birth year and key age
		Calendar cal = Calendar.getInstance();
		cal.set(year, 0, 1);
		cal.add(Calendar.YEAR, key.getAge());
		
		sb.append("\"date\": ").append(cal.getTime().getTime()).append(",");
		
		if(key.getPictureId() == null) {
			sb.append("\"pictureId\": ").append("\"-1\"");
		} else {
			sb.append("\"pictureId\": ").append("\"").append(key.getPictureId()).append("\"");
		}

		sb.append("}");
		
		return sb.toString();
	}
}
