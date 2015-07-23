package guiatv.realtime.servicegateway;

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
import org.springframework.boot.test.WebIntegrationTest;
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
import guiatv.persistence.domain.MLChannel;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ScheduleRepository;
import guiatv.realtime.servicegateway.CapturedFramesGateway;
import guiatv.schedule.poller.SchedulePoller;
import guiatv.schedule.publisher.SchedulePublisher;
import guiatv.schedule.publisher.TaskExecutorMQTTClient;
import guiatv.schedule.utils.ListScheduleCreator;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@ActiveProfiles("CapturedFramesGatewayTests")
@WebIntegrationTest
public class CapturedFramesGatewayTests extends AbstractTransactionalJUnit4SpringContextTests {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	@Autowired
	private CapturedFramesGateway capturedFramesGateway;
	
	@Autowired
	QueueChannel sendFrameChOut;
	
    
	@Test
	public void rtmpSpyingTest() {
		try {
			byte[] payload = new byte[1024]; 
			Channel ch1 = new Channel("someTestCh1");
//			RtmpSource rtmpSource = new RtmpSource(ch1, "someTestUrl1");
			Frame frameExpected = new Frame(payload, new MLChannel(), new Date());
			capturedFramesGateway.sendFrame(frameExpected);
			
			Message<?> inMessage = sendFrameChOut.receive(100);
			assertNotNull("Expected a message", inMessage);
			Frame frameActual = (Frame) inMessage.getPayload();
			assertEquals(frameExpected, frameActual);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
