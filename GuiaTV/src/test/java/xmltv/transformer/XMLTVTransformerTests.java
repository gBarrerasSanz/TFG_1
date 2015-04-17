package xmltv.transformer;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
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

import common.CommonUtility;
import xmltv.datatypes.Event;
import application.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@DirtiesContext
public class XMLTVTransformerTests {
	
	@Autowired
	private ApplicationContext ctx;
	
	@Autowired
	private XMLTVTransformer transformer;
	
	@Autowired
	private CommonUtility utils;

	@Test
	public void transformTest() {
		Resource resource = ctx.getResource("/xmltv/xmltv_sample.xml");
		try {
			URI uri = resource.getURI();
			File file = new File(uri);
			Message<?> fileMsg = MessageBuilder.
					withPayload(file).build();
			Message<List<Event>> resultMsg = transformer.transform(fileMsg);
			assertNotNull(resultMsg);
			
			// Construir resultado esperado
			List<Event> lEvt = new ArrayList<Event>();
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
			assertEquals(resultMsg.getPayload(), lEvt);
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
	}

}
