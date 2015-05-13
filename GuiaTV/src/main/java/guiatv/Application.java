package guiatv;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

	
//@EnableAutoConfiguration
//@ComponentScan
@ImportResource("classpath:/META-INF/spring/integration/spring-integration-context.xml")
//@Configuration
@SpringBootApplication // TODO: Probar con esto
public class Application {
	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}


