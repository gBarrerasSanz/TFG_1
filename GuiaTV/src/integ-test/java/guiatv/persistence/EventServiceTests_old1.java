//package guiatv.persistence;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.context.ApplicationContext;
//import org.springframework.core.io.Resource;
//import org.springframework.integration.support.MessageBuilder;
//import org.springframework.messaging.Message;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import guiatv.Application;
//import guiatv.common.CommonUtility;
//import guiatv.eventmanager.ImgProcessingGateway;
//import guiatv.persistence.domain.Event_old;
//import guiatv.xmltv.transformer.XMLTVTransformer_old1;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
//@DirtiesContext
//public class EventServiceTests_old1 {
//
//	private static final Logger LOGGER = Logger.getLogger(EventServiceTests_old1.class);
//	
//	@Autowired
//	private ApplicationContext ctx;
//	
//	@Autowired
//	private ImgProcessingGateway evService;
//	
//	@Autowired
//	private XMLTVTransformer_old1 transformer;
//	
//	@Before
//	public void doNothing() {
//		LOGGER.info("No hacer nada");
//	}
//	
//	@Test
//	public void createMultipleEventsTest() {
//		final int NUM_EVENTOS = 10;
//		ArrayList<Event_old> lEvt = new ArrayList<Event_old>(NUM_EVENTOS);
//		for (int i=0; i<NUM_EVENTOS; i++) {
//			Event_old evt = new Event_old();
//			evt.setChannel("Canal createMultipleEventosTest "+i);
//			evt.setTitle("Programa createMultipleEventosTest "+i);
//			Calendar cal = Calendar.getInstance(); // creates calendar
//		    cal.setTime(new Date()); // sets calendar time/date
//		    cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
//		    Date start = cal.getTime(); // returns new date object, one hour in the future
//		    cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
//		    Date end = cal.getTime();
//		    evt.setStart(start);
//		    evt.setEnd(end);
//		    Assert.assertEquals(true, evt.checkInitValues());
//			lEvt.add(evt);
//		}
//		
//		final List<Event_old> listCreatedEvt = evService.createMultipleEvents(lEvt);
//		Assert.assertNotNull("Expected a non null instance of List<Evento>, got null", listCreatedEvt);
//		final List<Event_old> listEventoResult = evService.getAllEvents();
//		boolean found = false;
//		for (Event_old eIn: lEvt) {
//			found = false;
//			for (Event_old eOut: listEventoResult) {
//				if (eIn.equals(eOut)) {
//					found = true;
//					break;
//				}
//			}
//			Assert.assertEquals(true, found);
//		}
//		Assert.assertEquals(true, found);
//	}
//	
//	@Test
//	public void createEventsFromXMLTVFileTest() {
//		Resource resource = ctx.getResource("guiatv.xmltv/xmltv_sample.xml");
//		assertTrue(resource.exists());
//		List<Event_old> lEvt = null;
//		/****************************************************************************
//		 * Transformar el fichero XMLTV en una lista de Events con el XMLTVTransformer
//		 ****************************************************************************/
//		try {
//			URI uri = resource.getURI();
//			File file = new File(uri);
//			Message<?> fileMsg = MessageBuilder.
//					withPayload(file).build();
//			Message<List<Event_old>> resultMsg = transformer.transform(fileMsg);
//			assertNotNull(resultMsg);
//			
//			// Construir resultado esperado
//			lEvt = new ArrayList<Event_old>();
//			// evt1
//			Event_old evt1 = new Event_old(); Event_old evt2 = new Event_old();
//			evt1.setChannel("neox-722.laguiatv.com");
//			evt1.setTitle("C�mo conoc� a vuestra Madre");
//			evt1.setStart(CommonUtility.strToDate("20150316173500 +0100"));
//			evt1.setEnd(CommonUtility.strToDate("20150316175400 +0100"));
//			// evt2
//			evt2.setChannel("neox-722.laguiatv.com");
//			evt2.setTitle("Los Simpson");
//			evt2.setStart(CommonUtility.strToDate("20150316214500 +0100"));
//			evt2.setEnd(CommonUtility.strToDate("20150316220000 +0100"));
//			lEvt.add(evt1); lEvt.add(evt2);
//			assertEquals(resultMsg.getPayload(), lEvt);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		/****************************************************************************
//		 * Insertar la lista de Events en la BD y comprobar que se ha insertado
//		 ****************************************************************************/
//		
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
//	}
//	
//	
//}