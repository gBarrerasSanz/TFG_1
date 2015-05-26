package guiatv.computervision.rtmpspy;

import static org.junit.Assert.*;
import guiatv.Application;
import guiatv.common.CommonUtility;
import guiatv.cv.classificator.Imshow;
import guiatv.xmltv.grabber.XMLTVGrabber;

import java.io.File;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class OpenCVWorkingTests {
	
//	@Test
	public void spyingTest() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Imshow im = new Imshow("Test Image");
		Mat testMat = new Mat(512, 512, CvType.CV_8UC1);
		im.showImage(testMat);
	}
}
