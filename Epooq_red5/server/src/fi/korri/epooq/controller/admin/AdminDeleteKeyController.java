package fi.korri.epooq.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import fi.korri.epooq.controller.AbstractBaseController;

@Controller
@RequestMapping(value = "/admin/deleteKey")
public class AdminDeleteKeyController extends AbstractBaseController {

	@RequestMapping(method = RequestMethod.POST)
	public String postAdminCommunityPage(WebRequest request) {

		// Delete key
		String idString = request.getParameter("key_id");

		if (idString != null && !"".equals(idString)) {
			long id = Long.parseLong(idString);

			deleteKeyById(id);
		}

		return "redirect:index.html";
	}
}
