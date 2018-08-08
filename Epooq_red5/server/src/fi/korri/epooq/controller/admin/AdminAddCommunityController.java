package fi.korri.epooq.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import fi.korri.epooq.Community;
import fi.korri.epooq.controller.AbstractBaseController;

@Controller
@RequestMapping(value = "/admin/addCommunity")
public class AdminAddCommunityController extends AbstractBaseController {

	@RequestMapping(method = RequestMethod.POST)
	public String postAdminCommunityPage(
			@RequestParam("file") MultipartFile file, WebRequest request) {

		// Add new community

		Long pictureId = writePicture(file);
		String name = request.getParameter("name");

		Community community = new Community();
		community.setName(name);
		if(pictureId != null) {
			community.setPictureId(pictureId);
		}

		writeCommunity(community);

		return "redirect:index.html";
	}
}
