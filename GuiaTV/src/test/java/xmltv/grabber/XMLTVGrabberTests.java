package xmltv.grabber;

import static org.junit.Assert.*;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import application.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class XMLTVGrabberTests {
	
	@Autowired
	private ApplicationContext ctx;
	
	@Test
	public void grabbingTest() {
		final XMLTVGrabber grabber = ctx.getBean(XMLTVGrabber.class);
		Date realDate = new Date();
		File grabFile = grabber.doGrabbing();
		assertEquals(true, grabFile.canRead());
		assertEquals(true, checkFileNameDate(grabFile.getName(), realDate));
	}

	private boolean checkFileNameDate(String grabFileName, Date realDate) {
			final Locale SPAIN_LOCALE = new Locale("es", "ES");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", SPAIN_LOCALE);
			String dateStr = grabFileName.substring(
					grabFileName.indexOf("_")+1, grabFileName.indexOf("."));
			try {
				Date readDate =  formatter.parse(dateStr);
				// SI la diferencia es como mucho 2 minutos
				if (realDate.getTime() - readDate.getTime() <= 2*(60*1000)) {
					return true;
				}
				else { return false; }
			} catch (ParseException e) {
				e.printStackTrace();
				return false;
			}
	}
}
