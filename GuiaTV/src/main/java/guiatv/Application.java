package guiatv;


import guiatv.conf.mvc.WebConfig;

import org.opencv.core.Core;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.web.WebAppConfiguration;

	
//@ImportResource({"classpath:/META-INF/spring/integration/spring-integration-context.xml",
//	"classpath:/WEB-INF/spring/appServlet/servlet-context.xml"})
@ImportResource("classpath:/META-INF/spring/integration/spring-integration-context.xml")
@ComponentScan(basePackageClasses = { WebConfig.class })
@Configuration
@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Cargar libreria de OpenCV
        SpringApplication.run(Application.class, args);
    }
}


