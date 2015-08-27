package guiatv.catalog.restcontroller;

import guiatv.common.CommonUtility;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ChannelRepository;
import guiatv.realtime.rtmpspying.MutexMonitor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;


@RestController
@RequestMapping("/admin/channels")
public class AdministrationRestController {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	@Autowired
	ChannelRepository chRep;
	@Autowired
	MutexMonitor monitor;
	
	@RequestMapping(
			value = "/activation/", 
			method = RequestMethod.POST)
	public ResponseEntity<Boolean> changeChannelActivation(
			@RequestParam(value="hashIdChBusiness", defaultValue="", required=true) String hashIdChBusiness,
			@RequestParam(value="activation", defaultValue="", required=true) boolean activation)
	{
		ResponseEntity<Boolean> okResp = new ResponseEntity<Boolean>(true, HttpStatus.ACCEPTED);
		ResponseEntity<Boolean> errResp = new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		Channel ch = chRep.findByHashIdChBusiness(hashIdChBusiness);
		if (ch != null) {
			if (activation) {
				monitor.activateChannel(ch);
				logger.debug("Channel ["+ch.getIdChBusiness()+"] is now ACTIVE");
			}
			else {
				monitor.deactivateChannel(ch);
				logger.debug("Channel ["+ch.getIdChBusiness()+"] is now OFF");
			}
			return okResp;
		}
		else {
			return errResp;
		}
	}

}
