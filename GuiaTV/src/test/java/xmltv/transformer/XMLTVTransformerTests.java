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

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import xmltv.datatypes.EventoService;

public class XMLTVTransformerTests {

	@Test
	public void transformTest() {
		final ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:/META-INF/spring/integration/spring-integration-context.xml");
		Resource resource = context.getResource("/META-INF/test/xmltv/xmltv_sample.xml");
		final XMLTVTransformer transformer = context.getBean(XMLTVTransformer.class);
		File file = null;
		try {
			file = resource.getFile();
			Message<?> fileMsg = MessageBuilder.withPayload(getStringFromFile(file)).build();
			Message<?> resultMsg = transformer.transform(fileMsg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getStringFromFile(File f) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(f));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			br.close();
			return sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
