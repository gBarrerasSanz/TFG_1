/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import common.CommonUtility;

import eventmanager.EventService;
import application.Application;
import xmltv.datatypes.Event;
import xmltv.transformer.XMLTVTransformer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class EventServiceTests {

	private static final Logger LOGGER = Logger.getLogger(EventServiceTests.class);
	
	@Autowired
	private ApplicationContext ctx;
	
	@Autowired
	private EventService evService;
	
	@Autowired
	private XMLTVTransformer transformer;
	
	@Autowired
	private CommonUtility utils;
	
	@Before
	public void doNothing() {
		LOGGER.info("No hacer nada");
	}
	
	@Test
	public void createMultipleEventsTest() {
		final int NUM_EVENTOS = 10;
		ArrayList<Event> lEvt = new ArrayList<Event>(NUM_EVENTOS);
		for (int i=0; i<NUM_EVENTOS; i++) {
			Event evt = new Event();
			evt.setChannel("Canal createMultipleEventosTest "+i);
			evt.setTitle("Programa createMultipleEventosTest "+i);
			Calendar cal = Calendar.getInstance(); // creates calendar
		    cal.setTime(new Date()); // sets calendar time/date
		    cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
		    Date start = cal.getTime(); // returns new date object, one hour in the future
		    cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
		    Date end = cal.getTime();
		    evt.setStart(start);
		    evt.setEnd(end);
		    Assert.assertEquals(true, evt.checkInitValues());
			lEvt.add(evt);
		}
		
		final List<Event> listCreatedEvt = evService.createMultipleEvents(lEvt);
		Assert.assertNotNull("Expected a non null instance of List<Evento>, got null", listCreatedEvt);
		final List<Event> listEventoResult = evService.getAllEvents();
		boolean found = false;
		for (Event eIn: lEvt) {
			found = false;
			for (Event eOut: listEventoResult) {
				if (eIn.equals(eOut)) {
					found = true;
					break;
				}
			}
			Assert.assertEquals(true, found);
		}
		Assert.assertEquals(true, found);
	}
	
	@Test
	public void createEventsFromXMLTVFileTest() {
		Resource resource = ctx.getResource("/META-INF/test/xmltv/xmltv_sample.xml");
		List<Event> lEvt = null;
		/****************************************************************************
		 * Transformar el fichero XMLTV en una lista de Events con el XMLTVTransformer
		 ****************************************************************************/
		try {
			URI uri = resource.getURI();
			File file = new File(uri);
			Message<?> fileMsg = MessageBuilder.
					withPayload(file).build();
			Message<?> resultMsg = transformer.transform(fileMsg);
			assertNotNull(resultMsg);
			
			// Construir resultado esperado
			lEvt = new ArrayList<Event>();
			// evt1
			Event evt1 = new Event(); Event evt2 = new Event();
			evt1.setChannel("neox-722.laguiatv.com");
			evt1.setTitle("Cómo conocí a vuestra Madre");
			evt1.setStart(utils.strToDate("20150316173500 +0100"));
			evt1.setEnd(utils.strToDate("20150316175400 +0100"));
			// evt2
			evt2.setChannel("neox-722.laguiatv.com");
			evt2.setTitle("Los Simpson");
			evt2.setStart(utils.strToDate("20150316214500 +0100"));
			evt2.setEnd(utils.strToDate("20150316220000 +0100"));
			lEvt.add(evt1); lEvt.add(evt2);
			assertEquals((List<Event>)resultMsg.getPayload(), lEvt);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/****************************************************************************
		 * Insertar la lista de Events en la BD y comprobar que se ha insertado
		 ****************************************************************************/
		
		evService.createMultipleEvents(lEvt);
		List<Event> listFoundEvt = evService.getAllEvents();
		Assert.assertNotNull("Expected a non null instance of List<Evento>, got null", listFoundEvt);
		boolean found = false;
		for (Event eIn: lEvt) { // Para cada evento creado
			found = false;
			for (Event eOut: listFoundEvt) { // Para cada evento encontrado
				if (eIn.equals(eOut)) {
					found = true;
					break;
				}
			}
			Assert.assertEquals(true, found); // Comprueba que eIn ha sido encontrado
		}
	}
	
	
}