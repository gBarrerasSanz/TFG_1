package guiatv.catalog.webapp;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Charsets;

@RestController
@RequestMapping("/catalogView")
public class CatalogViewController {
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@RequestMapping(value = "/schedulesCatalog", method = RequestMethod.GET)
	public ResponseEntity<String> getProgrammesCatalog(
		@RequestParam(value="hashNameProg", defaultValue="", required=true) String hashNameProg)
	{
		Map<String, Object> model = null;
		String html_text = null;
		
		/*************************************
		 * Inyectar info en el template
		 *************************************/
		
		model = new HashMap<String, Object>();
		model.put("hashNameProg", hashNameProg);
		html_text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				"schedules_catalog_tpt.html", "UTF-8", model);
		html_text = html_text.replace("\n", "").replace("\t", "").replace("\r", "");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_HTML);
		ResponseEntity<String> re = new ResponseEntity<>(html_text, headers, HttpStatus.OK); 
		return re;
	}
	
}