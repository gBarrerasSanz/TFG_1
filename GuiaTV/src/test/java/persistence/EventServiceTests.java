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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eventmanager.EventService;
import application.Application;
import xmltv.datatypes.Event;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class EventServiceTests {

	private static final Logger LOGGER = Logger.getLogger(EventServiceTests.class);
	
	@Autowired
	private EventService evService;
	
	@Before
	public void doNothing() {
		LOGGER.info("No hacer nada");
	}
	
	@Test
	public void createMultipleEventosTest() {
//		final ApplicationContext context = new ClassPathXmlApplicationContext(
//				"classpath:/META-INF/spring/integration/spring-integration-context.xml");

//		final EventService service = ctx.getBean(EventService.class);
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
		final List<Event> listEventoResult = evService.findEvent();
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
}