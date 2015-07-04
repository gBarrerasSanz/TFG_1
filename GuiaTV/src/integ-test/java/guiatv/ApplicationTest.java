package guiatv;


import guiatv.conf.mvc.WebConfig;
import guiatv.conf.mvc.WebConfig;

import org.opencv.core.Core;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

	

@SpringBootApplication 
@ImportResource("classpath:/META-INF/spring/integration/spring-integration-context.xml")
public class ApplicationTest {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Cargar libreria de OpenCV
        SpringApplication.run(ApplicationTest.class, args);
    }
}


