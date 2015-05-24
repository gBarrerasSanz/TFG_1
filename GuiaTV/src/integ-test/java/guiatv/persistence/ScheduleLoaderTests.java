package guiatv.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import guiatv.Application;
import guiatv.common.CommonUtility;
import guiatv.domain.Channel;
import guiatv.domain.Event;
import guiatv.domain.Programme;
import guiatv.domain.Schedule;
import guiatv.eventmanager.EventService;
import guiatv.persistence.repository.ScheduleRepository;
import guiatv.scheduleloader.ScheduleLoader;
import guiatv.xmltv.transformer.XMLTVTransformer_old1;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@DirtiesContext
public class ScheduleLoaderTests {

	private static Logger logger = Logger.getLogger("debugLog");
	
	@Autowired
	private ApplicationContext ctx;
	
	@Autowired
	ScheduleLoader schedLoader;
	
	@Autowired
	ScheduleRepository shedRep;
	
	@Before
	public void doNothing() {
		logger.info("No hacer nada");
	}
	
	@Test
	public void createMultipleEventsTest() {
		
		// Construir resultado esperado
		List<Schedule> listScheduleExpected = new ArrayList<Schedule>();
		// Crear channels
		Channel chNeox = new Channel();
		chNeox.setNomIdCh("neox-722.laguiatv.com");
		// Crear programmes
		Programme progMadre = new Programme();
		progMadre.setNomProg("Cómo conocí a vuestra Madre");
		Programme progSimpsons = new Programme();
		progSimpsons.setNomProg("Los Simpson");
		// Crear schedules
		Schedule schedMadre = new Schedule();
		schedMadre.setChannel(chNeox);
		schedMadre.setProgramme(progMadre);
		schedMadre.setStart(CommonUtility.strToDate("20150316173500 +0100"));
		schedMadre.setEnd(CommonUtility.strToDate("20150316175400 +0100"));
		
		Schedule schedSimpsons = new Schedule();
		schedSimpsons.setChannel(chNeox);
		schedSimpsons.setProgramme(progSimpsons);
		schedSimpsons.setStart(CommonUtility.strToDate("20150316214500 +0100"));
		schedSimpsons.setEnd(CommonUtility.strToDate("20150316220000 +0100"));
		
		listScheduleExpected.add(schedMadre);
		listScheduleExpected.add(schedSimpsons);
		schedLoader.loadListSchedules(listScheduleExpected);
		List<Schedule> listScheduleReturned= shedRep.findAll();

		Assert.assertNotNull("Expected a non null instance of List<Evento>, got null", listScheduleReturned);
		boolean found = false;
		for (Schedule schedExp: listScheduleExpected) {
			found = false;
			for (Schedule schedRet: listScheduleReturned) {
				if (schedExp.getProgramme().getNomProg().equals(schedRet.getProgramme().getNomProg())) {
					found = true;
					logger.info("Found programme with name"+schedExp.getProgramme().getNomProg());
					break;
				}
			}
			Assert.assertEquals(true, found);
		}
		Assert.assertEquals(true, found);
	}
	
	
	
	
}