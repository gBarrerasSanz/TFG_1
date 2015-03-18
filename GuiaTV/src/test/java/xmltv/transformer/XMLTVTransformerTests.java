package xmltv.transformer;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.apache.commons.jxpath.Container;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.xml.DocumentContainer;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import xmltv.datatypes.Evento;
import xmltv.datatypes.EventoService;

public class XMLTVTransformerTests {

	@Test
	public void transformTest() {
		final ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:/META-INF/spring/integration/spring-integration-context.xml");
		Resource resource = context.getResource("/META-INF/test/xmltv/xmltv_sample2.xml");
		final XMLTVTransformer transformer = context.getBean(XMLTVTransformer.class);
		File file = null;
		try {
			file = resource.getFile();
			URL url = resource.getURL();
//		    URL url = resource.getClassLoader().getResource("student_class.xml");
		    Container container = new DocumentContainer(url);
		    JXPathContext jxpathCtx = JXPathContext.newContext(container);
			Message<?> fileMsg = MessageBuilder.
					withPayload(jxpathCtx).build();
			Message<?> resultMsg = transformer.transform(fileMsg);
			assertNotNull(resultMsg);
			// Construir resultado esperado
			List<Evento> lEvt = new ArrayList<Evento>();
			
			Evento evt1 = new Evento(); Evento evt2 = new Evento();
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
			assertEquals((List<Evento>)resultMsg.getPayload(), lEvt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Método duplicado de XMLTVTransformer.strToDate()
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
