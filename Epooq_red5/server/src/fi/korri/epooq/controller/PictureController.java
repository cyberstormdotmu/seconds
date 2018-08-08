package fi.korri.epooq.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/picture")
public class PictureController extends AbstractBaseController {

	@RequestMapping(method = RequestMethod.GET)
	public void getFile(@RequestParam("id") Long pictureId,
			HttpServletResponse response) {

		
		try {
			//response.setContentType(rs.getString("mime_type"));
			InputStream is = getPicture(pictureId);

			if(is != null) {
				IOUtils.copy(is, response.getOutputStream());
			}
			response.flushBuffer();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}

	}
}
