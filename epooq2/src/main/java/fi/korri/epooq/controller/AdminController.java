package fi.korri.epooq.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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

@Controller
//@RequestMapping("/admin")
public class AdminController 
{
	@Autowired
	private ReloadableResourceBundleMessageSource message;
	
	@Autowired
	private AdminService adminService;
	
	@RequestMapping(value = "/admin/uploadMapImage", method = RequestMethod.GET)
	public ModelAndView uploadMapImageForm(HttpServletRequest request)
	{
		return new ModelAndView("/admin/uploadMapImage","mapImage", new MapImage()); 
	}
	
	@RequestMapping(value = "/admin/uploadMapImage", method = RequestMethod.POST)
	public ModelAndView insertMapImage(	@ModelAttribute("mapImage") MapImage mapImage) throws IOException
	{
		adminService.insertMapImage(mapImage);
		
		//List<MapImage> mapImageList = adminService.getMapImageList(); 
		
		ModelAndView mav = new ModelAndView("redirect:/admin/listMapImage.html");
		//mav.addObject("mapImageList", mapImageList);
		
		return mav;
	}
	
	@RequestMapping(value = "/admin/editMapImage", method = RequestMethod.GET)
	public ModelAndView editMapImageForm(HttpServletRequest request)
	{
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
	public ModelAndView updateMapImage(	@ModelAttribute("mapImage") MapImage mapImage) throws IOException
	{
		adminService.updateMapImage(mapImage);
		
		//List<MapImage> mapImageList = adminService.getMapImageList(); 
		return new ModelAndView("redirect:/admin/listMapImage.html");
	}
	
	@RequestMapping(value = "/admin/deleteMapImage", method = RequestMethod.GET)
	public ModelAndView updateMapImage(@RequestParam("id")	long id) throws IOException
	{
		adminService.deleteMapImage(id);
		
		//List<MapImage> mapImageList = adminService.getMapImageList(); 
		return new ModelAndView("redirect:/admin/listMapImage.html");//
	}
	
	@RequestMapping(value = "/admin/listMapImage", method = RequestMethod.GET )
	public ModelAndView listMapImage() throws Exception
	{
		List<MapImage> list = adminService.getMapImageList(1);
		double totalRows = adminService.getMapImageCount();
		int totalPages = (int)Math.ceil(totalRows/10);
		ModelAndView mav = new ModelAndView("/admin/listMapImage");
		mav.addObject("mapImageList", list);
		mav.addObject("currentPage", 1);
		mav.addObject("totalRows", totalRows);
		mav.addObject("totalPages", totalPages);
		
		return mav;
	}
	
	@RequestMapping(value = "/admin/listPageMapImage", method = RequestMethod.GET )
	public ModelAndView getCurrentPageImageList(@RequestParam("page") int page)
	{
		List<MapImage> list = adminService.getMapImageList(page);
		double totalRows = adminService.getMapImageCount();
		int totalPages = (int)Math.ceil(totalRows/10);
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
	
	@RequestMapping(value = "/admin/addImage", method = RequestMethod.POST)
	public ModelAndView uploadImage(@RequestParam("hstartLat") double hstartLat,
										  @RequestParam("hstartLong") double hstartLong,
										  @RequestParam("hendLat") double hendLat,
										  @RequestParam("hendLong") double hendLong,
										  @RequestParam("level") int level,
										  @RequestParam("title") String title,
										  @RequestParam("imageUpload") MultipartFile file) throws IOException
	{
		MapImage mapImage = new MapImage();
		
		// check if uploaded file is of 'image' type
		String contentType = file.getContentType();
		if(!contentType.contains("image"))
		{
			// send error message
			ModelAndView mav = new ModelAndView("story");
			mav.addObject("formUrl", "admin/uploadMapImage");
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
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		ModelAndView mav = new ModelAndView("/admin/uploadMapImage", "mapImage", mapImage);
		//ModelAndView mav = new ModelAndView("redirect:/admin/uploadMapImage.html");
		//mav.addObject("mapImage", mapImage);
		
		return mav;
	}
	
	@RequestMapping(value = "/admin/removeImage", method = RequestMethod.POST)
	public ModelAndView removeUploadedImage( @ModelAttribute("mapImage") MapImage mapImage ) throws IOException
	{
		// remove uploaded map image
		try
		{
			String tempMapImage = mapImage.getFile();
			
			if(StringUtils.isNotEmpty(tempMapImage))
			{
				// remove image from temp folder
				boolean deleted = adminService.removeUploadedMapImage(tempMapImage);
				if(deleted)
				{
					mapImage.setFile(null);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		ModelAndView mav = new ModelAndView("/admin/uploadMapImage", "mapImage", mapImage);
		//ModelAndView mav = new ModelAndView("redirect:/admin/uploadMapImage.html");
		//mav.addObject("mapImage", mapImage);
		
		return mav;
	}
	
	@RequestMapping(value = "/admin/edit/removeImage", method = RequestMethod.POST)
	public ModelAndView removeUploadedImage_Edit( @ModelAttribute("mapImage") MapImage mapImage ) throws IOException
	{
		// remove uploaded map image
		try
		{
			mapImage.setFile(null);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		ModelAndView mav = new ModelAndView("/admin/editMapImage", "mapImage", mapImage);
		
		return mav;
	}
	
	@RequestMapping(value = "/admin/editImage", method = RequestMethod.POST)
	public ModelAndView editImage(@RequestParam("hstartLat") double hstartLat,
										  @RequestParam("hstartLong") double hstartLong,
										  @RequestParam("hendLat") double hendLat,
										  @RequestParam("hendLong") double hendLong,
										  @RequestParam("level") int level,
										  @RequestParam("title") String title,
										  @RequestParam("id") long id,
										  @RequestParam("enableFlag") boolean enableFlag,
										  @RequestParam("imageUpload") MultipartFile file) throws IOException
	{
		MapImage mapImage = new MapImage();
		
		// check if uploaded file is of 'image' type
		String contentType = file.getContentType();
		if(!contentType.contains("image"))
		{
			// send error message
			ModelAndView mav = new ModelAndView("story");
			mav.addObject("formUrl", "admin/uploadMapImage");
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
		//ModelAndView mav = new ModelAndView("redirect:/admin/uploadMapImage.html");
		//mav.addObject("mapImage", mapImage);
		
		return mav;
	}
}
