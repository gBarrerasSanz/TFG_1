package guiatv.schedule.poller;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import guiatv.Application;
import guiatv.ApplicationTest;
import guiatv.common.CommonUtility;
import guiatv.eventmanager.ImgProcessingGateway;
import guiatv.persistence.domain.Event_old;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ScheduleRepository;
import guiatv.schedule.poller.SchedulePoller;
import guiatv.schedule.publisher.SchedulePublisher;
import guiatv.schedule.publisher.TaskExecutorMQTTClient;
import guiatv.schedule.utils.ListScheduleCreator;
import guiatv.xmltv.transformer.XMLTVTransformer_old1;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@ActiveProfiles("SchedulePollerTests")
public class SchedulePollerTests extends AbstractTransactionalJUnit4SpringContextTests {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	@Autowired
	ScheduleRepository schedRep;
	
	@Autowired
	SchedulePoller schedPoller;
	
	@Autowired
	SchedulePollerOutDummy schedPollerOutDummy;
	
	/**
	 * Comprobar que los schedules que devuelve el SchedulePoller son los mismos que se han introducido
	 * en la base de datos.
	 * ATENCION: Este test requiere que la persistencia funcione correctamente.
	 */
	@Test
	public void pollForSchedulesTest() {
		try {
			// Cargar valores de prueba en la base de datos
			List<Schedule> listSchedExpected = ListScheduleCreator.getListSchedule();
			schedRep.save(listSchedExpected);
			
			List<Schedule> listSchedPolled = schedPoller.askForSchedules();
			
			Assert.assertEquals(listSchedExpected, listSchedPolled);
			
//			boolean found = false;
//			for(Schedule schedPolled: listSchedPolled) {
//				found = false;
//				for (Schedule schedExp: listSchedExpected) {
//					if (schedPolled.getProgramme().getNomProg().equals(schedRep.getProgramme().getNomProg())) {
//						found = true;
////						logger.info("Found programme with name"+schedExp.getProgramme().getNomProg());
//						break;
//					}
//				}
//				Assert.assertEquals(true, found);
//			}
//			Assert.assertEquals(true, found);
			
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
