package guiatv.computervision;

import static org.junit.Assert.*;
import guiatv.Application;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@DirtiesContext
public class OpenCVTests {

	@Autowired
	private ApplicationContext ctx;

	@Test
	public void test() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Resource resource = ctx.getResource("/guiatv/xmltv/xmltv_sample.xml");
		Resource programmesDir = ctx
				.getResource("/guiatv/opencv/program_samples/laSexta");
		Resource tptRes = ctx
				.getResource("/guiatv/opencv/template_samples/laSexta/tpt1.png");
		Resource advertisementRes = ctx
				.getResource("/guiatv/opencv/advertisement_samples/laSexta/pub1.png");
//		assertTrue(programmesDir.exists());
//		assertTrue(tptRes.exists());
//		assertTrue(advertisementRes.exists());
		URI programmesUri, tptUri, advertisementUri;
		try {
			programmesUri = programmesDir.getURI();
			tptUri = tptRes.getURI();
			advertisementUri = advertisementRes.getURI();
			
			File dirSamples = new File(programmesUri);
			File tptFile = new File(tptUri);
			File advertisementFile = new File(advertisementUri);

			File[] listOfFiles = dirSamples.listFiles();
			Mat img = Highgui.imread(listOfFiles[0].getAbsolutePath());
	        Mat tpt = Highgui.imread(tptFile.getAbsolutePath());
	        
	        Classif classif = new TemplateMatchingClassif(tpt);
	        classif.learn();
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
