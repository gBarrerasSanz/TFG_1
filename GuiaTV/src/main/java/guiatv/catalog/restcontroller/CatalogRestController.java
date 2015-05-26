package guiatv.catalog.restcontroller;

import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ScheduleRepository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/catalog")
public class CatalogRestController {
	
	private static final Logger logger = LoggerFactory.getLogger(CatalogRestController.class);
	
	@Autowired
	ScheduleRepository schedRep;
	
	@RequestMapping(
			value = "/rendermap", 
			method = RequestMethod.GET)
	public ResponseEntity<List<Schedule>> findAllSchedules(@RequestHeader HttpHeaders headers){
		ResponseEntity<List<Schedule>> resp;
		List<Schedule> listSchedule = null;
		try{
			listSchedule = schedRep.findAll();
		}catch(Exception e){
			e.printStackTrace();
		}
		resp = new ResponseEntity<List<Schedule>>(listSchedule, HttpStatus.OK);
		return resp;
	}
}
