package guiatv.xmltv.transformer;

import static org.junit.Assert.*;
import guiatv.Application;
import guiatv.common.CommonUtility;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;
import guiatv.schedule.utils.ListScheduleCreator;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
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
	
	@Test
	public void transformTest() {
		URL url = this.getClass().getClassLoader().getResource("guiatv.xmltv/xmltv_sample.xml");
		try {
			// Construir resultado esperado
			List<Schedule> listSchedExpected = ListScheduleCreator.getListSchedule();
			
			// Pasarle el fichero con los mismos datos
			File file = new File(url.toURI());
			Message<?> fileMsg = MessageBuilder.
					withPayload(file).build();
			
			XMLTVTransformer transformer  = new XMLTVTransformer();
			Message<List<Schedule>> msgListScheduleReturned = transformer.transform(fileMsg);
			assertNotNull(msgListScheduleReturned);
			
			List<Schedule> listSchedReturned = msgListScheduleReturned.getPayload();
			
			Assert.assertEquals(listSchedExpected, listSchedReturned);
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URISyntaxException");
		}
	}

}
