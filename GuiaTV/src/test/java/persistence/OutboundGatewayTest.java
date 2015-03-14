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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import xmltv.datatypes.Evento;
import xmltv.datatypes.EventoService;

public class OutboundGatewayTest {

	private static final Logger LOGGER = Logger.getLogger(OutboundGatewayTest.class);
	
	@Before
	public void doNothing() {
		LOGGER.info("No hacer nada");
	}
	@Test
	public void insertEventoRecord() {
		final ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:/META-INF/spring/integration/spring-integration-context.xml");

		final EventoService service = context.getBean(EventoService.class);
		LOGGER.info("Creating person Instance");
		final Evento evt = new Evento();
		evt.setChannel("Canal 2");
		evt.setTitle("Programa 2");
		Calendar cal = Calendar.getInstance(); // creates calendar
	    cal.setTime(new Date()); // sets calendar time/date
	    cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
	    Date start = cal.getTime(); // returns new date object, one hour in the future
	    cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
	    Date end = cal.getTime();
	    evt.setStart(start);
	    evt.setEnd(end);
	    Assert.assertEquals(true, evt.checkInitValues());
		final Evento persistedEvento = service.createEvento(evt);
		Assert.assertNotNull("Expected a non null instance of Person, got null", persistedEvento);
		LOGGER.info("\n\tGenerated person with id: " + persistedEvento.getId() + "\n" +
				"\tchannel: " + persistedEvento.getChannel()+"\n" +
				"\ttitle: " + persistedEvento.getTitle()+"\n" +
				"\tstart: " + persistedEvento.getStart()+"\n" +
				"\tend: " + persistedEvento.getEnd()+"\n");
	}
	
	@Test
	public void findEventoRecord() {
		final ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:/META-INF/spring/integration/spring-integration-context.xml");

		final EventoService service = context.getBean(EventoService.class);
		LOGGER.info("Creating person Instance");
		final Evento evt = new Evento();
		evt.setChannel("Canal 2");
		evt.setTitle("Programa 2");
		Calendar cal = Calendar.getInstance(); // creates calendar
	    cal.setTime(new Date()); // sets calendar time/date
	    cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
	    Date start = cal.getTime(); // returns new date object, one hour in the future
	    cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
	    Date end = cal.getTime();
	    evt.setStart(start);
	    evt.setEnd(end);
	    Assert.assertEquals(true, evt.checkInitValues());
		final Evento persistedEvento = service.createEvento(evt);
		Assert.assertNotNull("Expected a non null instance of Person, got null", persistedEvento);
		LOGGER.info("\n\tGenerated person with id: " + persistedEvento.getId() + "\n" +
				"\tchannel: " + persistedEvento.getChannel()+"\n" +
				"\ttitle: " + persistedEvento.getTitle()+"\n" +
				"\tstart: " + persistedEvento.getStart()+"\n" +
				"\tend: " + persistedEvento.getEnd()+"\n");
		
		final List<Evento> listEvento = service.findEvento();
		boolean found = false;
		for (Evento e: listEvento) {
			if (e.equals(persistedEvento)) {
				found = true;
			}
		}
		Assert.assertEquals(true, found);
		
	}
}