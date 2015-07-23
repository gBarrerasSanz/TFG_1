package guiatv.conf.persistence;

import guiatv.computervision.CvUtils;
import guiatv.persistence.domain.StreamSource;
import guiatv.realtime.rtmpspying.MutexMonitor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DatabaseFillerOnStartup implements ApplicationListener<ContextRefreshedEvent> {
    
	private static final Logger logger = Logger.getLogger("debugLog"); 
	
	
	@Value("${streamsources.channels}")
	private String[] channels;
	
	@Value("${streamsources.urls}")
	private String[] urls;
	
	@Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
    	// Crear un objeto MLChannel para cada canal y meterlo en el repositorio
		
//		for (int i=0; i<channels.length; i++) {
//			StreamSource streamSource = new StreamSource(urls[i]);
//			
//			
//			logger.debug("Created MLChannel channels["+i+"] = "+channels[i]);
//		}
    }
}
