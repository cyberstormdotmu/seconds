package fi.korri.epooq.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fi.korri.epooq.dao.StoryDao;
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

	@Autowired
	private StoryService storyService;
	
	@Autowired
	private ReloadableResourceBundleMessageSource message;

	@RequestMapping(value = "/story/get", method = RequestMethod.GET)
	public ModelAndView getStory(@RequestParam("storyId") long storyId)
	{
		Story story = storyService.getStoryById(storyId);
		
		ModelAndView mav = new ModelAndView("form/viewStoryForm");
		mav.addObject("story", story);
		
		return mav;
	}
	
	
	@RequestMapping(value = "/story/getStoryByTitle", method = RequestMethod.POST)
	@ResponseBody
	public String getStoryByTitle(@RequestParam("storyTitle") String storyTitle)
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
	
	@RequestMapping(value = "/story/add", method = RequestMethod.GET)
	public ModelAndView addStory() {
		
		Story newStory = new Story();
		
		//ModelAndView mav = new ModelAndView("chooseMedia");
		ModelAndView mav = new ModelAndView("story");
		mav.addObject("newStory", newStory);
		
		return mav;
	}
	
	@RequestMapping(value = "/story/list", method = RequestMethod.POST)
	@ResponseBody
	public String getAllStories() {
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
	
	@RequestMapping(value = "/story/form/chooseMediaForm", method = RequestMethod.GET)
	public ModelAndView chooseMediaForm(@ModelAttribute("newStory") Story newStory,
										@RequestParam(value = "removePageNo", defaultValue = "") String removePageNo)
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
	
	@RequestMapping(value = "/story/form/getChooseMediaForm", method = RequestMethod.GET)
	public ModelAndView getChooseMediaForm(@ModelAttribute("newStory") Story newStory)
	{
		ModelAndView mav = new ModelAndView("form/chooseMediaForm");
		return mav;
	}
	
	@RequestMapping(value = "/story/edit/form/chooseMediaForm", method = RequestMethod.GET)
	public ModelAndView chooseMediaForm_Edit(@ModelAttribute("story") Story story,
											 @RequestParam(value = "removePageNo", defaultValue = "") String removePageNo)
	{
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
	public ModelAndView getChooseMediaForm_Edit(@ModelAttribute("story") Story story)
	{
		ModelAndView mav = new ModelAndView("form/chooseMediaForm");
		return mav;
	}
	
	@RequestMapping(value = "/story/form/uploadVideoForm", method = RequestMethod.GET)
	public ModelAndView uploadVideoForm(@ModelAttribute("newStory") Story newStory)
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
			//ModelAndView mav = new ModelAndView("form/uploadVideoForm");
			ModelAndView mav = new ModelAndView("story");
			mav.addObject("formUrl", "getUploadVideoForm");
			return mav;
		}
	}
	
	@RequestMapping(value = "/story/form/getUploadVideoForm", method = RequestMethod.GET)
	public ModelAndView getUploadVideoForm()
	{
		ModelAndView mav = new ModelAndView("form/uploadVideoForm");
		return mav;
	}
	
	@RequestMapping(value = "/story/addVideo", method = RequestMethod.POST)
	public ModelAndView addVideoStoryPage(@ModelAttribute("newStory") Story newStory,
									 	  @RequestParam("file") MultipartFile file,
									 	  @RequestParam("description") String description)
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
			
			return mav;
		}
	}
	
	@RequestMapping(value = "/story/changeVideo", method = RequestMethod.POST)
	public ModelAndView changeVideoStoryPage(@ModelAttribute("newStory") Story newStory,
									 	  	 @RequestParam("videoFile") MultipartFile file,
									 	  	 @RequestParam("storyPageListIndex") int storyPageListIndex)
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
			// check if uploaded file is of 'video' type
			String contentType = file.getContentType();
			if(!contentType.contains("video"))
			{
				// send error message
				ModelAndView mav = new ModelAndView("story");
				mav.addObject("formUrl", "form/getStoryReviewForm");
				String error = message.getMessage("error.invalidvideo", null, null);
				mav.addObject("error", error);
				mav.addObject("viewPageNo", new String(storyPageListIndex+1+""));
				return mav;
			}
			
			// change video storypage to newStory
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
			mav.addObject("viewPageNo", new String(storyPageListIndex+1+""));
			
			return mav;
		}
	}
	
	@RequestMapping(value = "/story/edit/changeVideo", method = RequestMethod.POST)
	public ModelAndView changeVideoStoryPage_Edit(@ModelAttribute("story") Story story,
									 	  	 	  @RequestParam("videoFile") MultipartFile file,
									 	  	 	  @RequestParam("storyPageListIndex") int storyPageListIndex)
	{
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
				mav.addObject("formUrl", "form/getEditStoryForm");
				mav.addObject("error", message.getMessage("error.invalidvideo", null, null));
				mav.addObject("viewPageNo", new String(storyPageListIndex+1+""));
				return mav;
			}
			
			// change video storypage to newStory
			List<StoryPage> storyPages = story.getStoryPages();
			if(storyPages!=null && storyPages.size()>=storyPageListIndex)
			{
				StoryContent storyContent = storyPages.get(storyPageListIndex).getStoryContent();
				
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
				
				story.setStoryPages(storyPages);
			}
			
			// ModelAndView mav = new ModelAndView("storyreview");
			
			ModelAndView mav = new ModelAndView("redirect:/story/form/editStoryForm.html");
			mav.addObject("story", story);
			mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
			mav.addObject("viewPageNo", new String(storyPageListIndex+1+""));
			
			return mav;
		}
	}
	
	@RequestMapping(value = "/story/changeAudio", method = RequestMethod.POST)
	public ModelAndView changeAudioStoryPage(@ModelAttribute("newStory") Story newStory,
									 	  	 @RequestParam("audioFile") MultipartFile file,
									 	  	 @RequestParam("storyPageListIndex") int storyPageListIndex)
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
			mav.addObject("viewPageNo", new String(storyPageListIndex+1+""));
			
			return mav;
		}
	}
	
	@RequestMapping(value = "/story/edit/changeAudio", method = RequestMethod.POST)
	public ModelAndView changeAudioStoryPage_Edit(@ModelAttribute("story") Story story,
									 	  	 	  @RequestParam("audioFile") MultipartFile file,
									 	  	 	  @RequestParam("storyPageListIndex") int storyPageListIndex)
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
				mav.addObject("formUrl", "form/getEditStoryForm");
				mav.addObject("error", message.getMessage("error.invalidaudio", null, null));
				mav.addObject("viewPageNo", new String(storyPageListIndex+1+""));
				return mav;
			}
			
			// change video storypage to newStory
			List<StoryPage> storyPages = story.getStoryPages();
			if(storyPages!=null && storyPages.size()>=storyPageListIndex)
			{
				StoryContent storyContent = storyPages.get(storyPageListIndex).getStoryContent();
				
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
					storyPages.get(storyPageListIndex).setStoryContent(storyContent);
				}
				
				story.setStoryPages(storyPages);
			}
			
			// ModelAndView mav = new ModelAndView("storyreview");
			
			ModelAndView mav = new ModelAndView("redirect:/story/form/editStoryForm.html");
			mav.addObject("story", story);
			mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
			mav.addObject("viewPageNo", new String(storyPageListIndex+1+""));
			
			return mav;
		}
	}
	
	@RequestMapping(value = "/story/changeImage", method = RequestMethod.POST)
	public ModelAndView changeImageStoryPage(@ModelAttribute("newStory") Story newStory,
									 	  	 @RequestParam("imageFile") MultipartFile file,
									 	  	 @RequestParam("storyPageListIndex") int storyPageListIndex)
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
			mav.addObject("viewPageNo", new String(storyPageListIndex+1+""));
			
			return mav;
		}
	}
	
	@RequestMapping(value = "/story/edit/changeImage", method = RequestMethod.POST)
	public ModelAndView changeImageStoryPage_Edit(@ModelAttribute("story") Story story,
									 	  	 	  @RequestParam("imageFile") MultipartFile file,
									 	  	 	  @RequestParam("storyPageListIndex") int storyPageListIndex)
	{
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
				mav.addObject("formUrl", "form/getEditStoryForm");
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
			mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
			mav.addObject("viewPageNo", new String(storyPageListIndex+1+""));
			
			return mav;
		}
	}
	
	@RequestMapping(value = "/story/edit/addVideo", method = RequestMethod.POST)
	public ModelAndView addVideoStoryPage_Edit(@ModelAttribute("story") Story story,
									 	  	  @RequestParam("file") MultipartFile file,
									 	  	  @RequestParam("description") String description)
	{
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
			mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
			mav.addObject("viewPageNo", new String(story.getStoryPages().size()+""));
			mav.addObject("story", story);
			
			return mav;
		}
	}
	
	@RequestMapping(value = "/story/uploadImage", method = RequestMethod.GET)
	public ModelAndView uploadImage(@ModelAttribute("newStory") Story newStory)
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
			ModelAndView mav = new ModelAndView("uploadImage");
			return mav;
		}
	}
	
	@RequestMapping(value = "/story/addImage", method = RequestMethod.POST)
	public ModelAndView addImageStoryPage(@ModelAttribute("newStory") Story newStory,
										  @RequestParam("file") MultipartFile file,
										  @RequestParam("description") String description) throws IOException
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
	
	@RequestMapping(value = "/story/edit/addImage", method = RequestMethod.POST)
	public ModelAndView addImageStoryPage_Edit(@ModelAttribute("story") Story story,
										  	   @RequestParam("file") MultipartFile file,
										  	   @RequestParam("description") String description) throws IOException
	{
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
			mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
			mav.addObject("viewPageNo", new String(story.getStoryPages().size()+""));
			mav.addObject("story", story);
			
			return mav;
		}
	}
	
	@RequestMapping(value = "/story/uploadAudio", method = RequestMethod.GET)
	public ModelAndView uploadAudio(@ModelAttribute("newStory") Story newStory)
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
			ModelAndView mav = new ModelAndView("uploadAudio");
			return mav;
		}
	}

	@RequestMapping(value = "/story/addAudio", method = RequestMethod.POST)
	public ModelAndView addAudioStoryPage(@ModelAttribute("newStory") Story newStory,
										  @RequestParam("file") MultipartFile file,
										  @RequestParam("description") String description) throws IOException
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
			
			//ModelAndView mav = new ModelAndView("storyreview");
			ModelAndView mav = new ModelAndView("redirect:/story/form/storyReviewForm.html");
			mav.addObject("newStory", newStory);
			
			return mav;
		}
	}
	
	@RequestMapping(value = "/story/addNarration", method = RequestMethod.POST)
	public ModelAndView addNarration(@ModelAttribute("newStory") Story newStory,
									 @RequestParam("narrationFile") MultipartFile file,
									 @RequestParam("storyPageListIndex") int storyPageListIndex) throws IOException
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
			mav.addObject("viewPageNo", new String(storyPageListIndex+1+""));
			
			return mav;
		}
	}
	
	@RequestMapping(value = "/story/edit/addNarration", method = RequestMethod.POST)
	public ModelAndView addNarration_Edit(@ModelAttribute("story") Story story,
									 	  @RequestParam("narrationFile") MultipartFile file,
									 	  @RequestParam("storyPageListIndex") int storyPageListIndex) throws IOException
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
			mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
			mav.addObject("viewPageNo", new String(storyPageListIndex+1+""));
			
			return mav;
		}
	}
	
	@RequestMapping(value = "/story/edit/addAudio", method = RequestMethod.POST)
	public ModelAndView addAudioStoryPage_Edit(@ModelAttribute("story") Story story,
										  	   @RequestParam("file") MultipartFile file,
										  	   @RequestParam("description") String description) throws IOException
	{
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
			
			//ModelAndView mav = new ModelAndView("storyreview");
			ModelAndView mav = new ModelAndView("redirect:/story/form/editStoryForm.html");
			mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
			mav.addObject("viewPageNo", new String(story.getStoryPages().size()+""));
			mav.addObject("story", story);
			
			return mav;
		}
	}
	
	@RequestMapping(value = "/story/uploadText", method = RequestMethod.GET)
	public ModelAndView uploadText(@ModelAttribute("newStory") Story newStory)
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
	
	@RequestMapping(value = "/story/edit/addText", method = RequestMethod.POST)
	public ModelAndView addTextStoryPage_Edit(@ModelAttribute("story") Story story,
			  							 	  @RequestParam("text") String text)
	{
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
			mav.addObject("storyMode", EpooqConstant.STORY_MODE_EDIT);
			mav.addObject("viewPageNo", new String(story.getStoryPages().size()+""));
			mav.addObject("story", story);
			
			return mav;
		}
	}
	
	@RequestMapping(value = "/story/cancelStoryPage", method = RequestMethod.GET)
	public String cancelStoryPage(@ModelAttribute("newStory") Story newStory,
								  @RequestParam("pageNo") int pageNo,
								  final RedirectAttributes redirectAttributes)
	{
		if(newStory==null)
		{
			// redirect to add.html
			return "redirect:add.html";
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
			redirectAttributes.addFlashAttribute("newStory",newStory);
			if(storyPages.size()<pageNo)
			{
				redirectAttributes.addFlashAttribute("viewPageNo", new String(""+(pageNo-1)));			
			}
			else
			{
				redirectAttributes.addFlashAttribute("viewPageNo", new String(""+pageNo));
			}
			
			return "redirect:/story/form/storyReviewForm.html";			
		}
		else
		{
			return "redirect:/story/form/chooseMediaForm.html";
		}
	}
	
	@RequestMapping(value = "/story/edit/cancelStoryPage", method = RequestMethod.GET)
	public String cancelStoryPage_Edit(@ModelAttribute("story") Story story,
								  	   @RequestParam("pageNo") int pageNo,
								  	   final RedirectAttributes redirectAttributes,
								  	   SessionStatus status)
	{
		if(story==null)
		{
			// redirect to add.html
			return "redirect:add.html";
		}
		
		List<StoryPage> storyPages = story.getStoryPages();
		if(storyPages!=null && storyPages.size()>=pageNo)
		{
			StoryContent storyContent = storyPages.get(pageNo-1).getStoryContent();
			if(storyContent.getType()!="TEXT")
			{
				String storyContentPath = storyContent.getContent();
				
				//boolean contentDeleted = storyService.removeStoryContent(storyContentPath);				
			}
			
			storyPages.remove(pageNo-1);
			story.setStoryPages(storyPages);
			
		}
		
		if(storyPages.size()!=0)
		{
			redirectAttributes.addFlashAttribute("story",story);
			redirectAttributes.addFlashAttribute("storyMode", EpooqConstant.STORY_MODE_EDIT);
			if(storyPages.size()<pageNo)
			{
				redirectAttributes.addFlashAttribute("viewPageNo", new String(""+(pageNo-1)));			
			}
			else
			{
				redirectAttributes.addFlashAttribute("viewPageNo", new String(""+pageNo));
			}
			
			return "redirect:/story/form/editStoryForm.html";			
		}
		else
		{
			redirectAttributes.addFlashAttribute("story",story);
			redirectAttributes.addFlashAttribute("storyMode", EpooqConstant.STORY_MODE_EDIT);
			
			return "redirect:/story/edit/form/chooseMediaForm.html";
		}
	}
	
	@RequestMapping(value = "/story/form/storyReviewForm", method = RequestMethod.GET)
	public ModelAndView storyReviewForm(@ModelAttribute(value="newStory") Story newStory,
										@ModelAttribute("viewPageNo") String viewPageNo)
	{
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
	
	@RequestMapping(value = "/story/acceptStory", method = RequestMethod.POST)
	public ModelAndView acceptStoryForm(@ModelAttribute("newStory") Story newStory,
										@RequestParam(value = "removePageNo", defaultValue = "") String removePageNo)
	{
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
	
	@RequestMapping(value = "/story/edit/acceptStory", method = RequestMethod.POST)
	public ModelAndView acceptStoryForm_Edit(@ModelAttribute("story") Story story,
											 @RequestParam(value = "removePageNo", defaultValue = "") String removePageNo)
	{
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
	public ModelAndView getAcceptStoryForm_Edit(@ModelAttribute("story") Story story)
	{
		ModelAndView mav = new ModelAndView("form/acceptEditedStoryForm");
		return mav;
	}
	
	@RequestMapping(value = "/story/cancelStory", method = RequestMethod.GET)
	public String cancelStory(@ModelAttribute("newStory") Story newStory,
							  SessionStatus status)
	{
		status.setComplete();
		
		return "redirect:/home.html";
	}
	
	@RequestMapping(value = "/story/edit/cancelStory", method = RequestMethod.GET)
	public String cancelStory_Edit(@ModelAttribute("story") Story story,
							  	   SessionStatus status)
	{
		status.setComplete();
		
		return "redirect:/home.html";
	}
	
	@RequestMapping(value = "/story/saveStory", method = RequestMethod.POST)
	public String saveStory(@ModelAttribute("newStory") Story newStory,
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
	
	@RequestMapping(value = "/story/updateStory", method = RequestMethod.POST)
	public String updateStory(@ModelAttribute("story") Story story,
							  @RequestParam("storyDay") int storyDay,
							  @RequestParam("storyMonth") int storyMonth,
							  @RequestParam("storyYear") int storyYear,
							  SessionStatus status,
							  HttpServletRequest httpRequest,
							  Model model)
	{
		
		if(story==null)
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
	
	@RequestMapping(value = "/story/editStory", method = RequestMethod.POST)
	public ModelAndView editStory(@RequestParam("storyId") int storyId,
								  HttpServletRequest httpRequest)
	{
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
	
	@RequestMapping(value = "/story/removeStory", method = RequestMethod.POST)
	public ModelAndView removeStory(@RequestParam("storyId") int storyId,
									HttpServletRequest httpRequest)
	{
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
