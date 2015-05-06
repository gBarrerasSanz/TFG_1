package guiatv.xmltv.transformer;

import static org.junit.Assert.*;
import guiatv.Application;
import guiatv.common.CommonUtility;
import guiatv.xmltv.datatypes.Event;
import guiatv.xmltv.transformer.XMLTVTransformer;

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
import org.springframework.core.io.Resource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class XMLTVTransformerTests {	
	@Test
	public void transformTest() {
		URL url = this.getClass().getResource("guiatv.xmltv/xmltv_sample.xml");
		try {
			File file = new File(url.toURI());
			Message<?> fileMsg = MessageBuilder.
					withPayload(file).build();
			
			XMLTVTransformer transformer  = new XMLTVTransformer();
			Message<List<Event>> resultMsg = transformer.transform(fileMsg);
			assertNotNull(resultMsg);
			
			// Construir resultado esperado
			List<Event> lEvt = new ArrayList<Event>();
			// evt1
			Event evt1 = new Event(); Event evt2 = new Event();
			evt1.setChannel("neox-722.laguiatv.com");
			evt1.setTitle("Cómo conocí a vuestra Madre");
			evt1.setStart(CommonUtility.strToDate("20150316173500 +0100"));
			evt1.setEnd(CommonUtility.strToDate("20150316175400 +0100"));
			// evt2
			evt2.setChannel("neox-722.laguiatv.com");
			evt2.setTitle("Los Simpson");
			evt2.setStart(CommonUtility.strToDate("20150316214500 +0100"));
			evt2.setEnd(CommonUtility.strToDate("20150316220000 +0100"));
			lEvt.add(evt1); lEvt.add(evt2);
			assertEquals(resultMsg.getPayload(), lEvt);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
	}

}
