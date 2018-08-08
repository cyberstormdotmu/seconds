package fi.korri.epooq.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/index")
public class IndexController extends AbstractBaseController {

	@RequestMapping(method = RequestMethod.GET)
	public void showClouds(HttpServletRequest request) {
	}
}
