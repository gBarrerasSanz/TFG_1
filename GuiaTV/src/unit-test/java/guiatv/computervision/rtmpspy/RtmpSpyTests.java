package guiatv.computervision.rtmpspy;

import static org.junit.Assert.*;
import guiatv.Application;
import guiatv.common.CommonUtility;
import guiatv.computervision.rtmpspy.RtmpSpyBeta;
import guiatv.xmltv.grabber.XMLTVGrabber;

import java.io.File;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class RtmpSpyTests {
	
//	@Test
	public void spyingTest() {
		RtmpSpyBeta spy = new RtmpSpyBeta();
		spy.doRtmpSpying();
//		assertEquals(true, spyFile.canRead());
	}
}
