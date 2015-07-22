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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Charsets;

@Controller
@RequestMapping("/publisherCatalog")
public class CatalogViewController {
	
	@RequestMapping(value = "/schedulesCatalog", method = RequestMethod.GET)
	public String getProgrammesCatalog(
		@RequestParam(value="hashNameProg", defaultValue="", required=true) String hashNameProg, Model model)
	{
		model.addAttribute("hashNameProg", hashNameProg);
		return "schedules_catalog_tpt";
	}
	
}
