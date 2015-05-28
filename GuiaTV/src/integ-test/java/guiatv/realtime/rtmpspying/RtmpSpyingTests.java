package guiatv.realtime.rtmpspying;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import guiatv.Application;
import guiatv.ApplicationTest;
import guiatv.common.CommonUtility;
import guiatv.common.datatypes.Frame;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Event_old;
import guiatv.persistence.domain.RtmpSource;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.AsyncTransactionService;
import guiatv.persistence.repository.LearnedRtmpSourceRepository;
import guiatv.persistence.repository.RtmpSourceRepository;
import guiatv.persistence.repository.ScheduleRepository;
import guiatv.realtime.servicegateway.CapturedFramesGateway;
import guiatv.schedule.poller.SchedulePoller;
import guiatv.schedule.publisher.SchedulePublisher;
import guiatv.schedule.publisher.TaskExecutorMQTTClient;
import guiatv.schedule.utils.ListScheduleCreator;
import guiatv.xmltv.transformer.XMLTVTransformer_old1;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@ActiveProfiles("RtmpSpyingTests")
public class RtmpSpyingTests extends AbstractTransactionalJUnit4SpringContextTests {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	@Autowired
	private ThreadPoolTaskExecutor rtmpSpyingTaskExecutor;
	
	@Autowired
	ApplicationContext ctx;
	
    @Autowired
    CapturedFramesGateway capturedFramesGateway;
    
    @Autowired
    RtmpSourceRepository rtmpRep;
    
    @Autowired
    QueueChannel sendFrameChOut;
    
    String[] nameIdChannels = {
    	"nameIdChTest_a3",
    	"nameIdChTest_laSexta"
    };
    
    String[] rtmpSources = {
    	"rtmp://antena3fms35livefs.fplive.net/antena3fms35live-live/stream-antena3_1",
    	"rtmp://antena3fms35livefs.fplive.net:1935/antena3fms35live-live/stream-lasexta_1"
    };
    
	@Test(timeout = 120000)
	public void rtmpSpyingTest() {
		try {
			// Crear dos RtmpSources (uno para Antena3 y otro para laSexta)
			Channel chA3 = new Channel(nameIdChannels[0]);
			RtmpSource rtmpSourceA3 = new RtmpSource(chA3, rtmpSources[0]);
			Channel chLa6 = new Channel(nameIdChannels[1]);
			RtmpSource rtmpSourceLa6 = new RtmpSource(chLa6, rtmpSources[1]);
			
			
			// Meterlos en la base de datos para que los pueda leer el RtmpSpyingTask
			rtmpRep.save(rtmpSourceA3);
			rtmpRep.save(rtmpSourceLa6);
			rtmpRep.flush();
			
			List<RtmpSource> ListrtmpSource = rtmpRep.findAll();
			
			for (int i=0; i<rtmpSources.length; i++) {
				// Ejecutar la task de RtmpSpyingTask
				RtmpSpyingTask rtmpSpyingTask = ctx.getBean(RtmpSpyingTask.class, 
						nameIdChannels[i],rtmpSources[i]);
				rtmpSpyingTaskExecutor.execute(rtmpSpyingTask);
			}
			
			Message<?> inMessage;
			int numRcvMsg = 0;
			while (numRcvMsg < 2) {
				inMessage = sendFrameChOut.receive();
				assertNotNull("Expected a message", inMessage);
				Frame frameActual = (Frame) inMessage.getPayload();
				assertNotNull(frameActual);
			}
			
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
