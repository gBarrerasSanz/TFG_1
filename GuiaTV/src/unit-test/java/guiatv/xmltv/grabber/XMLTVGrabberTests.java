package guiatv.xmltv.grabber;

import static org.junit.Assert.*;
import guiatv.common.CommonUtility;
import guiatv.xmltv.grabber.XMLTVGrabber;

import java.io.File;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class XMLTVGrabberTests {
	
	@Test
	public void grabbingTest() {
		Date realDate = new Date();
		XMLTVGrabber grabber = new XMLTVGrabber();
		File grabFile = grabber.doGrabbing();
		assertEquals(true, grabFile.canRead());
		assertEquals(true, CommonUtility.checkFileNameDate(grabFile.getName(), realDate));
		// Borarr fichero generado
		boolean deleted = grabFile.delete();
		assertEquals(true, deleted); // Comprobar que se ha borrado
		assertEquals(false, grabFile.exists()); // Comprobar que no existe
	}
}
