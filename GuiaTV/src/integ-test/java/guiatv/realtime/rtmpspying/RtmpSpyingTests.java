package guiatv.realtime.rtmpspying;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
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
import guiatv.realtime.service.CapturedFramesGateway;
import guiatv.schedule.poller.SchedulePoller;
import guiatv.schedule.publisher.SchedulePublisher;
import guiatv.schedule.publisher.TaskExecutorMQTTClient;
import guiatv.schedule.utils.ListScheduleCreator;
import guiatv.xmltv.transformer.XMLTVTransformer_old1;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@ActiveProfiles("SchedulePollerTests")
public class RtmpSpyingTests extends AbstractTransactionalJUnit4SpringContextTests {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	
	@Autowired
	RtmpSpyingOutDummy rtmpSpyingOutDummy;
	
	@Test
	public void pollForSchedulesTest() {
		try {
			List<Frame> listFramesRecv = new ArrayList<Frame>();
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
