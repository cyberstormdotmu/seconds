package fi.korri.epooq.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import fi.korri.epooq.controller.AbstractBaseController;

@Controller
@RequestMapping(value = "/admin/deleteCommunity")
public class AdminDeleteCommunityController extends AbstractBaseController {

	@RequestMapping(method = RequestMethod.POST)
	public String postAdminCommunityPage(WebRequest request) {

		// Delete community
		String idString = request.getParameter("community_id");

		if (idString != null && !"".equals(idString)) {
			long id = Long.parseLong(idString);

			deleteCommunityById(id);
		}

		return "redirect:index.html";
	}
}
