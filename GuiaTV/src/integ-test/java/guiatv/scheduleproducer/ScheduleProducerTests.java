package guiatv.scheduleproducer;

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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import guiatv.Application;
import guiatv.ApplicationTest;
import guiatv.common.CommonUtility;
import guiatv.eventmanager.ImgProcessingGateway;
import guiatv.eventproducer.utils.TaskExecutorMQTTClient;
import guiatv.persistence.domain.Event_old;
import guiatv.scheduleproducer.ScheduleProducerPublisher;
import guiatv.xmltv.transformer.XMLTVTransformer_old1;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@ActiveProfiles("ScheduleProducerTests")
public class ScheduleProducerTests {
	
	private static final Logger log = Logger.getLogger("debugLog");
	
	@Autowired
	private ApplicationContext ctx;
	
	@Autowired
	private XMLTVTransformer_old1 transformer;
	
	@Autowired
	private ImgProcessingGateway evService;
	
	@Autowired
	private ScheduleProducerPublisher evProd;
	
	@Autowired
	private TaskExecutorMQTTClient client1;
	
	@Autowired
	private TaskExecutorMQTTClient client2;
	
	/*
	 * Este test se completa satisfactoriamente aun sin estar el RabbitMQ funcionando...
	 * */
	@Test
	public void publishEventSimple() {
		String ch1 = "ch1_Test";
		String prog1 = "prog1_Test";
		String prog2 = "prog2_Test";
		try {
			// Hacer la suscripción por parte de los clientes
			String[] cl1MsgsArr = new String[]{ch1+"."+prog1};
			String[] cl2MsgsArr = new String[]{ch1+"."+prog1, ch1+"."+prog2};
			client1.connectAndSubscribe("client1", cl1MsgsArr);
			client2.connectAndSubscribe("client2", cl2MsgsArr);
			
			// Producir mensaje
			List<Event_old> lEvt = new ArrayList<Event_old>();
			Event_old evt1 = new Event_old(ch1,	prog1, new Date(), new Date());
			Event_old evt2 = new Event_old(ch1, prog2, new Date(), new Date());
			lEvt.add(evt1); lEvt.add(evt2);
			Message<List<Event_old>> lEvtMsg = MessageBuilder.
					withPayload(lEvt).build();
			evProd.publishTopics(lEvtMsg);
			log.debug("Topics publicados");
			
			// Confirmar que los consumidores lo han recibido
			List<String> cl1Msgs = new ArrayList<>(Arrays.asList(cl1MsgsArr));
			List<String> cl2Msgs = new ArrayList<String>(Arrays.asList(cl2MsgsArr));
			long initTime = System.currentTimeMillis();
			long timeout = 1000;
			boolean mustContinue = true;
			while (mustContinue) {
				if (System.currentTimeMillis() > initTime + timeout
						|| (cl1Msgs.equals(client1.getReceivedMessages()) && cl2Msgs.equals(client2.getReceivedMessages()))) 
				{
					mustContinue = false;
				}
				else {
					Thread.sleep(50);
				}
			}
			assertEquals(cl1Msgs, client1.getReceivedMessages());
			assertEquals(cl2Msgs, client2.getReceivedMessages());
			client1.disconnect();
			client2.disconnect();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
//	TODO: Completarlo si interesa
//	@Test
//	public void publishTopicsFromXMLTVFileTest() {
//		Resource resource = ctx.getResource("/xmltv/xmltv_sample.xml");
//		List<Event_old> lEvt = null;
//		/****************************************************************************
//		 * Transformar el fichero XMLTV en una lista de Events con el XMLTVTransformer
//		 ****************************************************************************/
//		try {
//			URI uri = resource.getURI();
//			File file = new File(uri);
//			Message<?> fileMsg = MessageBuilder.
//					withPayload(file).build();
//			Message<?> resultMsg = transformer.transform(fileMsg);
//			assertNotNull(resultMsg);
//			
//			// Construir resultado esperado
//			Date start1, start2, end1, end2;
//			start1 = CommonUtility.getFutureRandomDate(CommonUtility.getTimeUnit("min"), 2, 10); 
//			end1 = CommonUtility.sumTwoHours(start1);
//			start2 = CommonUtility.getFutureRandomDate(CommonUtility.getTimeUnit("min"), 12, 15);
//			end2 = CommonUtility.sumTwoHours(start2);
//			
//			lEvt = new ArrayList<Event_old>();
//			// evt1
//			Event_old evt1 = new Event_old(); Event_old evt2 = new Event_old();
//			evt1.setChannel("neox-722.laguiatv.com");
//			evt1.setTitle("Cómo conocí a vuestra Madre");
////			evt1.setStart(utils.strToDate("20150316173500 +0100"));
////			evt1.setEnd(utils.strToDate("20150316175400 +0100"));
//			evt1.setStart(start1); evt1.setEnd(end1);
//			// evt2
//			evt2.setChannel("neox-722.laguiatv.com");
//			evt2.setTitle("Los Simpson");
////			evt2.setStart(utils.strToDate("20150316214500 +0100"));
////			evt2.setEnd(utils.strToDate("20150316220000 +0100"));
//			evt2.setStart(start2); evt2.setEnd(end2);
//			lEvt.add(evt1); lEvt.add(evt2);
//			
//			@SuppressWarnings("unchecked")
//			List<Event_old> recEvList = (List<Event_old>)resultMsg.getPayload();
//			assertEquals(lEvt.size(), recEvList.size());
//			recEvList.get(0).setStart(start1); recEvList.get(0).setEnd(end1);
//			recEvList.get(1).setStart(start2); recEvList.get(1).setEnd(end2);
//			assertEquals(lEvt, recEvList);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		/****************************************************************************
//		 * Insertar la lista de Events en la BD y comprobar que se ha insertado
//		 ****************************************************************************/
//		evService.createMultipleEvents(lEvt);
//		List<Event_old> listFoundEvt = evService.getAllEvents();
//		Assert.assertNotNull("Expected a non null instance of List<Evento>, got null", listFoundEvt);
//		boolean found = false;
//		for (Event_old eIn: lEvt) { // Para cada evento creado
//			found = false;
//			for (Event_old eOut: listFoundEvt) { // Para cada evento encontrado
//				if (eIn.equals(eOut)) {
//					found = true;
//					break;
//				}
//			}
//			Assert.assertEquals(true, found); // Comprueba que eIn ha sido encontrado
//		}
//		
//		
//	}
}
