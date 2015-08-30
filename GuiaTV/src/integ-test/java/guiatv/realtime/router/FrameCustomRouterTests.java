package guiatv.realtime.router;

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
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ScheduleRepository;
import guiatv.realtime.servicegateway.CapturedBlobsGateway;
import guiatv.schedule.poller.SchedulePoller;
import guiatv.schedule.publisher.SchedulePublisher;
import guiatv.schedule.publisher.TaskExecutorMQTTClient;
import guiatv.schedule.utils.ListScheduleCreator;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@ActiveProfiles("FrameCustomRouterTests")
@WebIntegrationTest
public class FrameCustomRouterTests extends AbstractTransactionalJUnit4SpringContextTests {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	
	@Autowired
	BlobCustomRouter frameCustomRouter;
	
	@Test
	public void routeClassificationFrameTest() {
		// TODO: 
//			Hacer bien la persistencia de learnedChannel
//			Testear la petición learnedChRepImpl.isTrained
//			Mockear learnedChRep para que learnedChRepImpl.isTrained devuelva true
		
		// Construir frame tal que exista un registro en LearnedChannel lc con:
		// 	lc.channel = frame.getChannel(), lc.rtmpSource == frame.getRtmpSource() y ch.learned == true
//		Frame_OLD frame = new Frame_OLD();
//		assertEquals("classificationChIn", frameCustomRouter.routeBlob(frame));
	}
	
//	@Test
//	public void routeTrainingFrameTest() {
//		// Construir frame tal que NO exista un registro en LearnedChannel lc con:
//		// 	lc.channel = frame.getChannel(), lc.rtmpSource == frame.getRtmpSource() y ch.learned == true
//		//	o en un caso raro, que sí exista pero con ch.learned == false
//		Frame frame = new Frame();
//		assertEquals("traningChIn", frameCustomRouter.processFrame(frame));
//	}
	
	
}
