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
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import guiatv.Application;
import guiatv.ApplicationTest;
import guiatv.common.CommonUtility;
import guiatv.eventmanager.ImgProcessingGateway;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Event_old;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ScheduleRepository;
import guiatv.persistence.utils.ListScheduleCreator;
import guiatv.scheduleloader.ScheduleLoader;
import guiatv.xmltv.transformer.XMLTVTransformer_old1;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("/META-INF/spring/integration/spring-integration-context.xml")
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@ActiveProfiles("PersistenceTests")
public class ScheduleLoaderTests {

	private static Logger logger = Logger.getLogger("debugLog");
	
//	@Autowired
//	private ApplicationContext ctx;
	
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
		List<Schedule> listScheduleExpected = ListScheduleCreator.getListSchedule();
		
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