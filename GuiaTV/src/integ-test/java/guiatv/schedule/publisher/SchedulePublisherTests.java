package guiatv.schedule.publisher;

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
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import guiatv.Application;
import guiatv.ApplicationTest;
import guiatv.common.CommonUtility;
import guiatv.persistence.domain.Event_old;
import guiatv.persistence.domain.Schedule;
import guiatv.realtime.servicegateway.CapturedFramesGateway;
import guiatv.schedule.publisher.SchedulePublisher;
import guiatv.schedule.utils.ListScheduleCreator;
import guiatv.xmltv.transformer.XMLTVTransformer_old1;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@ActiveProfiles("SchedulePublisherTests")
@WebIntegrationTest
public class SchedulePublisherTests {
	
	private static final Logger log = Logger.getLogger("debugLog");
	
	
	@Autowired
	private SchedulePublisher schedPublisher;
	
	@Autowired
	private TaskExecutorMQTTClient client1, client2;
	
//	@Autowired
//	private TaskExecutorMQTTClient client2;
	
	/*
	 * Este test se completa satisfactoriamente aun sin estar el RabbitMQ funcionando...
	 * */
	@Test
	public void publishEventSimple() {
//		String ch1 = "ch1_Test";
//		String prog1 = "prog1_Test";
//		String prog2 = "prog2_Test";
		
		// Construir los schedules
		List<Schedule> lSchedPub = ListScheduleCreator.getListSchedule();
		try {
			
			
			// Hacer la suscripción por parte de los clientes
			String[] cl1MsgsArr = new String[]{
					lSchedPub.get(0).getChannel().getIdChBusiness()+"."+lSchedPub.get(0).getProgramme().getNameProg()};
			String[] cl2MsgsArr = new String[]{
					lSchedPub.get(1).getChannel().getIdChBusiness()+"."+lSchedPub.get(1).getProgramme().getNameProg()};
			client1.connectAndSubscribe("client1", cl1MsgsArr);
			client2.connectAndSubscribe("client2", cl2MsgsArr);
			
			// Enviar schedules
			Message<List<Schedule>> lSchedPubMsg = MessageBuilder.
					withPayload(lSchedPub).build();
			schedPublisher.publishTopics(lSchedPubMsg);
			log.debug("Schedules publicados");
			
			// Confirmar que los consumidores lo han recibido
			List<String> cl1Msgs = new ArrayList<>(Arrays.asList(cl1MsgsArr));
			List<String> cl2Msgs = new ArrayList<String>(Arrays.asList(cl2MsgsArr));
			long initTime = System.currentTimeMillis();
			long timeout = 1000;
			boolean mustContinue = true;
			while (mustContinue) {
				if (System.currentTimeMillis() > initTime + timeout
						|| (cl1Msgs.equals(client1.getReceivedListSchedules()) && cl2Msgs.equals(client2.getReceivedListSchedules()))) 
				{
					mustContinue = false;
				}
				else {
					Thread.sleep(50);
				}
			}
			assertEquals(lSchedPub.get(0), client1.getReceivedListSchedules().get(0));
			assertEquals(lSchedPub.get(1), client2.getReceivedListSchedules().get(0));
			client1.disconnect();
			client2.disconnect();
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
