package guiatv.conf.initialization;

import guiatv.computervision.CvUtils;
import guiatv.persistence.domain.ArffObject;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.MLChannel;
import guiatv.persistence.domain.StreamSource;
import guiatv.persistence.repository.StreamSourceRepository;
import guiatv.persistence.repository.service.ArffObjectService;
import guiatv.persistence.repository.service.ChannelService;
import guiatv.persistence.repository.service.MLChannelService;
import guiatv.persistence.repository.service.StreamSourceService;
import guiatv.realtime.rtmpspying.MutexMonitor;
import guiatv.realtime.rtmpspying.RtmpSpyingService;
import guiatv.realtime.rtmpspying.serializable.ChannelData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class CommandsOnStartup implements ApplicationListener<ContextRefreshedEvent> {
    
	private static final Logger logger = Logger.getLogger("debugLog"); 
	
	@Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
		
//		loadAndSpyChannels();
    	// Crear un objeto MLChannel para cada canal y meterlo en el repositorio
		
//		for (int i=0; i<channels.length; i++) {
//			StreamSource streamSource = new StreamSource(urls[i]);
//			
//			
//			logger.debug("Created MLChannel channels["+i+"] = "+channels[i]);
//		}
    }
	
}
