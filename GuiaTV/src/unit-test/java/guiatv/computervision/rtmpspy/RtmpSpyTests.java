package guiatv.computervision.rtmpspy;

import static org.junit.Assert.*;
import guiatv.common.CommonUtility;
import guiatv.xmltv.grabber.XMLTVGrabber;

import java.io.File;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class RtmpSpyTests {
	
	@Test
	public void spyingTest() {
		RtmpSpy spy = new RtmpSpy();
		File spyFile = spy.doRtmpSpying();
		assertEquals(true, spyFile.canRead());
		
	}
}
