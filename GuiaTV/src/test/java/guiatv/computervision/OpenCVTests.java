package guiatv.computervision;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

public class OpenCVTests {
	
	@Autowired
	private ApplicationContext ctx;
	
	@Test
	public void test() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		Resource resource = ctx.getResource("/guiatv/opencv/laSexta/program_samples/");
		assertTrue(resource.exists());
		URI uri;
		try {
			uri = resource.getURI();
			File dirSamples = new File(uri);
			assertTrue(dirSamples.isDirectory());
			File[] listOfFiles = dirSamples.listFiles();
			for (File file : listOfFiles) {
			    if (file.isFile()) {
			        System.out.println(file.getName());
			    }
			}
			
			Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
			System.out.println("mat = " + mat.dump());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
