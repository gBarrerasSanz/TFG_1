package xmltv.grabber;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import common.CommonUtility;

import application.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class XMLTVGrabberTests {
	
	@Autowired
	private XMLTVGrabber grabber;
	
	@Autowired
	private CommonUtility utils;
	
	@Test
	public void grabbingTest() {
		Date realDate = new Date();
		File grabFile = grabber.doGrabbing();
		assertEquals(true, grabFile.canRead());
		assertEquals(true, utils.checkFileNameDate(grabFile.getName(), realDate));
		// Borarr fichero generado
		boolean deleted = grabFile.delete();
		assertEquals(true, deleted); // Comprobar que se ha borrado
		assertEquals(false, grabFile.exists()); // Comprobar que no existe
	}
}
