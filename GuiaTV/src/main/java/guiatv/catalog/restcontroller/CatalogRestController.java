package guiatv.catalog.restcontroller;

import guiatv.catalog.datatypes.Catalog;
import guiatv.catalog.datatypes.ListChannels;
import guiatv.catalog.datatypes.ListProgrammes;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Programme.MultipleProgrammes;
import guiatv.persistence.domain.Programme.SingleProgramme;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ChannelRepository;
import guiatv.persistence.repository.ProgrammeRepository;
import guiatv.persistence.repository.ScheduleRepository;
import guiatv.persistence.repository.service.ChannelService;
import guiatv.persistence.repository.service.ProgrammeService;
import guiatv.persistence.repository.service.ScheduleService;

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
	
	
	@RequestMapping(
			value = "", 
			method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Catalog> getCatalog(@RequestParam(value = "country", 
			required = false, defaultValue = "spain") String country)
	{
		// TODO: (Muy largo plazo) Controlar dependiendo de los paises
		Catalog catalog = new Catalog(country);
		catalog.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
				CatalogRestController.class).getCatalog(country)).withSelfRel());
		catalog.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
				CatalogRestController.class).getChannels()).withRel("channels"));
		catalog.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
				CatalogRestController.class).getProgrammes()).withRel("programmes"));
		
		return new ResponseEntity<Catalog>(catalog, HttpStatus.OK);
	}
	@RequestMapping(
			value = "/channels", 
			method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ListChannels> getChannels()
	{
		ListChannels lChannels = chServ.findAll();
		for (Channel ch: lChannels) {
			ch.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
					CatalogRestController.class).getChannelByHashIdChBusiness(
							ch.getHashIdChBusiness())).withSelfRel());
		}
		return new ResponseEntity<ListChannels>(lChannels, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/channels/{hashIdChBusiness}", 
			method = RequestMethod.GET)
	public ResponseEntity<Channel> getChannelByHashIdChBusiness(@PathVariable(value = "hashIdChBusiness") String hashIdChBusiness)
	{
	
		Channel ch = chServ.findByHashIdChBusiness(hashIdChBusiness, false);
		if (ch == null){
			return new ResponseEntity<Channel>(new Channel(), HttpStatus.OK);
		}
		ch.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
				CatalogRestController.class).getChannelByHashIdChBusiness(hashIdChBusiness)).withSelfRel());
		List<Schedule> lSched =  schedServ.findByChannel(ch, true);
		HashMap<String, Integer> hmProg = new HashMap<String, Integer>();
		for (Schedule sched: lSched) {
			if (hmProg.putIfAbsent(sched.getProgramme().getHashNameProg(), 1) == null) {
				ch.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
						CatalogRestController.class)
						.getProgrammeByHashNameProg(sched.getProgramme().getHashNameProg())).withRel("programmes"));
			}
		}
		
		return new ResponseEntity<Channel>(ch, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/programmes", 
			method = RequestMethod.GET)
	@JsonView(Programme.MultipleProgrammes.class)
	@ResponseBody
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
		for (Schedule sched: prog.getListSchedules()){
			System.out.println(sched);
	//			// Enlaces del channel de la emisi�n concreta del programme
	//			prog.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
	//			CatalogRestController.class).getChannelByHashIdChBusiness(
	//					sched.getChannel().getHashIdChBusiness())).withRel("channels"));
		}
		
		return new ResponseEntity<Programme>(prog, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/schedules/", 
			method = RequestMethod.GET)
	@JsonView(Programme.SingleProgramme.class)
	public ResponseEntity<List<Schedule>> getSchedulesByHashIdChBusinessAndHashNameProgAndStartAndEnd(
			@RequestParam(value="hashIdChBusiness", defaultValue="", required=false) String hashIdChBusiness,
			@RequestParam(value="hashNameProg", defaultValue="", required=false) String hashNameProg,
			@RequestParam(value="start", defaultValue="", required=false) String startStr,
			@RequestParam(value="end", defaultValue="", required=false) String endStr)
	{
		ResponseEntity<List<Schedule>> response;
		ResponseEntity<List<Schedule>> errorResp = new ResponseEntity<List<Schedule>>(new ArrayList<Schedule>(), HttpStatus.BAD_REQUEST);
		Timestamp start = Timestamp.valueOf(startStr);
		Timestamp end = Timestamp.valueOf(endStr);
		if (hashNameProg.length()>0 && hashIdChBusiness.length()>0 
				&& start.toString().length()>0 && end.toString().length()>0) {
			// Si se especifican todos los par�metros -> Devolver el schedule (debe ser �nico 
			// seg�n la restricci�n de unicidad)
			Channel ch = chServ.findByHashIdChBusiness(hashIdChBusiness, true);
			Programme prog = progServ.findByHashNameProg(hashNameProg, true);
			if (ch != null && prog != null) {
				Schedule sched = schedServ.findByChannelAndProgrammeAndStartAndEnd(ch, prog, 
						new Timestamp(start.getTime()), new Timestamp(end.getTime()));
				List<Schedule> lSched = new ArrayList<Schedule>(1);
				lSched.set(0, sched);
				response = new ResponseEntity<List<Schedule>>(lSched, HttpStatus.OK);
			}
			else {
				response = errorResp;
			}
		}
		else if (hashNameProg.length()>0 && hashIdChBusiness.length()>0) {
			// Si se especifica solamente canal y programa -> Devolver todos los schedules desde ahora
			Channel ch = chServ.findByHashIdChBusiness(hashIdChBusiness, true);
			Programme prog = progServ.findByHashNameProg(hashNameProg, true);
			if (ch != null && prog != null) {
				List<Schedule> lSched = schedServ.findByChannelAndProgramme(ch, prog);
				response = new ResponseEntity<List<Schedule>>(lSched, HttpStatus.OK);
			}
			else {
				response = errorResp;
			}
				
		}
		else if (hashIdChBusiness.length()>0 && hashNameProg.length()==0 
				&& start.toString().length()==0 && end.toString().length()==0) {
			// Si solo se especifica el Channel -> Devolver error
			response = errorResp;
		}
		else if (hashNameProg.length()>0 && hashIdChBusiness.length()==0 
				&& start.toString().length()==0 && end.toString().length()==0) {
			// Si solo se especifica el Programme -> Devolver error
			response = errorResp;
		}
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
