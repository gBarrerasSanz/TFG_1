package application;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

	
@EnableAutoConfiguration
@ComponentScan
@Configuration
public class Application {
	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

@Configuration
@ImportResource("classpath:/META-INF/spring/integration/spring-integration-context.xml")
class XmlImportingConfiguration {
}
