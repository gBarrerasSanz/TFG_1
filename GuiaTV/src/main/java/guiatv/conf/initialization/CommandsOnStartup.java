package guiatv.conf.initialization;

import java.nio.charset.Charset;
import java.util.TimeZone;

import guiatv.computervision.CvUtils;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.StreamSource;
import guiatv.persistence.domain.helper.ArffHelper;
import guiatv.persistence.repository.service.ChannelService;
import guiatv.realtime.rtmpspying.RtmpSpyingService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class CommandsOnStartup implements ApplicationListener<ContextRefreshedEvent> {
    
	private static final Logger logger = Logger.getLogger("debugLog"); 
	
	@Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
		System.err.close(); 
		setConfig();
		//		loadAndSpyChannels();
    	// Crear un objeto MLChannel para cada canal y meterlo en el repositorio
		
//		for (int i=0; i<channels.length; i++) {
//			StreamSource streamSource = new StreamSource(urls[i]);
//			
//			
//			logger.debug("Created MLChannel channels["+i+"] = "+channels[i]);
//		}
    }
	
	private void setConfig() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        // cannot override encoding in Spring at runtime as some strings have already been read
        // however, we can assert and ensure right values are loaded here
        // verify system property is set
//        Assert.isTrue("UTF-8".equals(System.getProperty("file.encoding")));
        // and actually verify it is being used
//        Charset charset = Charset.defaultCharset();
//        Assert.isTrue(charset.equals(Charset.forName("UTF-8")));
	}
	
}
