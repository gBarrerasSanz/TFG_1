package guiatv.catalog.restcontroller;

import guiatv.catalog.datatypes.Catalog;
import guiatv.catalog.datatypes.ListChannels;
import guiatv.catalog.datatypes.ListProgrammes;
import guiatv.common.CommonUtility;
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
import guiatv.realtime.rtmpspying.MutexMonitor;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

@Profile({"CatalogRestControllerTests", "dev", "default"})
@RestController
@RequestMapping("/catalog")
public class CatalogRestController {
	
	
	private static final Logger logger = LoggerFactory.getLogger(CatalogRestController.class);
	
	@Autowired
	ScheduleService schedServ;
	@Autowired
	ChannelService chServ;
	@Autowired
	ProgrammeService progServ;
	@Autowired
	ObjectMapper mapper;
	@Autowired
	MutexMonitor monitor;
	
	
	@RequestMapping(
			value = "", 
			method = RequestMethod.GET)
	@ResponseBody
	// TODO: @RequestParam(value = "country", required = false, defaultValue = "spain") String country
	public ResponseEntity<Catalog> getCatalog()
	{
		// TODO: (Muy largo plazo) Controlar dependiendo de los paises
		Catalog catalog = new Catalog();
		catalog.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
				CatalogRestController.class).getCatalog()).withSelfRel());
		catalog.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
				CatalogRestController.class).getChannels()).withRel("channels"));
		catalog.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
				CatalogRestController.class).getProgrammes()).withRel("programmes"));
		
		return new ResponseEntity<Catalog>(catalog, HttpStatus.OK);
	}
	@RequestMapping(
			value = "/channels", 
			method = RequestMethod.GET)
	@JsonView(Channel.MultipleChannels.class)
	public ResponseEntity<ListChannels> getChannels()
	{
		ListChannels lChannels = chServ.findAll();
		ListChannels flChannels = new ListChannels();
		// Filtrar dejando solo los channels activos
		for (Channel ch: lChannels) {
			if (monitor.checkActiveChannel(ch)) {
				flChannels.add(ch);
			}
		}
		// Añadir los links HATEOAS
		for (Channel ch: flChannels) {
			ch.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
					CatalogRestController.class).getChannelByHashIdChBusiness(
							ch.getHashIdChBusiness())).withSelfRel());
		}
		return new ResponseEntity<ListChannels>(flChannels, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/channels/{hashIdChBusiness}", 
			method = RequestMethod.GET)
	@JsonView(Channel.SingleChannel.class)
	public ResponseEntity<Channel> getChannelByHashIdChBusiness(@PathVariable(value = "hashIdChBusiness") String hashIdChBusiness)
	{
	
		Channel ch = chServ.findByHashIdChBusiness(hashIdChBusiness, true);
		if (ch == null){
			return new ResponseEntity<Channel>(new Channel(), HttpStatus.OK);
		}
		ch.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
				CatalogRestController.class).getChannelByHashIdChBusiness(hashIdChBusiness)).withSelfRel());
		List<Schedule> lSched = schedServ.findByChannel(ch, true);
		ch.setListSchedules(lSched);
		// Computar listProgrammes
		ch.computeListProgrammesFromListSchedules();
		for (Programme prog: ch.getListProgrammes()){
			prog.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
					CatalogRestController.class)
					.getProgrammeByHashNameProg(prog.getHashNameProg())).withSelfRel());
			prog.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
					CatalogRestController.class)
					.getChannelByHashIdChBusiness(ch.getHashIdChBusiness())).withRel("channel"));
		}
//		HashMap<String, Integer> hmProg = new HashMap<String, Integer>();
//		for (Schedule sched: lSched) {
//			if (hmProg.putIfAbsent(sched.getProgramme().getHashNameProg(), 1) == null) {
//				ch.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
//						CatalogRestController.class)
//						.getProgrammeByHashNameProg(sched.getProgramme().getHashNameProg())).withRel("programmes"));
//			}
//		}
		
		return new ResponseEntity<Channel>(ch, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/programmes", 
			method = RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@JsonView(Programme.MultipleProgrammes.class)
	public ResponseEntity<ListProgrammes> getProgrammes()
	{
		ListProgrammes lProgrammes = progServ.findAll();
		for (Programme prog: lProgrammes) {
			prog.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
					CatalogRestController.class).getProgrammeByHashNameProg(
							prog.getHashNameProg())).withSelfRel());
		}
		return new ResponseEntity<ListProgrammes>(lProgrammes, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/programmes/{hashNameProg}", 
			method = RequestMethod.GET)
	@JsonView(Programme.SingleProgramme.class)
	public ResponseEntity<Programme> getProgrammeByHashNameProg(@PathVariable(value = "hashNameProg") String hashNameProg)
	{
		Programme prog =  progServ.findByHashNameProg(hashNameProg, true);
		if (prog == null) {
			return new ResponseEntity<Programme>(new Programme(), HttpStatus.OK);
		}
		prog.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
				CatalogRestController.class).getProgrammeByHashNameProg(
						prog.getHashNameProg())).withSelfRel());
		
		// Devolver los enlaces de los channels en los que se emite
		// De cada schedule del programme, coger el channel al que pertenece
//		for (Schedule sched: prog.getListSchedules()){
//			System.out.println(sched);
	//			// Enlaces del channel de la emisión concreta del programme
	//			prog.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
	//			CatalogRestController.class).getChannelByHashIdChBusiness(
	//					sched.getChannel().getHashIdChBusiness())).withRel("channels"));
//		}
		
		return new ResponseEntity<Programme>(prog, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/schedules/", 
			method = RequestMethod.GET)
	@JsonView(Schedule.CustomSchedule.class)
	public ResponseEntity<List<Schedule>> getSchedulesByHashIdChBusinessAndHashNameProgAndStartAndEnd(
			@RequestParam(value="hashIdChBusiness", defaultValue="", required=false) String hashIdChBusiness,
			@RequestParam(value="hashNameProg", defaultValue="", required=false) String hashNameProg,
			@RequestParam(value="start", defaultValue="", required=false) @DateTimeFormat(pattern=CommonUtility.zonedDateFormat) String startDate,
			@RequestParam(value="end", defaultValue="", required=false) @DateTimeFormat(pattern=CommonUtility.zonedDateFormat) String endDate)
	{
		ResponseEntity<List<Schedule>> response;
		ResponseEntity<List<Schedule>> errorResp = new ResponseEntity<List<Schedule>>(new ArrayList<Schedule>(), HttpStatus.BAD_REQUEST);
		Date start, end;
		try {
			start = CommonUtility.utcDateStrToUtcDate(startDate);
			end = CommonUtility.utcDateStrToUtcDate(endDate);
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
						start, end);
				if(sched != null) { // Si se ha encontrado un schedule
					List<Schedule> lSched = new ArrayList<Schedule>();
					lSched.add(sched);
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
		 * CHANNEL, PROGRAMME, START -> DEVOLVER SCHEDULES A PARTIR DE START
		 */
		else if (hashNameProg.length()>0 && hashIdChBusiness.length()>0 
				&& start != null && end == null) {
			// Si se especifica canal, programa y start -> Devolver todos los schedules a partir de start
			Channel ch = chServ.findByHashIdChBusiness(hashIdChBusiness, true);
			Programme prog = progServ.findByHashNameProg(hashNameProg, true);
			if (ch != null && prog != null) {
				List<Schedule> lSched = schedServ.findByChannelAndProgrammeAndStartGreaterOrEqualThan(
						ch, prog, start, true);
				response = new ResponseEntity<List<Schedule>>(lSched, HttpStatus.OK);
			}
			else {
				response = errorResp;
			}
		}
		/**
		 * CHANNEL, PROGRAMME, END -> DEVOLVER SCHEDULES ANTES DE END
		 */
		else if (hashNameProg.length()>0 && hashIdChBusiness.length()>0 
				&& start == null && end != null) {
			// Si se especifica canal, programa y end -> Devolver todos los schedules antes de end
			Channel ch = chServ.findByHashIdChBusiness(hashIdChBusiness, true);
			Programme prog = progServ.findByHashNameProg(hashNameProg, true);
			if (ch != null && prog != null) {
				List<Schedule> lSched = schedServ.findByChannelAndProgrammeAndEndLessThan(
						ch, prog, end, true);
				response = new ResponseEntity<List<Schedule>>(lSched, HttpStatus.OK);
			}
			else {
				response = errorResp;
			}
		}
		/**
		 * CHANNEL, PROGRAMME -> DEVOLVER SCHEDULES DESPUES DE NOW()
		 */
		else if (hashNameProg.length()>0 && hashIdChBusiness.length()>0 
				&& start == null && end == null) {
			// Si se especifica canal, programa y start -> Devolver todos los schedules a partir de start
			Channel ch = chServ.findByHashIdChBusiness(hashIdChBusiness, true);
			Programme prog = progServ.findByHashNameProg(hashNameProg, true);
			Date now = new Date();
			if (ch != null && prog != null) {
				List<Schedule> lSched = schedServ.findByChannelAndProgrammeAndStartGreaterOrEqualThan(
						ch, prog, now, true);
				response = new ResponseEntity<List<Schedule>>(lSched, HttpStatus.OK);
			}
			else {
				response = errorResp;
			}
		}
		/**
		 * PROGRAMME -> DEVOLVER SCHEDULES DESPUES DE NOW()
		 */
		else if (hashNameProg.length()>0 && hashIdChBusiness.length()==0 
				&& start == null && end == null) {
			Programme prog = progServ.findByHashNameProg(hashNameProg, true);
			if (prog != null) {
				List<Schedule> lSched = schedServ.findByProgramme(
						prog, true);
				response = new ResponseEntity<List<Schedule>>(lSched, HttpStatus.OK);
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
