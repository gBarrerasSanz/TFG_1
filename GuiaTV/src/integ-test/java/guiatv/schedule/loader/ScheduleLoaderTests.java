package guiatv.schedule.loader;

import static org.junit.Assert.*;

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
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import guiatv.Application;
import guiatv.ApplicationTest;
import guiatv.common.CommonUtility;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ScheduleRepository;
import guiatv.persistence.repository.service.ScheduleService;
import guiatv.realtime.servicegateway.CapturedBlobsGateway;
import guiatv.schedule.loader.ScheduleLoader;
import guiatv.schedule.utils.ListScheduleCreator;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@ActiveProfiles("ScheduleLoaderTests")
@WebIntegrationTest
public class ScheduleLoaderTests extends
		AbstractTransactionalJUnit4SpringContextTests {

	private static Logger logger = Logger.getLogger("debugLog");

	// @Autowired
	// private ApplicationContext ctx;

	@Autowired
	ScheduleLoader schedLoader;

	@Autowired
	ScheduleService shedServ;

	@Before
	public void doNothing() {
		logger.info("No hacer nada");
	}

	@Test
	public void createMultipleEventsTest() {
		try {
			// Construir resultado esperado
			List<Schedule> listScheduleExpected = ListScheduleCreator
					.getListSchedule();

			schedLoader.loadListSchedules(listScheduleExpected);
			List<Schedule> listScheduleReturned = shedServ.findAll(true);

			Assert.assertNotNull(
					"Expected a non null instance of List<Evento>, got null",
					listScheduleReturned);
			boolean found = false;
			for (Schedule schedExp : listScheduleExpected) {
				found = false;
				for (Schedule schedRet : listScheduleReturned) {
					if (schedExp.getProgramme().getNameProg()
							.equals(schedRet.getProgramme().getNameProg())) {
						found = true;
						logger.info("Found programme with name"
								+ schedExp.getProgramme().getNameProg());
						break;
					}
				}
				Assert.assertEquals(true, found);
			}
			Assert.assertEquals(true, found);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}