package guiatv.catalog.restcontroller;

import guiatv.catalog.datatypes.Catalog;
import guiatv.catalog.datatypes.ListChannels;
import guiatv.catalog.datatypes.ListProgrammes;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ChannelRepository;
import guiatv.persistence.repository.ProgrammeRepository;
import guiatv.persistence.repository.ScheduleRepository;
import guiatv.persistence.repository.service.ChannelService;
import guiatv.persistence.repository.service.ProgrammeService;
import guiatv.persistence.repository.service.ScheduleService;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
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
	
	public interface MultipleProgrammes extends ResourcesLinksVisible {}
	public interface SingleProgramme extends ResourcesLinksVisible {}
	
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
					CatalogRestController.class).getChannelByIdChBusiness(
							ch.getIdChBusiness())).withSelfRel());
		}
		return new ResponseEntity<ListChannels>(lChannels, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/channels/{idChBusiness:.*}", 
			method = RequestMethod.GET)
	public ResponseEntity<Channel> getChannelByIdChBusiness(@PathVariable(value = "idChBusiness") String idChBusiness)
	{
	
		Channel ch = chServ.findByIdChBusiness(idChBusiness);
		if (ch == null){
			return new ResponseEntity<Channel>(new Channel(), HttpStatus.OK);
		}
		ch.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
				CatalogRestController.class).getChannelByIdChBusiness(idChBusiness)).withSelfRel());
		List<Schedule> lSched =  schedServ.findByChannel(ch, true);
		HashMap<String, Integer> hmProg = new HashMap<String, Integer>();
		for (Schedule sched: lSched) {
			if (hmProg.putIfAbsent(sched.getProgramme().getNameProg(), 1) == null) {
				ch.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
						CatalogRestController.class)
						.getProgrammeByNameProg(sched.getProgramme().getNameProg())).withRel("programmes"));
			}
		}
		
		return new ResponseEntity<Channel>(ch, HttpStatus.OK);
	}
	
	@JsonView(MultipleProgrammes.class)
	@RequestMapping(
			value = "/programmes", 
			method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ListProgrammes> getProgrammes()
	{
		ListProgrammes lProgrammes = progServ.findAll();
		for (Programme prog: lProgrammes) {
			prog.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
					CatalogRestController.class).getProgrammeByNameProg(
							prog.getNameProg())).withSelfRel());
		}
		return new ResponseEntity<ListProgrammes>(lProgrammes, HttpStatus.OK);
	}
	
	@JsonView(SingleProgramme.class)
	@RequestMapping(
			value = "/programmes/{nameProg}", 
			method = RequestMethod.GET)
	public ResponseEntity<Programme> getProgrammeByNameProg(@PathVariable(value = "nameProg") String nameProg)
	{
		Programme prog =  progServ.findByNameProg(nameProg);
		if (prog == null) {
			return new ResponseEntity<Programme>(new Programme(), HttpStatus.OK);
		}
		prog.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
				CatalogRestController.class).getProgrammeByNameProg(
						prog.getNameProg())).withSelfRel());
		// Devolver los enlaces de los channels en los que se emite
		// De cada schedule del programme, coger el channel al que pertenece
		for (Schedule sched: prog.getListSchedules()){
			// Enlaces de los channels en los que se emite el programa
//			prog.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
//					CatalogRestController.class).getChannelByIdChBusiness(
//							sched.getChannel().getIdChBusiness())).withRel("channels"));
			// Enlaces del channel de la emisión concreta del programme
//			prog.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
//			CatalogRestController.class).getChannelByIdChBusiness(
//					sched.getChannel().getIdChBusiness())).withRel("channels"));
		}
		
		return new ResponseEntity<Programme>(prog, HttpStatus.OK);
	}
}
