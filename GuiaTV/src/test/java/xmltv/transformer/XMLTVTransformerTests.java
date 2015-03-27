package xmltv.transformer;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import xmltv.datatypes.Event;
import application.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class XMLTVTransformerTests {
	
	@Autowired
	private ApplicationContext ctx;
	
	// TODO: Solucionar la conversion no segura
	//assertEquals((List<Evento>)resultMsg.getPayload(), lEvt);
	@SuppressWarnings("unchecked")
	@Test
	public void transformTest() {
//		final ApplicationContext context = new ClassPathXmlApplicationContext(
//				"classpath:/META-INF/spring/integration/spring-integration-context.xml");
		Resource resource = ctx.getResource("/META-INF/test/xmltv/xmltv_sample.xml");
		final XMLTVTransformer transformer = ctx.getBean(XMLTVTransformer.class);
		try {
			URI uri = resource.getURI();
			File file = new File(uri);
			Message<?> fileMsg = MessageBuilder.
					withPayload(file).build();
			Message<?> resultMsg = transformer.transform(fileMsg);
			assertNotNull(resultMsg);
			
			// Construir resultado esperado
			List<Event> lEvt = new ArrayList<Event>();
			// evt1
			Event evt1 = new Event(); Event evt2 = new Event();
			evt1.setChannel("neox-722.laguiatv.com");
			evt1.setTitle("Cómo conocí a vuestra Madre");
			evt1.setStart(strToDate("20150316173500 +0100"));
			evt1.setEnd(strToDate("20150316175400 +0100"));
			// evt2
			evt2.setChannel("neox-722.laguiatv.com");
			evt2.setTitle("Los Simpson");
			evt2.setStart(strToDate("20150316214500 +0100"));
			evt2.setEnd(strToDate("20150316220000 +0100"));
			lEvt.add(evt1); lEvt.add(evt2);
			assertEquals((List<Event>)resultMsg.getPayload(), lEvt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Método duplicado de xmltv.transformer.XMLTVTransformer.strToDate()
	 */
	private static Date strToDate(String str) {
		final Locale SPAIN_LOCALE = new Locale("es","ES");
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss Z", SPAIN_LOCALE);
		try {
			return format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

}
