package fi.korri.epooq.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import fi.korri.epooq.Key;
import fi.korri.epooq.controller.AbstractBaseController;

@Controller
@RequestMapping(value = "/admin/addKey")
public class AdminAddKeyController extends AbstractBaseController {

	@RequestMapping(method = RequestMethod.POST)
	public String postAdminCommunityPage(
			@RequestParam("file") MultipartFile file, WebRequest request) {

		// Add new community

		Long pictureId = writePicture(file);
		String question = request.getParameter("question");
		String ageString = request.getParameter("age");
		int age = Integer.parseInt(ageString); 
		String communityIdString = request.getParameter("community_id");
		Long communityId = null;
		if(!"-1".equals(communityIdString)) {
			communityId = Long.parseLong(communityIdString);	
		}

		Key key = new Key();
		key.setQuestion(question);
		key.setAge(age);
		key.setPictureId(pictureId);
		key.setCommunityId(communityId);

		writeKey(key);

		return "redirect:index.html";
	}
}
