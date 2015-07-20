package guiatv.catalog.restcontroller;

import guiatv.catalog.datatypes.Catalog;
import guiatv.catalog.datatypes.ListChannels;
import guiatv.catalog.datatypes.ListProgrammes;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Programme.MultipleProgrammes;
import guiatv.persistence.domain.Programme.SingleProgramme;
import guiatv.persistence.domain.Schedule.CustomSchedule;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ChannelRepository;
import guiatv.persistence.repository.ProgrammeRepository;
import guiatv.persistence.repository.ScheduleRepository;
import guiatv.persistence.repository.service.ChannelService;
import guiatv.persistence.repository.service.ProgrammeService;
import guiatv.persistence.repository.service.ScheduleService;
import guiatv.schedule.publisher.SchedulePublisher;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.ResourcesLinksVisible;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;

@Profile({"CatalogRestControllerTests, dev, default"})
@RestController
@RequestMapping("/publisher")
public class PublisherRestController {
	
	
	private static final Logger logger = LoggerFactory.getLogger(PublisherRestController.class);
	
	@Autowired
	ScheduleService schedServ;
	@Autowired
	ChannelService chServ;
	@Autowired
	ProgrammeService progServ;
	@Autowired
	ObjectMapper mapper;
	@Autowired 
	SchedulePublisher publisher;
	
	@RequestMapping(
			value = "/schedules/", 
			method = RequestMethod.GET)
	@JsonView(Schedule.CustomSchedule.class)
	public ResponseEntity<List<Schedule>> publishSchedulesByHashIdChBusinessAndHashNameProgAndStartAndEnd(
			@RequestParam(value="hashIdChBusiness", defaultValue="", required=false) String hashIdChBusiness,
			@RequestParam(value="hashNameProg", defaultValue="", required=false) String hashNameProg,
			@RequestParam(value="start", defaultValue="", required=false) @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss a z") Date startDate,
			@RequestParam(value="end", defaultValue="", required=false) @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss a z") Date endDate)
	{
		ResponseEntity<List<Schedule>> response;
		ResponseEntity<List<Schedule>> errorResp = new ResponseEntity<List<Schedule>>(new ArrayList<Schedule>(), HttpStatus.BAD_REQUEST);
		Timestamp start, end;
		try {
			start = new Timestamp(startDate.getTime());
			end = new Timestamp(endDate.getTime());
		} catch(Exception e) {
			start = null;
			end = null;
		}
		/**
		 * CHANNEL, PROGRAMME, START, END -> DEVOLVER SCHEDULE ÚNICO
		 */
		if (hashNameProg.length()>0 && hashIdChBusiness.length()>0 
				&& start != null && end != null) {
			// Si se especifican todos los parámetros -> Devolver el schedule (debe ser único 
			// según la restricción de unicidad)
			Channel ch = chServ.findByHashIdChBusiness(hashIdChBusiness, true);
			Programme prog = progServ.findByHashNameProg(hashNameProg, true);
			if (ch != null && prog != null) {
				Schedule sched = schedServ.findByChannelAndProgrammeAndStartAndEnd(ch, prog, 
						new Timestamp(start.getTime()), new Timestamp(end.getTime()));
				if(sched != null) { // Si se ha encontrado un schedule
					List<Schedule> lSched = new ArrayList<Schedule>();
					lSched.add(sched);
					/********************************************
					 * PUBLICAR SCHEDULE
					 *******************************************/
					Message<List<Schedule>> schedMsg = MessageBuilder.withPayload(lSched).build();
					publisher.publishTopics(schedMsg);
					response = new ResponseEntity<List<Schedule>>(lSched, HttpStatus.OK);
				}
				else { // Si NO se ha encontrado ningún schedule
					response = new ResponseEntity<List<Schedule>>(new ArrayList<Schedule>(), HttpStatus.NOT_FOUND);
				}
			}
			else {
				response = errorResp;
			}
		}
		/**
		 * EN CUALAQUIER OTRO CASO
		 */
		else { 
			// En cualquier otro caso -> Devolver error
			response = errorResp;
		}
		for (Schedule sched: response.getBody()) {
			sched.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
					CatalogRestController.class).getChannelByHashIdChBusiness(
							sched.getChannel().getHashIdChBusiness())).withRel("channel"));
			sched.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
					CatalogRestController.class).getProgrammeByHashNameProg(
							sched.getProgramme().getHashNameProg())).withRel("programme"));
		}
		return response;
	}
		
		
}
