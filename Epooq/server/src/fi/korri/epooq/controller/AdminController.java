package fi.korri.epooq.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import fi.korri.epooq.model.MapImage;
import fi.korri.epooq.model.Story;
import fi.korri.epooq.service.AdminService;
import fi.korri.epooq.service.StoryService;

@Controller
public class AdminController 
{	
	private final Logger log = Logger.getLogger(AdminController.class.getName());

	@Autowired
	private ReloadableResourceBundleMessageSource message;

	@Autowired
	private AdminService adminService;

	// Methods for UPLOAD Image on Map
	@RequestMapping(value = "/admin/uploadMapImage", method = RequestMethod.GET)
	public ModelAndView uploadMapImageForm(@ModelAttribute("mapImage") MapImage mapImage)
	{	
		log.debug("Upload map image");
		return new ModelAndView("/admin/uploadMapImage","mapImage", new MapImage()); 
	}

	@RequestMapping(value = "/admin/uploadMapImage", method = RequestMethod.POST)
	public ModelAndView insertMapImage(	@ModelAttribute("mapImage") MapImage mapImage) throws IOException
	{	
		String regex = "-?[0-9]+.[0-9]+";
		String regex1 = "-?[0-9]+";

		if((mapImage.getStartLat().matches(regex) || mapImage.getStartLat().matches(regex1)) &&
				(mapImage.getStartLong().matches(regex)|| mapImage.getStartLong().matches(regex1)) &&
				(mapImage.getEndLat().matches(regex) || mapImage.getEndLat().matches(regex1)) &&
				(mapImage.getEndLong().matches(regex) || mapImage.getEndLong().matches(regex1))){
			adminService.insertMapImage(mapImage);
			log.info("Map image uploaded successfully");
			ModelAndView mav = new ModelAndView("redirect:/admin/listMapImage.html");

			return mav;

		}else{

			ModelAndView mav = new ModelAndView("/admin/uploadMapImage");
			mav.addObject("error", message.getMessage("adminEroor.imageUploadFail", null, null));
			mav.addObject("title",mapImage.getTitle());
			mav.addObject("level",mapImage.getLevel());
			mapImage.setStartLat("67");
			mapImage.setStartLong("29");
			mapImage.setEndLat("63");
			mapImage.setEndLong("25");		
			mapImage.setFile(null);
			log.info("Map image Not uploaded successfully");
			return mav;


		}
	}

	// Save map image
	@RequestMapping(value = "/admin/addImage", method = RequestMethod.POST)
	public ModelAndView uploadImage(@RequestParam(value="hstartLat") String hstartLat,
			@RequestParam(value="hstartLong") String hstartLong,
			@RequestParam(value="hendLat") String hendLat,
			@RequestParam(value="hendLong") String hendLong,
			@RequestParam(value="level") int level,
			@RequestParam(value="title") String title,
			@RequestParam(value="imageUpload") MultipartFile file) throws IOException
			{	
				MapImage mapImage = new MapImage();


				String regex = "-?[0-9]+.[0-9]+";
				String regex1 = "-?[0-9]+";

		if((hstartLat.matches(regex) || hstartLat.matches(regex1)) &&
				(hstartLong.matches(regex)|| hstartLong.matches(regex1)) &&
				(hendLong.matches(regex) || hendLong.matches(regex1)) &&
				(hendLat.matches(regex) || hendLat.matches(regex1))){

			mapImage.setLevel(level);
			mapImage.setTitle(title);
			mapImage.setStartLat(hstartLat);
			mapImage.setEndLat(hendLat);
			mapImage.setStartLong(hstartLong);
			mapImage.setEndLong(hendLong);
			// check if uploaded file is of 'image' type
			String contentType = file.getContentType();

			if(!contentType.contains("image"))
			{	
				// send error message
				ModelAndView mav = new ModelAndView("/admin/uploadMapImage");
				mav.addObject("mapImage",mapImage);
				mav.addObject("error", message.getMessage("error.invalidimage", null, null));
				mav.addObject("title",title);
				mav.addObject("level",level);
				log.info("Save map image failed");
				return mav;
			}

			// add image storypage to newStory
			try
			{
				mapImage = adminService.addMediaStoryPage(mapImage, file);
				mapImage.setStartLat(hstartLat);
				mapImage.setStartLong(hstartLong);
				mapImage.setEndLat(hendLat);
				mapImage.setEndLong(hendLong);
				mapImage.setLevel(level);
				mapImage.setTitle(title);
				log.info("Map image saved successfully");
			}
			catch(Exception e)
			{
				log.error("Failed to save map image");
				e.printStackTrace();
			}

			ModelAndView mav = new ModelAndView("/admin/uploadMapImage", "mapImage", mapImage);

			return mav;
		}else{
			ModelAndView mav = new ModelAndView("/admin/uploadMapImage");
			mav.addObject("mapImage",mapImage);
			mav.addObject("error", message.getMessage("adminEroor.imageUploadFail", null, null));
			mav.addObject("title",title);
			mav.addObject("level",level);
			log.info("Save map image failed");
			return mav;
		}

			}

	// Methods for EDIT Image on Map
	@RequestMapping(value = "/admin/editMapImage", method = RequestMethod.GET)
	public ModelAndView editMapImageForm(HttpServletRequest request)
	{
		// Get map image by id
		log.debug("Edit map image");
		long id = 0;
		MapImage mapImage = new MapImage();
		String imageId = request.getParameter("id");
		if(StringUtils.isNotEmpty(imageId))
		{
			id = Long.parseLong(request.getParameter("id"));
			mapImage = adminService.selectMapImage(id);
		}
		return new ModelAndView("/admin/editMapImage","mapImage", mapImage); 
	}


	@RequestMapping(value = "/admin/editMapImage", method = RequestMethod.POST)
	public ModelAndView updateMapImage(@ModelAttribute("mapImage") MapImage mapImage) throws IOException
	{	
		String regex = "-?[0-9]+.[0-9]+";
		String regex1 = "-?[0-9]+";
		
		if((mapImage.getStartLat().matches(regex) || mapImage.getStartLat().matches(regex1)) &&
				(mapImage.getStartLong().matches(regex)|| mapImage.getStartLong().matches(regex1)) &&
				(mapImage.getEndLat().matches(regex) || mapImage.getEndLat().matches(regex1)) &&
				(mapImage.getEndLong().matches(regex) || mapImage.getEndLong().matches(regex1))){
			adminService.updateMapImage(mapImage);
			log.info("Map image updated successfully");
			ModelAndView mav = new ModelAndView("redirect:/admin/listMapImage.html");

			return mav;

		}else{

			ModelAndView mav = new ModelAndView("/admin/editMapImage");
			mav.addObject("error", message.getMessage("adminEroor.imageUploadFail", null, null));
			mav.addObject("title",mapImage.getTitle());
			mav.addObject("level",mapImage.getLevel());
			mapImage.setStartLat("67");
			mapImage.setStartLong("29");
			mapImage.setEndLat("63");
			mapImage.setEndLong("25");		
			mapImage.setFile(null);
			log.info("Map image Not uploaded successfully");
			return mav;


		}
		
	}

	@RequestMapping(value = "/admin/editImage", method = RequestMethod.POST)
	public ModelAndView editImage(@RequestParam(value="hstartLat") String hstartLat,
			@RequestParam(value="hstartLong") String hstartLong,
			@RequestParam(value="hendLat") String hendLat,
			@RequestParam(value="hendLong") String hendLong,
			@RequestParam(value="level") int level,
			@RequestParam(value="title") String title,
			@RequestParam(value="id") long id,
			@RequestParam(value="enableFlag") boolean enableFlag,
			@RequestParam(value="imageUpload") MultipartFile file) throws IOException
			{	
	
	
		MapImage mapImage = new MapImage();
		mapImage.setId(id);
		mapImage.setLevel(level);
		mapImage.setTitle(title);
		mapImage.setStartLat(hstartLat);
		mapImage.setEndLat(hendLat);
		mapImage.setStartLong(hstartLong);
		mapImage.setEndLong(hendLong);
		mapImage.setEnableFlag(enableFlag);

		// check if uploaded file is of 'image' type
		String contentType = file.getContentType();
		if(!contentType.contains("image"))
		{
			// send error message
			
			ModelAndView mav = new ModelAndView("/admin/editMapImage","mapImage", mapImage);
			mav.addObject("error", message.getMessage("error.invalidimage", null, null));
			return mav;
		}

		// add image storypage to newStory
		try
		{
			mapImage = adminService.addMediaStoryPage(mapImage, file);
			mapImage.setStartLat(hstartLat);
			mapImage.setStartLong(hstartLong);
			mapImage.setEndLat(hendLat);
			mapImage.setEndLong(hendLong);
			mapImage.setLevel(level);
			mapImage.setTitle(title);
			mapImage.setId(id);
			mapImage.setEnableFlag(enableFlag);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		ModelAndView mav = new ModelAndView("/admin/editMapImage", "mapImage", mapImage);

		return mav;
		} 
	

	// Method for DELETE Image from Map
	@RequestMapping(value = "/admin/deleteMapImage", method = RequestMethod.GET)
	public ModelAndView updateMapImage(@RequestParam("id")	long id) throws IOException
	{	log.debug("Delete map Image: "+id);
	adminService.deleteMapImage(id);
	return new ModelAndView("redirect:/admin/listMapImage.html");
	}

	// Methods for display all image list  
	@RequestMapping(value = "/admin/listMapImage", method = RequestMethod.GET )
	public ModelAndView listMapImage() throws Exception
	{	log.debug("list of map images");
	// pagination to display list of images in 6 row per list
	List<MapImage> list = adminService.getMapImageList(1);
	double totalRows = adminService.getMapImageCount();
	int totalPages = (int)Math.ceil(totalRows/6);
	ModelAndView mav = new ModelAndView("/admin/listMapImage");
	mav.addObject("mapImageList", list);
	mav.addObject("currentPage", 1);
	mav.addObject("totalRows", totalRows);
	mav.addObject("totalPages", totalPages);

	return mav;
	}

	@RequestMapping(value = "/admin/listPageMapImage", method = RequestMethod.GET )
	public ModelAndView getCurrentPageImageList(@RequestParam("page") int page)
	{	log.info("Current Page of map image list is: "+page);
	// pagination to display list of images in 6 row per list
	List<MapImage> list = adminService.getMapImageList(page);
	double totalRows = adminService.getMapImageCount();
	int totalPages = (int)Math.ceil(totalRows/6);
	ModelAndView mav = new ModelAndView("/admin/listMapImage");
	mav.addObject("mapImageList", list);
	mav.addObject("currentPage", page);
	mav.addObject("totalRows", totalRows);
	mav.addObject("totalPages", totalPages);

	return mav;
	}


	@RequestMapping(value = "/admin/listImages", method = RequestMethod.GET )
	@ResponseBody
	public String listsImages() throws Exception
	{
		String json="";
		List<MapImage> list = adminService.getMapImageList();
		if(list!=null && list.size()>0){
			ObjectMapper mapper = new ObjectMapper();
			try {
				json=mapper.writeValueAsString(list);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return json;
	}


	@RequestMapping(value = "/admin/removeImage", method = RequestMethod.POST)
	public ModelAndView removeUploadedImage( @ModelAttribute("mapImage") MapImage mapImage ) throws IOException
	{	log.debug("Remove map image");
	// remove uploaded map image
	try
	{
		String tempMapImage = mapImage.getFile();

		if(StringUtils.isNotEmpty(tempMapImage))
		{
			// remove image from temp folder
			boolean deleted = adminService.removeUploadedMapImage(tempMapImage);
			log.info("Map image removed successfully");
			if(deleted)
			{	
				mapImage.setFile(null);
			}
		}
	}
	catch(Exception e)
	{	
		log.info("Failed to remove map image");
		e.printStackTrace();
	}

	ModelAndView mav = new ModelAndView("/admin/uploadMapImage", "mapImage", mapImage);

	return mav;
	}

	@RequestMapping(value = "/admin/edit/removeImage", method = RequestMethod.POST)
	public ModelAndView removeUploadedImageEdit(@ModelAttribute("mapImage") MapImage mapImage ) throws IOException
	{	
		log.debug("Remove uploaded map image");
		// remove uploaded map image
		try
		{	log.info("Remove uploaded map image successfully");
		mapImage.setFile(null);
		}
		catch(Exception e)
		{	log.debug("Failed to remove uploaded map image");
		e.printStackTrace();
		}

		ModelAndView mav = new ModelAndView("/admin/editMapImage", "mapImage", mapImage);

		return mav;
	}


}
