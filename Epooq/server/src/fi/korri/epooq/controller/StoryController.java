package fi.korri.epooq.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import fi.korri.epooq.model.Story;
import fi.korri.epooq.model.StoryContent;
import fi.korri.epooq.model.StoryPage;
import fi.korri.epooq.model.User;
import fi.korri.epooq.service.StoryService;
import fi.korri.epooq.util.DateUtil;
import fi.korri.epooq.util.EpooqConstant;

@Controller
@SessionAttributes({"newStory","story"})
public class StoryController {

	private final Logger log = Logger.getLogger(StoryService.class.getName());
	@Autowired
	private StoryService storyService;

	@Autowired
	private ReloadableResourceBundleMessageSource message;
	// Method to View Story by id
	@RequestMapping(value = "/story/get", method = RequestMethod.GET)
	public ModelAndView getStory(@RequestParam("storyId") long storyId)
	{
		Story story = storyService.getStoryById(storyId);
		log.info("Story "+storyId+" "+"is loaded");
		ModelAndView mav = new ModelAndView("form/viewStoryForm");
		mav.addObject("story", story);

		return mav;
	}

	// Method to get Story by title
	@RequestMapping(value = "/story/getStoryByTitle", method = RequestMethod.POST)
	//@ResponseBody
	public @ResponseBody String getStoryByTitle(@RequestParam("storyTitle") String storyTitle)
	{
		String json="";
		List<Story> storyList = storyService.getStoryByTitle(storyTitle);
		if(storyList!=null && storyList.size()>0){
			ObjectMapper mapper = new ObjectMapper();
			try {
				json=mapper.writeValueAsString(storyList);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}

		return json;
	}

	// Open choose media
	@RequestMapping(value = "/story/add", method = RequestMethod.GET)
	public ModelAndView addStory() {

		Story newStory = new Story();
		log.debug("Choose media");
		ModelAndView mav = new ModelAndView("story");
		mav.addObject("newStory", newStory);

		return mav;
	}

	// List of stories
	@RequestMapping(value = "/story/list", method = RequestMethod.POST)
	public @ResponseBody String getAllStories() {
		String json="";
		List<Story> listStories=storyService.getAllStories();
		if(listStories!=null && listStories.size()>0){
			ObjectMapper mapper = new ObjectMapper();
			try {
				json=mapper.writeValueAsString(listStories);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return json;
	}
	// Method to Redirect to chooseMedia Form 
	@RequestMapping(value = "/story/form/chooseMediaForm", method = RequestMethod.GET)
	public ModelAndView chooseMediaForm(@ModelAttribute("newStory") Story newStory,
			@RequestParam(value = "removePageNo", defaultValue = "", required = false) String removePageNo)
	{
		ModelAndView mav = new ModelAndView("story");

		// remove pages no
		if(StringUtils.isNotEmpty(removePageNo))
		{
			String removepageArr[] = removePageNo.split(",");


			List<StoryPage> storyPages = newStory.getStoryPages();
			for(int i = removepageArr.length; i>0; i--)
			{
				storyPages.remove(Integer.parseInt(removepageArr[i-1])-1);
			}
			newStory.setStoryPages(storyPages);
			mav.addObject("newStory", newStory);
		}

		mav.addObject("formUrl", "getChooseMediaForm");

		if(newStory==null)
		{
			mav.addObject("newStory", new Story());
		}

		return mav;
	}
	// Method for open chooseMedia Form 
	@RequestMapping(value = "/story/form/getChooseMediaForm", method = RequestMethod.GET)
	public ModelAndView getChooseMediaForm(@ModelAttribute("newStory") Story newStory)
	{
		ModelAndView mav = new ModelAndView("form/chooseMediaForm");
		mav.addObject("newStory", newStory);
		return mav;
	}

	// Method to Redirect to add Video Form
	@RequestMapping(value = "/story/addVideoForm", method = RequestMethod.GET)
	public ModelAndView addVideoForm(@ModelAttribute("newStory") Story newStory,@RequestParam(value = "pageIndex", defaultValue = "", required = false) String pageIndex)
	{	log.debug("Add Video");
		ModelAndView mav = new ModelAndView("story");
		if(StringUtils.isNotEmpty(pageIndex)){
			mav.addObject("viewPageNo", pageIndex);
		}
		mav.addObject("newStory", newStory);
		mav.addObject("formUrl", "form/getAddVideoForm");
		return mav;
	}
	// Methods to add Video
	@RequestMapping(value = "/story/form/getAddVideoForm", method = RequestMethod.GET)
	public ModelAndView getAddVideoForm(@ModelAttribute("newStory") Story newStory)
	{
		ModelAndView mav = new ModelAndView("form/addVideoForm");
		mav.addObject("videoId", UUID.randomUUID().toString());
		return mav; 
	}
	// Method to Redirect Edit Video Form
	@RequestMapping(value = "/story/editVideoForm")
	public ModelAndView editVideoForm(@ModelAttribute("story") Story story,@RequestParam(value = "pageIndex", defaultValue = "", required = false) String pageIndex)
	{	log.debug("Edit video");
		ModelAndView mav = new ModelAndView("story");
		if(StringUtils.isNotEmpty(pageIndex)){
			mav.addObject("viewPageNo", pageIndex);
		}
		mav.addObject("story", story);
		mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
		mav.addObject("formUrl", "form/getEditVideoForm");
		return mav;
	}

	// Method to Edit Video
	@RequestMapping(value = "/story/form/getEditVideoForm", method = RequestMethod.GET)
	public ModelAndView getEditVideoForm(@ModelAttribute("story") Story story)
	{
		ModelAndView mav = new ModelAndView("form/editVideoForm");
		mav.addObject("videoId", UUID.randomUUID().toString());
		return mav; 
	}
	// Method to add Video
	@RequestMapping(value = "/story/edit/addVideoForm", method = RequestMethod.GET)
	public ModelAndView addVideoFormEdit(@ModelAttribute("story") Story story)
	{
		ModelAndView mav = new ModelAndView("story");
		mav.addObject("story", story);
		mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
		mav.addObject("formUrl", "form/getAddVideoForm");
		return mav;
	}

	@RequestMapping(value = "/story/edit/form/getAddVideoForm", method = RequestMethod.GET)
	public ModelAndView getAddVideoFormEdit(@ModelAttribute("story") Story story)
	{
		ModelAndView mav = new ModelAndView("form/addVideoForm");
		mav.addObject("videoId", UUID.randomUUID().toString());
		return mav; 
	}


	// AUDIO Methods 
	// Edit >> add audio
	@RequestMapping(value = "/story/edit/addAudioForm", method = RequestMethod.GET)
	public ModelAndView addAudioFormEdit(HttpServletRequest request,@ModelAttribute("story") Story story,@RequestParam(value = "pageIndex", defaultValue = "", required = false) String pageIndex)
	{	
		log.debug("Add audio");
		ModelAndView mav = new ModelAndView("story");
		if(StringUtils.isNotEmpty(pageIndex)){
			mav.addObject("viewPageNo", pageIndex);
		}
		mav.addObject("story", story);
		mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
		mav.addObject("formUrl", request.getContextPath()+"/story/edit/getAddAudioForm");
		return mav;
	}
	// Method to Redirect Add Audio Form
	@RequestMapping(value = "/story/addAudioForm", method = RequestMethod.GET)
	public ModelAndView addAudioForm(@ModelAttribute("newStory") Story newStory,@RequestParam(value = "pageIndex", defaultValue = "", required = false) String pageIndex)
	{	log.debug("Add audio");
		ModelAndView mav = new ModelAndView("story");
		if(StringUtils.isNotEmpty(pageIndex)){
			mav.addObject("viewPageNo", pageIndex);
		}
		mav.addObject("newStory", newStory);
		mav.addObject("formUrl", "form/getAddAudioForm");
		return mav;
	}
	// Method to Add Audio 
	@RequestMapping(value = "/story/edit/getAddAudioForm", method = RequestMethod.GET)
	public ModelAndView getAddAudioFormEdit(@ModelAttribute("story") Story story)
	{
		ModelAndView mav = new ModelAndView("form/addAudioForm");
		mav.addObject("videoId", UUID.randomUUID().toString());
		return mav; 
	}

	// Method to Redirect Add Audio Form
	@RequestMapping(value = "/story/form/getAddAudioForm", method = RequestMethod.GET)
	public ModelAndView getAddAudioForm(@ModelAttribute("newStory") Story newStory)
	{
		ModelAndView mav = new ModelAndView("form/addAudioForm");
		mav.addObject("videoId", UUID.randomUUID().toString());
		return mav; 
	}


	// Method to edit Audio 
	@RequestMapping(value = "/story/edit/addAudio", method = RequestMethod.POST)
	public ModelAndView addAudioStoryPageEdit(HttpServletRequest request,@ModelAttribute("story") Story story,
			@RequestParam("file") MultipartFile file,
			@RequestParam("description") String description) throws IOException
			{
		log.debug("Edit audio");
		if(story==null)
		{
			// redirect to home.html
			ModelAndView mav = new ModelAndView("redirect:/home.html");
			return mav;
		}
		else
		{
			// check if uploaded file is of 'audio' type
			String contentType = file.getContentType();
			if(!contentType.contains("audio"))
			{
				// send error message
				ModelAndView mav = new ModelAndView("story");
				mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
				mav.addObject("formUrl", "form/getChooseMediaForm");
				mav.addObject("error", message.getMessage("error.invalidaudio", null, null));
				return mav;
			}

			// add audio storypage to newStory
			try
			{
				story = storyService.addMediaStoryPage(story, file, description, contentType);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			ModelAndView mav = new ModelAndView("redirect:/story/form/editStoryForm.html");
			mav.addObject("flashScope.storyMode", EpooqConstant.STORY_MODE_EDIT);
			mav.addObject("flashScope.viewPageNo", new String(story.getStoryPages().size()+""));
			mav.addObject("story", story);

			return mav;
		}
			}

	// Method to add Audio 
	@RequestMapping(value = "/story/addAudio", method = RequestMethod.POST)
	public ModelAndView addAudioStoryPage(@ModelAttribute("newStory") Story newStory,
			@RequestParam("file") MultipartFile file,
			@RequestParam("description") String description) throws IOException
			{
		log.debug("Add audio");
		if(newStory==null)
		{
			// redirect to chooseMedia
			ModelAndView mav = new ModelAndView("chooseMedia");
			mav.addObject("newStory", new Story());

			return mav;
		}
		else
		{
			// check if uploaded file is of 'audio' type
			String contentType = file.getContentType();
			if(!contentType.contains("audio"))
			{
				// send error message
				ModelAndView mav = new ModelAndView("story");
				mav.addObject("formUrl", "form/getChooseMediaForm");
				mav.addObject("error", message.getMessage("error.invalidaudio", null, null));
				return mav;
			}

			// add audio storypage to newStory
			try
			{
				newStory = storyService.addMediaStoryPage(newStory, file, description, contentType);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			ModelAndView mav = new ModelAndView("redirect:/story/form/storyReviewForm.html");
			mav.addObject("newStory", newStory);

			return mav;
		}
			}

	@RequestMapping(value = "/story/changeAudio", method = RequestMethod.POST)
	public ModelAndView changeAudioStoryPage(@ModelAttribute("newStory") Story newStory,
									 	  	 @RequestParam("file") MultipartFile file,
									 	  	 @RequestParam("pageIndex") int pageIndex)
	{
		if(newStory==null)
		{
			// redirect to chooseMedia
			ModelAndView mav = new ModelAndView("chooseMedia");
			mav.addObject("newStory", new Story());
			
			return mav;
		}
		else
		{
			// check if uploaded file is of 'audio' type
			String contentType = file.getContentType();
			if(!contentType.contains("audio"))
			{
				// send error message
				ModelAndView mav = new ModelAndView("story");
				mav.addObject("formUrl", "form/getStoryReviewForm");
				mav.addObject("error", message.getMessage("error.invalidaudio", null, null));
				mav.addObject("viewPageNo", new String(pageIndex+1+""));
				return mav;
			}
			
			// change audio storypage to newStory
			List<StoryPage> storyPages = newStory.getStoryPages();
			if(storyPages!=null && storyPages.size()>=pageIndex)
			{
				// delete old temp video
				StoryContent storyContent = storyPages.get(pageIndex).getStoryContent();
				String storyContentPath = storyContent.getContent();
				boolean contentDeleted = storyService.removeStoryContent(storyContentPath);				
				
				// add new temp video
				String newContent = null;
				try 
				{
					newContent = storyService.addMediaFile(file, contentType);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				
				if(newContent!=null)
				{
					storyContent.setContent(newContent);
					storyPages.get(pageIndex).setStoryContent(storyContent);
				}
				
				newStory.setStoryPages(storyPages);
			}
			
			// ModelAndView mav = new ModelAndView("storyreview");
			
			ModelAndView mav = new ModelAndView("redirect:/story/form/storyReviewForm.html");
			mav.addObject("newStory", newStory);
			mav.addObject("flashScope.viewPageNo", new String(pageIndex+1+""));
			
			return mav;
		}
	}
	
	@RequestMapping(value = "/story/edit/changeAudio", method = RequestMethod.POST)
	public ModelAndView changeAudioStoryPageEdit(@ModelAttribute("story") Story story,
											@RequestParam("file") MultipartFile file,
									 	  	 @RequestParam("pageIndex") int pageIndex)
	{
		if(story==null)
		{
			// redirect to home.html
			ModelAndView mav = new ModelAndView("redirect:home.html");
			return mav;
		}
		else
		{
			// check if uploaded file is of 'audio' type
			String contentType = file.getContentType();
			if(!contentType.contains("audio"))
			{
				// send error message
				ModelAndView mav = new ModelAndView("story");
				mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
				mav.addObject("formUrl", "../form/getEditStoryForm");
				mav.addObject("error", message.getMessage("error.invalidaudio", null, null));
				mav.addObject("viewPageNo", new String(pageIndex+1+""));
				return mav;
			}
			
			// change video storypage to newStory
			List<StoryPage> storyPages = story.getStoryPages();
			if(storyPages!=null && storyPages.size()>=pageIndex)
			{
				StoryContent storyContent = storyPages.get(pageIndex).getStoryContent();
				
				// add new temp audio
				String newContent = null;
				try 
				{
					newContent = storyService.addMediaFile(file, contentType);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				
				if(newContent!=null)
				{
					storyContent.setContent(newContent);
					storyPages.get(pageIndex).setStoryContent(storyContent);
				}
				
				story.setStoryPages(storyPages);
			}
			
			// ModelAndView mav = new ModelAndView("storyreview");
			
			ModelAndView mav = new ModelAndView("redirect:/story/form/editStoryForm.html");
			mav.addObject("story", story);
			mav.addObject("flashScope.storyMode", EpooqConstant.STORY_MODE_EDIT);
			mav.addObject("flashScope.viewPageNo", new String(pageIndex+1+""));
			
			return mav;
		}
	}
	
	
	@RequestMapping(value = "/story/editAudioForm")
	public ModelAndView editAudioForm(@ModelAttribute("story") Story story,@RequestParam(value = "pageIndex", defaultValue = "", required = false) String pageIndex)
	{	
		log.debug("Edit audio form");
		ModelAndView mav = new ModelAndView("story");
		if(StringUtils.isNotEmpty(pageIndex)){
			mav.addObject("viewPageNo", pageIndex);
		}
		mav.addObject("story", story);
		mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
		mav.addObject("formUrl", "form/getEditAudioForm");
		return mav;
	}
	
	
	@RequestMapping(value = "/story/form/getEditAudioForm", method = RequestMethod.GET)
	public ModelAndView getEditAudioForm(@ModelAttribute("story") Story story)
	{
		ModelAndView mav = new ModelAndView("form/editAudioForm");
		mav.addObject("videoId", UUID.randomUUID().toString());
		return mav; 
	}

	//choose media from edit
	@RequestMapping(value = "/story/edit/form/chooseMediaForm", method = RequestMethod.GET)
	public ModelAndView chooseMediaFormEdit(@ModelAttribute("story") Story story,
			@RequestParam(value = "removePageNo", defaultValue = "", required = false) String removePageNo)
	{	log.debug("Choose media");
		ModelAndView mav = new ModelAndView("story");

		// remove pages no
		if(StringUtils.isNotEmpty(removePageNo))
		{
			String removepageArr[] = removePageNo.split(",");


			List<StoryPage> storyPages = story.getStoryPages();
			for(int i = removepageArr.length; i>0; i--)
			{
				storyPages.remove(Integer.parseInt(removepageArr[i-1])-1);
			}
			story.setStoryPages(storyPages);
			mav.addObject("story", story);
		}

		mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
		mav.addObject("formUrl", "getChooseMediaForm");

		if(story==null)
		{
			mav.addObject("story", new Story());
		}

		return mav;
	}
	
	@RequestMapping(value = "/story/edit/form/getChooseMediaForm", method = RequestMethod.GET)
	public ModelAndView getChooseMediaFormEdit(@ModelAttribute("story") Story story)
	{
		ModelAndView mav = new ModelAndView("form/chooseMediaForm");
		return mav;
	}
	
	// Method to upload video
	@RequestMapping(value = "/story/form/uploadVideoForm", method = RequestMethod.GET)
	public ModelAndView uploadVideoForm(@ModelAttribute("newStory") Story newStory)
	{	log.debug("Upload video");
		if(newStory==null)
		{
			// redirect to chooseMedia
			ModelAndView mav = new ModelAndView("chooseMedia");
			mav.addObject("newStory", new Story());

			return mav;
		}
		else
		{
			//ModelAndView mav = new ModelAndView("form/uploadVideoForm");
			ModelAndView mav = new ModelAndView("story");
			mav.addObject("formUrl", "getUploadVideoForm");
			return mav;
		}
	}
	// Method to validate uploaded video
	@RequestMapping(value = "/story/form/getUploadVideoForm", method = RequestMethod.GET)
	public ModelAndView getUploadVideoForm()
	{
		ModelAndView mav = new ModelAndView("form/uploadVideoForm");
		return mav;
	}
	// Add video from webcam
	@RequestMapping(value = "/story/addWebcamVideo", method = RequestMethod.POST)
	public ModelAndView addWebcamVideoStoryPage(@ModelAttribute("newStory") Story newStory,@RequestParam("videoId") String videoId)
	{	log.debug("Add video from webcam");
		if(newStory==null && StringUtils.isEmpty(videoId))
		{
			// redirect to chooseMedia
			ModelAndView mav = new ModelAndView("chooseMedia");
			mav.addObject("newStory", new Story());

			return mav;
		}

		try
		{
			newStory = storyService.addWebcamStoryPage(newStory, videoId);			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		ModelAndView mav = new ModelAndView("redirect:/story/form/storyReviewForm.html");
		mav.addObject("newStory", newStory);

		return mav;
	}
	// Edit video from webcam
	@RequestMapping(value = "/story/edit/addWebcamVideo", method = RequestMethod.POST)
	public ModelAndView addWebcamVideoStoryPageEdit(@ModelAttribute("story") Story story,
			@RequestParam("videoId") String videoId)
	{	log.debug("Edit video");
		if(story==null && StringUtils.isEmpty(videoId))
		{
			// redirect to home.html
			ModelAndView mav = new ModelAndView("redirect:/home.html");
			return mav;
		}

		try
		{
			story = storyService.addWebcamStoryPage(story, videoId);			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		ModelAndView mav = new ModelAndView("redirect:/story/form/editStoryForm.html");
		mav.addObject("flashScope.storyMode", EpooqConstant.STORY_MODE_EDIT);
		mav.addObject("flashScope.viewPageNo", new String(story.getStoryPages().size()+""));
		mav.addObject("story", story);

		return mav;
	}
	// Change webcam video
	@RequestMapping(value = "/story/changeWebcamVideo", method = RequestMethod.POST)
	public ModelAndView changeWebcamVideoStoryPage(@ModelAttribute("newStory") Story newStory,
			@RequestParam(value="description",defaultValue = "", required = false) String description,
			@RequestParam("pageIndex") int pageIndex,
			@RequestParam("videoId") String videoId)
	{
		log.debug("Change video from webcam");
		if(newStory==null)
		{
			// redirect to chooseMedia
			ModelAndView mav = new ModelAndView("chooseMedia");
			mav.addObject("newStory", new Story());

			return mav;
		}else{
			// check if uploaded file is of 'video' type
			try
			{
				List<StoryPage> storyPages = newStory.getStoryPages();
				if(storyPages!=null && storyPages.size()>=pageIndex)
				{
					StoryContent storyContent = storyPages.get(pageIndex).getStoryContent();
					String storyContentPath = storyContent.getContent();
					storyContent.setType("video/webcam");
					storyContent.setContent("streams/"+videoId+".flv");
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			ModelAndView mav = new ModelAndView("redirect:/story/form/storyReviewForm.html");
			mav.addObject("newStory", newStory);
			mav.addObject("flashScope.viewPageNo", new String(pageIndex+1+""));
			return mav;
		}
	}

	// Edit video from webcam
	@RequestMapping(value = "/story/changeWebcamVideoEdit")
	public ModelAndView changeWebcamVideoStoryPageEdit(@ModelAttribute("story") Story story,
			@RequestParam("description") String description,
			@RequestParam("pageIndex") int pageIndex,
			@RequestParam("videoId") String videoId)
	{	log.debug("Edit video from webcam");
		if(story==null)
		{
			// redirect to chooseMedia
			ModelAndView mav = new ModelAndView("chooseMedia");
			mav.addObject("newStory", new Story());

			return mav;
		}else
		{
			// check if uploaded file is of 'video' type
			try
			{
				List<StoryPage> storyPages = story.getStoryPages();
				if(storyPages!=null && storyPages.size()>=pageIndex)
				{
					StoryContent storyContent = storyPages.get(pageIndex).getStoryContent();
					String storyContentPath = storyContent.getContent();
					storyContent.setType("video/webcam");
					storyContent.setContent("streams/"+videoId+".flv");
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			ModelAndView mav = new ModelAndView("redirect:/story/form/editStoryForm.html");
			mav.addObject("newStory", story);
			mav.addObject("flashScope.storyMode", EpooqConstant.STORY_MODE_EDIT);
			mav.addObject("flashScope.viewPageNo", new String(pageIndex+1+""));
			return mav;
		}
	}

	// Method to change audio by microphone
	@RequestMapping(value = "/story/changeMicrophoneAudio", method = RequestMethod.POST)
	public ModelAndView changeMicrophoneAudio(@ModelAttribute("newStory") Story newStory,
			@RequestParam(value= "description", defaultValue = "", required =false) String description,
			@RequestParam("pageIndex") int pageIndex,
			@RequestParam("videoId") String videoId)
	{
		log.debug("Change microphone audio");
		if(newStory==null)
		{
			// redirect to chooseMedia
			ModelAndView mav = new ModelAndView("chooseMedia");
			mav.addObject("newStory", new Story());

			return mav;
		}else
		{
			// check if uploaded file is of 'video' type
			try
			{
				List<StoryPage> storyPages = newStory.getStoryPages();
				if(storyPages!=null && storyPages.size()>=pageIndex)
				{
					StoryContent storyContent = storyPages.get(pageIndex).getStoryContent();
					String storyContentPath = storyContent.getContent();
					newStory = storyService.changeMicrophoneStoryPage(newStory, videoId,pageIndex);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			ModelAndView mav = new ModelAndView("redirect:/story/form/storyReviewForm.html");
			mav.addObject("newStory", newStory);
			mav.addObject("flashScope.viewPageNo", pageIndex+1);
			return mav;
		}
	}

	// Change microphone audio
	@RequestMapping(value = "/story/changeMicrophoneAudioEdit")
	public ModelAndView changeMicrophoneAudioEdit(@ModelAttribute("story") Story story,
			@RequestParam("description") String description,
			@RequestParam("pageIndex") int pageIndex,
			@RequestParam("videoId") String videoId)
	{
		log.debug("Change microphone audio");
		if(story==null)
		{
			// redirect to chooseMedia
			ModelAndView mav = new ModelAndView("chooseMedia");
			mav.addObject("newStory", new Story());

			return mav;
		}else{
			
			// check if uploaded file is of 'video' type
			try
			{
				List<StoryPage> storyPages = story.getStoryPages();
				if(storyPages!=null && storyPages.size()>=pageIndex)
				{
					StoryContent storyContent = storyPages.get(pageIndex).getStoryContent();
					String storyContentPath = storyContent.getContent();
					story = storyService.changeMicrophoneStoryPage(story, videoId,pageIndex);
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}


			ModelAndView mav = new ModelAndView("redirect:/story/form/editStoryForm.html");
			mav.addObject("newStory", story);
			mav.addObject("flashScope.storyMode", EpooqConstant.STORY_MODE_EDIT);
			mav.addObject("flashScope.viewPageNo", pageIndex+1);
			return mav;
		}
	}

	// Method to add microphone audio
	@RequestMapping(value = "/story/addMicrophoneAudio", method = RequestMethod.POST)
	public ModelAndView addMicrophoneAudioStoryPage(@ModelAttribute("newStory") Story newStory,@RequestParam("videoId") String audioId)
	{	log.debug("Add microphone audio");
		if(newStory==null && StringUtils.isEmpty(audioId))
		{
			// redirect to chooseMedia
			ModelAndView mav = new ModelAndView("chooseMedia");
			mav.addObject("newStory", new Story());
			return mav;
		}

		try
		{
			newStory = storyService.addMicrophoneStoryPage(newStory, audioId);			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		ModelAndView mav = new ModelAndView("redirect:/story/form/storyReviewForm.html");
		mav.addObject("newStory", newStory);
		return mav;
	}

	// Edit microphone audio
	@RequestMapping(value = "/story/edit/addMicrophoneAudio", method = RequestMethod.POST)
	public ModelAndView addMirophoneAudioStoryPageEdit(@ModelAttribute("story") Story story,
			@RequestParam("videoId") String audioId)
	{	log.debug("Edit microphone audio");
		if(story==null && StringUtils.isEmpty(audioId))
		{
			// redirect to home.html
			ModelAndView mav = new ModelAndView("redirect:/home.html");
			return mav;
		}

		try
		{
			story = storyService.addMicrophoneStoryPage(story, audioId);			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		ModelAndView mav = new ModelAndView("redirect:/story/form/editStoryForm.html");
		mav.addObject("flashScope.storyMode", EpooqConstant.STORY_MODE_EDIT);
		mav.addObject("flashScope.viewPageNo", new String(story.getStoryPages().size()+""));
		mav.addObject("story", story);

		return mav;
	}
	
	// Method to add video
	@RequestMapping(value = "/story/addVideo", method = RequestMethod.POST)
	public ModelAndView addVideoStoryPage(@ModelAttribute("newStory") Story newStory,
			@RequestParam("file") MultipartFile file,
			@RequestParam("description") String description,
			@RequestParam("pageIndex") String pageIndex)
	{
		log.debug("Add video");
		if(newStory==null)
		{
			// redirect to chooseMedia
			ModelAndView mav = new ModelAndView("chooseMedia");
			mav.addObject("newStory", new Story());

			return mav;
		}
		else
		{
			// check if uploaded file is of 'video' type
			String contentType = file.getContentType();
			if(!contentType.contains("video"))
			{
				// send error message
				ModelAndView mav = new ModelAndView("story");
				mav.addObject("formUrl", "form/getChooseMediaForm");
				String error = message.getMessage("error.invalidvideo", null, null);
				mav.addObject("error", error);
				return mav;
			}

			// add video storypage to newStory
			try
			{
				newStory = storyService.addMediaStoryPage(newStory, file, description, contentType);				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			// ModelAndView mav = new ModelAndView("storyreview");

			ModelAndView mav = new ModelAndView("redirect:/story/form/storyReviewForm.html");
			mav.addObject("newStory", newStory);
			if(StringUtils.isNotEmpty(pageIndex)){
			
				mav.addObject("viewPageNo", new String((Integer.parseInt(pageIndex))+""));
			}
			return mav;
		}
	}
	// Change video
	@RequestMapping(value = "/story/changeVideo", method = RequestMethod.POST)
	public ModelAndView changeVideoStoryPage(@ModelAttribute("newStory") Story newStory,
			@RequestParam("file") MultipartFile file,
			@RequestParam("description") String description,
			@RequestParam("pageIndex") int pageIndex)
	{
		log.debug("Change video");
		if(newStory==null)
		{
			// redirect to chooseMedia
			ModelAndView mav = new ModelAndView("chooseMedia");
			mav.addObject("newStory", new Story());

			return mav;
		}else
		{
			// check if uploaded file is of 'video' type
			String contentType = file.getContentType();
			if(!contentType.contains("video"))
			{
				// send error message
				ModelAndView mav = new ModelAndView("story");
				mav.addObject("formUrl", "form/getStoryReviewForm");
				String error = message.getMessage("error.invalidvideo", null, null);
				mav.addObject("error", error);
				mav.addObject("viewPageNo", new String(pageIndex+1+""));
				return mav;
			}

			// change video storypage to newStory
			List<StoryPage> storyPages = newStory.getStoryPages();
			if(storyPages!=null && storyPages.size()>=pageIndex)
			{
				// delete old temp video
				StoryContent storyContent = storyPages.get(pageIndex).getStoryContent();
				String storyContentPath = storyContent.getContent();
				boolean contentDeleted = storyService.removeStoryContent(storyContentPath);				

				// add new temp video
				String newContent = null;
				try 
				{
					newContent = storyService.addMediaFile(file, contentType);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}

				if(newContent!=null)
				{
					storyContent.setContent(newContent);
					storyPages.get(pageIndex).setStoryContent(storyContent);
				}

				newStory.setStoryPages(storyPages);
			}

			// ModelAndView mav = new ModelAndView("storyreview");

			ModelAndView mav = new ModelAndView("redirect:/story/form/storyReviewForm.html");
			mav.addObject("newStory", newStory);
			mav.addObject("flashScope.viewPageNo", new String(pageIndex+1+""));

			return mav;
		}
	}




	// changeVideoStoryPage_Edit will be call when user try to update uploaded file.

	@RequestMapping(value = "/story/edit/changeVideo", method = RequestMethod.POST)
	public ModelAndView changeVideoStoryPageEdit(@ModelAttribute("story") Story story,
			@RequestParam("file") MultipartFile file,
			@RequestParam("pageIndex") int pageIndex)
	{	
		log.debug("Change uploaded video");
		if(story==null)
		{
			// redirect to home.html
			ModelAndView mav = new ModelAndView("redirect:home.html");
			return mav;
		}
		else
		{
			// check if uploaded file is of 'video' type
			String contentType = file.getContentType();
			if(!contentType.contains("video"))
			{
				// send error message
				ModelAndView mav = new ModelAndView("story");
				mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
				mav.addObject("formUrl", "../form/getEditStoryForm");
				mav.addObject("error", message.getMessage("error.invalidvideo", null, null));
				mav.addObject("viewPageNo", new String(pageIndex+1+""));
				return mav;
			}

			// change video storypage to newStory
			List<StoryPage> storyPages = story.getStoryPages();
			if(storyPages!=null && storyPages.size()>=pageIndex)
			{
				// delete old temp video
				StoryContent storyContent = storyPages.get(pageIndex).getStoryContent();
				String storyContentPath = storyContent.getContent();
				boolean contentDeleted = storyService.removeStoryContent(storyContentPath);				
				storyContent.setType(file.getContentType());
				// add new temp video
				String newContent = null;
				try 
				{
					newContent = storyService.addMediaFile(file, contentType);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}

				if(newContent!=null)
				{
					storyContent.setContent(newContent);
					storyPages.get(pageIndex).setStoryContent(storyContent);
				}

				story.setStoryPages(storyPages);
			}


			// ModelAndView mav = new ModelAndView("storyreview");

			ModelAndView mav = new ModelAndView("redirect:/story/form/editStoryForm.html");
			mav.addObject("story", story);
			mav.addObject("flashScope.storyMode", EpooqConstant.STORY_MODE_EDIT);
			mav.addObject("flashScope.viewPageNo", new String(pageIndex+1+""));

			return mav;
		}
	}
	

	// Change uploaded audio

	
	// Change image
	@RequestMapping(value = "/story/changeImage", method = RequestMethod.POST)
	public ModelAndView changeImageStoryPage(@ModelAttribute("newStory") Story newStory,
			@RequestParam("imageFile") MultipartFile file,
			@RequestParam("storyPageListIndex") int storyPageListIndex)
	{	
		log.debug("Change image");
		if(newStory==null)
		{
			// redirect to chooseMedia
			ModelAndView mav = new ModelAndView("chooseMedia");
			mav.addObject("newStory", new Story());

			return mav;
		}
		else
		{
			// check if uploaded file is of 'image' type
			String contentType = file.getContentType();
			if(!contentType.contains("image"))
			{
				// send error message
				ModelAndView mav = new ModelAndView("story");
				mav.addObject("formUrl", "form/getStoryReviewForm");
				mav.addObject("error", message.getMessage("error.invalidimage", null, null));
				mav.addObject("viewPageNo", new String(storyPageListIndex+1+""));
				return mav;
			}

			// change audio storypage to newStory
			List<StoryPage> storyPages = newStory.getStoryPages();
			if(storyPages!=null && storyPages.size()>=storyPageListIndex)
			{
				// delete old temp video
				StoryContent storyContent = storyPages.get(storyPageListIndex).getStoryContent();
				String storyContentPath = storyContent.getContent();
				boolean contentDeleted = storyService.removeStoryContent(storyContentPath);				

				// add new temp video
				String newContent = null;
				try 
				{
					newContent = storyService.addMediaFile(file, contentType);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}

				if(newContent!=null)
				{
					storyContent.setContent(newContent);
					storyPages.get(storyPageListIndex).setStoryContent(storyContent);
				}

				newStory.setStoryPages(storyPages);
			}

			// ModelAndView mav = new ModelAndView("storyreview");

			ModelAndView mav = new ModelAndView("redirect:/story/form/storyReviewForm.html");
			mav.addObject("newStory", newStory);
			mav.addObject("flashScope.viewPageNo", new String(storyPageListIndex+1+""));

			return mav;
		}
	}
	
	// Change uploade image
	@RequestMapping(value = "/story/edit/changeImage", method = RequestMethod.POST)
	public ModelAndView changeImageStoryPageEdit(@ModelAttribute("story") Story story,
			@RequestParam("imageFile") MultipartFile file,
			@RequestParam("storyPageListIndex") int storyPageListIndex)
	{	log.debug("Change uploaded image");
		if(story==null)
		{
			// redirect to home.html
			ModelAndView mav = new ModelAndView("redirect:home.html");
			return mav;
		}
		else
		{
			// check if uploaded file is of 'image' type
			String contentType = file.getContentType();
			if(!contentType.contains("image"))
			{
				// send error message
				ModelAndView mav = new ModelAndView("story");
				mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
				mav.addObject("formUrl", "../form/getEditStoryForm");
				mav.addObject("error", message.getMessage("error.invalidimage", null, null));
				mav.addObject("viewPageNo", new String(storyPageListIndex+1+""));
				return mav;
			}

			// change video storypage to newStory
			List<StoryPage> storyPages = story.getStoryPages();
			if(storyPages!=null && storyPages.size()>=storyPageListIndex)
			{
				StoryContent storyContent = storyPages.get(storyPageListIndex).getStoryContent();

				// add new temp image
				String newContent = null;
				try 
				{
					newContent = storyService.addMediaFile(file, contentType);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}

				if(newContent!=null)
				{
					storyContent.setContent(newContent);
					storyPages.get(storyPageListIndex).setStoryContent(storyContent);
				}

				story.setStoryPages(storyPages);
			}

			// ModelAndView mav = new ModelAndView("storyreview");

			ModelAndView mav = new ModelAndView("redirect:/story/form/editStoryForm.html");
			mav.addObject("story", story);
			mav.addObject("flashScope.storyMode", EpooqConstant.STORY_MODE_EDIT);
			mav.addObject("flashScope.viewPageNo", new String(storyPageListIndex+1+""));

			return mav;
		}
	}
	// Edit video
	@RequestMapping(value = "/story/edit/addVideo", method = RequestMethod.POST)
	public ModelAndView addVideoStoryPageEdit(HttpServletRequest request,@ModelAttribute("story") Story story,
			@RequestParam("file") MultipartFile file,
			@RequestParam("description") String description)
	{	log.debug("Edit video");
		if(story==null)
		{
			// redirect to home.html
			ModelAndView mav = new ModelAndView("redirect:/home.html");
			return mav;
		}
		else
		{
			// check if uploaded file is of 'video' type
			String contentType = file.getContentType();
			if(!contentType.contains("video"))
			{
				// send error message
				ModelAndView mav = new ModelAndView("story");
				mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
				mav.addObject("formUrl", "form/getChooseMediaForm");
				String error = message.getMessage("error.invalidvideo", null, null);
				mav.addObject("error", error);
				return mav;
			}

			// add video storypage to newStory
			try
			{
				story = storyService.addMediaStoryPage(story, file, description, contentType);				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			ModelAndView mav = new ModelAndView("redirect:/story/form/editStoryForm.html");
			mav.addObject("flashScope.storyMode", EpooqConstant.STORY_MODE_EDIT);
			mav.addObject("flashScope.viewPageNo", new String(story.getStoryPages().size()+""));
			mav.addObject("story", story);

			return mav;
		}
	}
	// Method to upload image
	@RequestMapping(value = "/story/uploadImage", method = RequestMethod.GET)
	public ModelAndView uploadImage(@ModelAttribute("newStory") Story newStory)
	{	log.debug("Upload image");
		if(newStory==null)
		{
			// redirect to chooseMedia
			ModelAndView mav = new ModelAndView("chooseMedia");
			mav.addObject("newStory", new Story());

			return mav;
		}
		else
		{
			ModelAndView mav = new ModelAndView("uploadImage");
			return mav;
		}
	}
	
	// Add image
	@RequestMapping(value = "/story/addImage", method = RequestMethod.POST)
	public ModelAndView addImageStoryPage(@ModelAttribute("newStory") Story newStory,
			@RequestParam("file") MultipartFile file,
			@RequestParam("description") String description) throws IOException
	{
		log.debug("Add image");
		if(newStory==null)
		{
			// redirect to chooseMedia
			ModelAndView mav = new ModelAndView("chooseMedia");
			mav.addObject("newStory", new Story());

			return mav;
		}
		else
		{
			// check if uploaded file is of 'image' type
			String contentType = file.getContentType();
			if(!contentType.contains("image"))
			{
				// send error message
				ModelAndView mav = new ModelAndView("story");
				mav.addObject("formUrl", "form/getChooseMediaForm");
				mav.addObject("error", message.getMessage("error.invalidimage", null, null));
				return mav;
			}

			// add image storypage to newStory
			try
			{
				newStory = storyService.addMediaStoryPage(newStory, file, description, contentType);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			//ModelAndView mav = new ModelAndView("storyreview");
			ModelAndView mav = new ModelAndView("redirect:/story/form/storyReviewForm.html");
			mav.addObject("newStory", newStory);

			return mav;
		}
			}
	// Edit image
	@RequestMapping(value = "/story/edit/addImage", method = RequestMethod.POST)
	public ModelAndView addImageStoryPageEdit(HttpServletRequest request,@ModelAttribute("story") Story story,
			@RequestParam("file") MultipartFile file,
			@RequestParam("description") String description) throws IOException
			{
		log.debug("Edit image");
		if(story==null)
		{
			// redirect to chooseMedia
			ModelAndView mav = new ModelAndView("redirect:/home.html");
			return mav;
		}
		else
		{
			// check if uploaded file is of 'image' type
			String contentType = file.getContentType();
			if(!contentType.contains("image"))
			{
				// send error message
				ModelAndView mav = new ModelAndView("story");
				mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
				mav.addObject("formUrl", "form/getChooseMediaForm");
				mav.addObject("error", message.getMessage("error.invalidimage", null, null));
				return mav;
			}

			// add image storypage to newStory
			try
			{
				story = storyService.addMediaStoryPage(story, file, description, contentType);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			//ModelAndView mav = new ModelAndView("storyreview");
			ModelAndView mav = new ModelAndView("redirect:/story/form/editStoryForm.html");
			mav.addObject("flashScope.storyMode", EpooqConstant.STORY_MODE_EDIT);
			mav.addObject("flashScope.viewPageNo", new String(story.getStoryPages().size()+""));
			mav.addObject("story", story);

			return mav;
		}
			}
	
	// Upload audio
	@RequestMapping(value = "/story/uploadAudio", method = RequestMethod.GET)
	public ModelAndView uploadAudio(@ModelAttribute("newStory") Story newStory)
	{	log.debug("Upload audio");
		if(newStory==null)
		{
			// redirect to chooseMedia
			ModelAndView mav = new ModelAndView("chooseMedia");
			mav.addObject("newStory", new Story());

			return mav;
		}
		else
		{
			ModelAndView mav = new ModelAndView("uploadAudio");
			return mav;
		}
	}

	// Method to add narration audio

	@RequestMapping(value = "/story/addNarration", method = RequestMethod.POST)
	public ModelAndView addNarration(@ModelAttribute("newStory") Story newStory,
			@RequestParam("narrationFile") MultipartFile file,
			@RequestParam("storyPageListIndex") int storyPageListIndex) throws IOException
			{
		log.debug("Add narration");
		if(newStory==null)
		{
			// redirect to chooseMedia
			ModelAndView mav = new ModelAndView("chooseMedia");
			mav.addObject("newStory", new Story());

			return mav;
		}
		else
		{
			// check if uploaded file is of 'audio' type
			String contentType = file.getContentType();
			if(!contentType.contains("audio"))
			{
				// send error message
				ModelAndView mav = new ModelAndView("story");
				mav.addObject("formUrl", "form/getStoryReviewForm");
				mav.addObject("error", message.getMessage("error.invalidaudio", null, null));
				mav.addObject("viewPageNo", new String(storyPageListIndex+1+""));
				return mav;
			}

			// change audio storypage to newStory
			List<StoryPage> storyPages = newStory.getStoryPages();
			if(storyPages!=null && storyPages.size()>=storyPageListIndex)
			{
				// delete old temp video
				StoryContent storyContent = storyPages.get(storyPageListIndex).getStoryContent();
				char narration = storyContent.getNarrationAudio();
				if(CharUtils.toString(narration).equals("Y"))
				{
					String narrationAudioPath = storyContent.getDescription();
					boolean contentDeleted = storyService.removeStoryContent(narrationAudioPath);					
				}

				// add new narration video
				String newNarration = null;
				try 
				{
					newNarration = storyService.addMediaFile(file, contentType);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}

				if(newNarration!=null)
				{
					storyContent.setDescription(newNarration);
					storyContent.setNarrationAudio('Y');
					storyPages.get(storyPageListIndex).setStoryContent(storyContent);
				}

				newStory.setStoryPages(storyPages);
			}

			// ModelAndView mav = new ModelAndView("storyreview");

			ModelAndView mav = new ModelAndView("redirect:/story/form/storyReviewForm.html");
			mav.addObject("newStory", newStory);
			mav.addObject("flashScope.viewPageNo", new String(storyPageListIndex+1+""));

			return mav;
		}
			}
	// Edit narration
	@RequestMapping(value = "/story/edit/addNarration", method = RequestMethod.POST)
	public ModelAndView addNarrationEdit(@ModelAttribute("story") Story story,
			@RequestParam("narrationFile") MultipartFile file,
			@RequestParam("storyPageListIndex") int storyPageListIndex) throws IOException
			{
		log.debug("Edit narration");
		if(story==null)
		{
			// redirect to home.html
			ModelAndView mav = new ModelAndView("redirect:home.html");
			return mav;
		}
		else
		{
			// check if uploaded file is of 'audio' type
			String contentType = file.getContentType();
			if(!contentType.contains("audio"))
			{
				// send error message
				ModelAndView mav = new ModelAndView("story");
				mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
				mav.addObject("formUrl", "form/getEditStoryForm");
				mav.addObject("error", message.getMessage("error.invalidaudio", null, null));
				mav.addObject("viewPageNo", new String(storyPageListIndex+1+""));
				return mav;
			}

			// change audio storypage to newStory
			List<StoryPage> storyPages = story.getStoryPages();
			if(storyPages!=null && storyPages.size()>=storyPageListIndex)
			{
				StoryContent storyContent = storyPages.get(storyPageListIndex).getStoryContent();

				// add new narration video
				String newNarration = null;
				try 
				{
					newNarration = storyService.addMediaFile(file, contentType);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}

				if(newNarration!=null)
				{
					storyContent.setDescription(newNarration);
					storyContent.setNarrationAudio('Y');
					storyPages.get(storyPageListIndex).setStoryContent(storyContent);
				}

				story.setStoryPages(storyPages);
			}

			ModelAndView mav = new ModelAndView("redirect:/story/form/editStoryForm.html");
			mav.addObject("story", story);
			mav.addObject("flashScope.storyMode", EpooqConstant.STORY_MODE_EDIT);
			mav.addObject("flashScope.viewPageNo", new String(storyPageListIndex+1+""));

			return mav;
		}
			}
	// Add story text
	@RequestMapping(value = "/story/uploadText", method = RequestMethod.GET)
	public ModelAndView uploadText(@ModelAttribute("newStory") Story newStory)
	{	log.debug("Add story text");
		if(newStory==null)
		{
			// redirect to chooseMedia
			ModelAndView mav = new ModelAndView("chooseMedia");
			mav.addObject("newStory", new Story());

			return mav;
		}
		else
		{
			ModelAndView mav = new ModelAndView("uploadText");
			return mav;
		}
	}

	@RequestMapping(value = "/story/addText", method = RequestMethod.POST)
	public ModelAndView addTextStoryPage(@ModelAttribute("newStory") Story newStory,
			@RequestParam("text") String text)
	{
		if(newStory==null)
		{
			// redirect to chooseMedia
			ModelAndView mav = new ModelAndView("chooseMedia");
			mav.addObject("newStory", new Story());

			return mav;
		}
		else
		{
			// add text to newStory
			try
			{
				newStory = storyService.addTextStoryPage(newStory, text);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			//ModelAndView mav = new ModelAndView("storyreview");
			ModelAndView mav = new ModelAndView("redirect:/story/form/storyReviewForm.html");
			mav.addObject("newStory", newStory);

			return mav;
		}
	}
	// Edit story text
	@RequestMapping(value = "/story/edit/addText", method = RequestMethod.POST)
	public ModelAndView addTextStoryPageEdit(@ModelAttribute("story") Story story,
			@RequestParam("text") String text)
	{	log.debug("Edit story text");
		if(story==null)
		{
			// redirect to chooseMedia
			ModelAndView mav = new ModelAndView("chooseMedia");
			mav.addObject("newStory", new Story());

			return mav;
		}
		else
		{
			// add text to newStory
			try
			{
				story = storyService.addTextStoryPage(story, text);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			//ModelAndView mav = new ModelAndView("storyreview");
			ModelAndView mav = new ModelAndView("redirect:/story/form/editStoryForm.html");
			mav.addObject("flashScope.storyMode", EpooqConstant.STORY_MODE_EDIT);
			mav.addObject("flashScope.viewPageNo", new String(story.getStoryPages().size()+""));
			mav.addObject("story", story);

			return mav;
		}
	}
	// Method to cancel story page
	@RequestMapping(value = "/story/cancelStoryPage", method = RequestMethod.GET)
	public ModelAndView cancelStoryPage(@ModelAttribute("newStory") Story newStory,
			@RequestParam("pageNo") int pageNo)
	{	log.debug("cancel story page");

		if(newStory==null)
		{
			// redirect to add.html
			//return "redirect:add.html";
			return new ModelAndView("redirect:/add.html");
		}

		List<StoryPage> storyPages = newStory.getStoryPages();
		if(storyPages!=null && storyPages.size()>=pageNo)
		{
			StoryContent storyContent = storyPages.get(pageNo-1).getStoryContent();
			if(storyContent.getType()!="TEXT")
			{
				String storyContentPath = storyContent.getContent();

				boolean contentDeleted = storyService.removeStoryContent(storyContentPath);				
			}

			storyPages.remove(pageNo-1);
			newStory.setStoryPages(storyPages);

		}

		if(storyPages.size()!=0)
		{

			ModelAndView mav=new ModelAndView("redirect:form/storyReviewForm.html");

			if(storyPages.size()<pageNo)
			{
				mav.addObject("flashScope.viewPageNo", new String(""+(pageNo-1)));
			}
			else
			{
				mav.addObject("flashScope.viewPageNo", new String(""+pageNo));
			}

			//return "redirect:/story/form/storyReviewForm.html";
			return mav;
		}
		else
		{	
			//return "redirect:/story/form/chooseMediaForm.html";
			ModelAndView mav=new ModelAndView("redirect:/story/form/chooseMediaForm.html");
			return mav;

		}
	}

	// Cancel story page
	@RequestMapping(value = "/story/edit/cancelStoryPage", method = RequestMethod.GET)
	public ModelAndView cancelStoryPageEdit(@ModelAttribute("story") Story story,
			@RequestParam("pageNo") int pageNo,
			SessionStatus status)
	{
		log.debug("Cancel story page");
		if(story==null)
		{
			// redirect to add.html
			//return "redirect:add.html";
			ModelAndView mav=new ModelAndView("redirect:/add.html");
			return mav;
		}

		List<StoryPage> storyPages = story.getStoryPages();
		if(storyPages!=null && storyPages.size()>=pageNo)
		{
			StoryContent storyContent = storyPages.get(pageNo-1).getStoryContent();
			if(storyContent.getType()!="TEXT")
			{
				String storyContentPath = storyContent.getContent();
			}

			storyPages.remove(pageNo-1);
			story.setStoryPages(storyPages);
		}

		if(storyPages.size()!=0)
		{	
			ModelAndView mav=new ModelAndView("redirect:/story/form/editStoryForm.html");
			//mav.addObject("story",story);
			mav.addObject("flashScope.storyMode", EpooqConstant.STORY_MODE_EDIT);

			if(storyPages.size()<pageNo)
			{
				mav.addObject("flashScope.viewPageNo", new String(""+(pageNo-1)));
			}
			else
			{
				mav.addObject("flashScope.viewPageNo", new String(""+pageNo));
			}

			//return "redirect:/story/form/editStoryForm.html";
			return mav;
		}
		else
		{
			ModelAndView mav=new ModelAndView("redirect:/story/edit/form/chooseMediaForm.html");
			mav.addObject("flashScope.storyMode", EpooqConstant.STORY_MODE_EDIT);

			return mav;
		}
	}
	// Story review form
	@RequestMapping(value = "/story/form/storyReviewForm", method = RequestMethod.GET)
	public ModelAndView storyReviewForm(@ModelAttribute(value="newStory") Story newStory,
			@ModelAttribute("viewPageNo") String viewPageNo)
	{	log.debug("Story review form");
		if(newStory.getStoryPages()==null || newStory.getStoryPages().size()==0)
		{
			ModelAndView mav = new ModelAndView("story");
			mav.addObject("newStory", newStory);
			mav.addObject("formUrl", "getChooseMediaForm");

			return mav;
		}

		ModelAndView mav = new ModelAndView("story");
		mav.addObject("newStory", newStory);
		mav.addObject("formUrl", "getStoryReviewForm");
		if(StringUtils.isNotEmpty(viewPageNo))
		{
			mav.addObject("viewPageNo", viewPageNo);
		}

		return mav;
	}

	@RequestMapping(value = "/story/form/getStoryReviewForm", method = RequestMethod.GET)
	public ModelAndView getStoryReviewForm(@ModelAttribute("newStory") Story newStory)
	{
		ModelAndView mav = new ModelAndView("form/storyReviewForm");
		mav.addObject("newStory", newStory);
		return mav; 
	}
	// Accept story
	@RequestMapping(value = "/story/acceptStory", method = RequestMethod.POST)
	public ModelAndView acceptStoryForm(@ModelAttribute("newStory") Story newStory,
			@RequestParam(value = "removePageNo", defaultValue = "", required = false) String removePageNo)
	{	log.debug("Accept story");
		if(newStory==null)
		{
			// redirect to add.html
			return new ModelAndView("redirect:add.html");
		}

		// remove pages no
		if(StringUtils.isNotEmpty(removePageNo))
		{
			String removepageArr[] = removePageNo.split(",");


			List<StoryPage> storyPages = newStory.getStoryPages();
			for(int i = removepageArr.length; i>0; i--)
			{
				storyPages.remove(Integer.parseInt(removepageArr[i-1])-1);
			}

			if(storyPages.isEmpty())
			{
				// redirect to chooseMedia form
				ModelAndView mav = new ModelAndView("story");
				mav.addObject("newStory", newStory);
				mav.addObject("formUrl", "form/getChooseMediaForm");

				return mav;
			}

			newStory.setStoryPages(storyPages);
		}

		// check if any story pages exists or not


		ModelAndView mav = new ModelAndView("story");
		mav.addObject("formUrl", "getAcceptStoryForm");
		return mav;
	}
	// Accept story form
	@RequestMapping(value = "/story/edit/acceptStory", method = RequestMethod.POST)
	public ModelAndView acceptStoryFormEdit(@ModelAttribute("story") Story story,
			@RequestParam(value = "removePageNo", defaultValue = "", required = false) String removePageNo)
	{	log.debug("Accept edited story");
		if(story==null)
		{
			// redirect to add.html
			return new ModelAndView("redirect:add.html");
		}

		// remove pages no
		if(StringUtils.isNotEmpty(removePageNo))
		{
			String removepageArr[] = removePageNo.split(",");


			List<StoryPage> storyPages = story.getStoryPages();
			for(int i = removepageArr.length; i>0; i--)
			{
				storyPages.remove(Integer.parseInt(removepageArr[i-1])-1);
			}

			if(storyPages.isEmpty())
			{
				// redirect to chooseMedia form
				ModelAndView mav = new ModelAndView("story");
				mav.addObject("story", story);
				mav.addObject("formUrl", "form/getChooseMediaForm");

				return mav;
			}

			story.setStoryPages(storyPages);
		}

		// check if any story pages exists or not


		ModelAndView mav = new ModelAndView("story");
		mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
		mav.addObject("formUrl", "getAcceptStoryForm");
		return mav;
	}

	@RequestMapping(value = "/story/getAcceptStoryForm", method = RequestMethod.GET)
	public ModelAndView getAcceptStoryForm(@ModelAttribute("newStory") Story newStory)
	{
		ModelAndView mav = new ModelAndView("form/acceptStoryForm");
		return mav;
	}

	@RequestMapping(value = "/story/edit/getAcceptStoryForm", method = RequestMethod.GET)
	public ModelAndView getAcceptStoryFormEdit(@ModelAttribute("story") Story story)
	{
		ModelAndView mav = new ModelAndView("form/acceptEditedStoryForm");
		return mav;
	}
	// Cancel story
	@RequestMapping(value = "/story/cancelStory", method = RequestMethod.GET)
	public String cancelStory(HttpServletRequest request,@ModelAttribute("newStory") Story newStory,
			SessionStatus status)
	{	log.debug("Cancel story");
		status.setComplete();


		return "redirect:/home.html";
	}
	
	// Cancel edited story
	@RequestMapping(value = "/story/edit/cancelStory", method = RequestMethod.GET)
	public String cancelStoryEdit(HttpServletRequest request,@ModelAttribute("story") Story story,
			SessionStatus status)
	{	log.debug("Cancel edited story");
		status.setComplete();

		return "redirect:/home.html";
	}
	// Save story
	@RequestMapping(value = "/story/saveStory", method = RequestMethod.POST)
	public String saveStory(HttpServletRequest request,@ModelAttribute("newStory") Story newStory,
			@RequestParam("storyDay") int storyDay,
			@RequestParam("storyMonth") int storyMonth,
			@RequestParam("storyYear") int storyYear,
			@RequestParam(value="lattitude", required=false) String lattitude,
			@RequestParam(value="longitude", required=false) String longitude,
			//@RequestParam("publishing") String publishing,
			SessionStatus status,
			HttpServletRequest httpRequest,
			Model model)
	{
		log.debug("Save story");
		if(newStory==null)
		{
			// redirect to add.html
			return "redirect:add.html";
		}

		User user = (User) httpRequest.getSession().getAttribute("userSession");

		if(user==null)
		{
			// redirect to add.html
			return "redirect:add.html";
		}

		// check title is entered
		if(StringUtils.isEmpty(newStory.getTitle()))
		{
			model.addAttribute("formUrl", "getAcceptStoryForm");
			model.addAttribute("error",message.getMessage("story.titlerequired", null, null));

			return "story";
		}

		Date storyDate=DateUtil.convertStringToDate(storyDay, storyMonth, storyYear);
		if(storyDate==null)
		{
			model.addAttribute("formUrl", "getAcceptStoryForm");
			model.addAttribute("error",message.getMessage("story.invaliddate", null, null));

			return "story";
		}

		// set user inside story
		newStory.setUser(user);
		newStory.setStoryDate(storyDate);
		newStory.setCreated(new Date());
		newStory.setLattitude(lattitude);
		newStory.setLongitude(longitude);

		// insert story into database
		storyService.saveStory(newStory,
				"PUBLIC");

		status.setComplete();

		// redirect to home page
		return "redirect:/home.html";
	}
	// Update story
	@RequestMapping(value = "/story/updateStory", method = RequestMethod.POST)
	public String updateStory(HttpServletRequest request,@ModelAttribute("story") Story story,
			@RequestParam("storyDay") int storyDay,
			@RequestParam("storyMonth") int storyMonth,
			@RequestParam("storyYear") int storyYear,
			SessionStatus status,
			HttpServletRequest httpRequest,
			Model model)
	{
		log.debug("Update story");
		if(story==null)
		{
			return "redirect:add.html";
		}

		User user = (User) httpRequest.getSession().getAttribute("userSession");

		if(user==null)
		{
			// redirect to add.html
			return "redirect:add.html";
		}

		// check title is entered
		if(StringUtils.isEmpty(story.getTitle()))
		{
			model.addAttribute("formUrl", "edit/getAcceptStoryForm");
			model.addAttribute("error",message.getMessage("story.titlerequired", null, null));

			return "story";
		}

		Date storyDate=DateUtil.convertStringToDate(storyDay, storyMonth, storyYear);
		if(storyDate==null)
		{
			model.addAttribute("formUrl", "edit/getAcceptStoryForm");
			model.addAttribute("error",message.getMessage("story.invaliddate", null, null));

			return "story";
		}

		// set user inside story
		story.setStoryDate(storyDate);

		// insert story into database
		storyService.updateStory(story,
				"PUBLIC");

		status.setComplete();

		// redirect to home page
		return "redirect:/home.html";
	}
	// Edit story
	@RequestMapping(value = "/story/editStory", method = RequestMethod.POST)
	public ModelAndView editStory(HttpServletRequest request,@RequestParam(value="storyId") int storyId,
			HttpServletRequest httpRequest)
	{
		log.debug("Edit story");
		User user = (User) httpRequest.getSession().getAttribute("userSession");

		// Get the story object
		Story story = storyService.getStoryById(storyId);

		if(user==null || story.getUser().getId()!= user.getId())
		{
			// redirect to add.html
			return new ModelAndView("redirect:/home.html");
		}

		ModelAndView mav = new ModelAndView("story");
		mav.addObject("story", story);
		mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
		mav.addObject("formUrl", "form/getEditStoryForm");

		return mav;
	}

	@RequestMapping(value = "/story/form/editStoryForm", method = RequestMethod.GET)
	public ModelAndView editStoryForm(@ModelAttribute(value="story") Story story,
			@ModelAttribute("viewPageNo") String viewPageNo)
	{	
		if(story.getStoryPages()==null || story.getStoryPages().size()==0)
		{
			ModelAndView mav = new ModelAndView("story");
			mav.addObject("story", story);
			mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
			mav.addObject("formUrl", "getChooseMediaForm");

			return mav;
		}

		ModelAndView mav = new ModelAndView("story");
		mav.addObject("story", story);
		mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
		mav.addObject("formUrl", "getEditStoryForm");
		if(StringUtils.isNotEmpty(viewPageNo))
		{
			mav.addObject("viewPageNo", viewPageNo);
		}

		return mav;
	}

	@RequestMapping(value = "/story/form/getEditStoryForm", method = RequestMethod.GET)
	public ModelAndView getEditStoryForm(@ModelAttribute("story") Story story)
	{
		ModelAndView mav = new ModelAndView("form/editStoryForm");
		mav.addObject("story", story);
		return mav;
	}
	// Remove story
	@RequestMapping(value = "/story/removeStory", method = RequestMethod.POST)
	public ModelAndView removeStory(HttpServletRequest request,@RequestParam("storyId") int storyId,
			HttpServletRequest httpRequest)
	{	log.debug("Remove story");
		User user = (User) httpRequest.getSession().getAttribute("userSession");

		// Get the story object
		Story story = storyService.getStoryById(storyId);

		if(user==null || story.getUser().getId()!= user.getId())
		{
			// redirect to add.html
			return new ModelAndView("redirect:/home.html");
		}

		storyService.removeStory(story);

		return new ModelAndView("redirect:/home.html");
	}
}
