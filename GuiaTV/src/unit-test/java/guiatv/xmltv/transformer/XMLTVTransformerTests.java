package guiatv.xmltv.transformer;

import static org.junit.Assert.*;
import guiatv.Application;
import guiatv.common.CommonUtility;
import guiatv.domain.Channel;
import guiatv.domain.Event;
import guiatv.domain.Programme;
import guiatv.domain.Schedule;
import guiatv.xmltv.transformer.XMLTVTransformer_old1;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class XMLTVTransformerTests {	
	
	@Autowired
	ApplicationContext ctx;
	
	@Test
	public void transformTest() {
		URL url = this.getClass().getClassLoader().getResource("guiatv.xmltv/xmltv_sample.xml");
		try {
			File file = new File(url.toURI());
			Message<?> fileMsg = MessageBuilder.
					withPayload(file).build();
			
			XMLTVTransformer transformer  = new XMLTVTransformer();
			Message<List<Schedule>> msgListScheduleReturned = transformer.transform(fileMsg);
			assertNotNull(msgListScheduleReturned);
			
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
			
			List<Schedule> listScheduleReturned = msgListScheduleReturned.getPayload();
			for (int i=0; i<listScheduleExpected.size(); i++) {
				Schedule schedReturned = listScheduleReturned.get(i);
				Schedule schedExpected = listScheduleReturned.get(i);
				assertEquals(schedExpected.getProgramme(), schedReturned.getProgramme());
				assertEquals(schedExpected.getChannel(), schedReturned.getChannel());
				assertEquals(schedExpected.getStart(), schedReturned.getStart());
				assertEquals(schedExpected.getEnd(), schedReturned.getEnd());
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URISyntaxException");
		}
	}

}
