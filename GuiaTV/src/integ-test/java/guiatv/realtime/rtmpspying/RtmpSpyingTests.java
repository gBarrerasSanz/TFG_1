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

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
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
import guiatv.persistence.domain.Event_old;
import guiatv.persistence.domain.Schedule;
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
	private ApplicationContext ctx;
	
    @Autowired
    private MessageHandler rtmpSpyingOutputHandler; 
	
    String[] rtmpSources = {
    	"rtmp://antena3fms35livefs.fplive.net/antena3fms35live-live/stream-antena3_1",
    	"rtmp://antena3fms35livefs.fplive.net:1935/antena3fms35live-live/stream-lasexta_1"
    };
    
	@Test
	public void rtmpSpyingTest() {
		try {
			
			for (String rtmpSource: rtmpSources) {
				rtmpSpyingTaskExecutor.execute(ctx.getBean(RtmpSpyingTask.class, rtmpSource));
			}
	        Mockito.verify(rtmpSpyingOutputHandler).handleMessage(Mockito.any(Message.class));
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
