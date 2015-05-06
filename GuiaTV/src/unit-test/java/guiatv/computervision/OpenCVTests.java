package guiatv.computervision;

import static org.junit.Assert.*;
import guiatv.Application;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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

public class OpenCVTests {


	@Test
	public void test() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		URL programmesDirUrl = this.getClass().getClassLoader().getResource("guiatv.opencv.program_samples.laSexta");
		URL tptUrl = this.getClass().getClassLoader().getResource("guiatv.opencv.template_samples.laSexta/tpt1.png");
		URL advertsUrl = this.getClass().getClassLoader().getResource("guiatv.opencv.advertisement_samples.laSexta/pub1.png");

		URI programmesUri, tptUri, advertisementUri;
		try {
			File programmesDir = new File(programmesDirUrl.toURI());
			File tptFile = new File(tptUrl.toURI());
			File advertsFile = new File(advertsUrl.toURI());
			
			assertTrue(programmesDir.exists());
			assertTrue(tptFile.exists());
			assertTrue(advertsFile.exists());
			
			File[] listOfFiles = programmesDir.listFiles();
			Mat img = Highgui.imread(listOfFiles[0].getAbsolutePath());
	        Mat tpt = Highgui.imread(tptFile.getAbsolutePath());
	        
	        Classif classif = new TemplateMatchingClassif(tpt);
	        classif.learn();
	        
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URISyntaxException");
		}
	}

}
