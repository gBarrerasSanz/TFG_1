package guiatv.computervision.rtmpspy;

import static org.junit.Assert.*;
import guiatv.Application;
import guiatv.common.CommonUtility;
import guiatv.xmltv.grabber.XMLTVGrabber;

import java.io.File;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@DirtiesContext
public class RtmpSpyTests {
	
	@Test
	public void spyingTest() {
		RtmpSpy spy = new RtmpSpy();
		spy.doRtmpSpying();
//		assertEquals(true, spyFile.canRead());
	}
}
