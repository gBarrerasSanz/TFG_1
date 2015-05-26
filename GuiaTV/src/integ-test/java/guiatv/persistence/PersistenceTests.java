package guiatv.persistence;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import guiatv.Application;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ScheduleRepository;
import guiatv.schedule.utils.ListScheduleCreator;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("PersistenceTests")
public class PersistenceTests extends AbstractTransactionalJUnit4SpringContextTests {
	
	private static Logger logger = Logger.getLogger("debugLog");
	
	@Autowired
	ScheduleRepository schedRep;
	
	@Test
	public void test() {
		try {
			// Cargar valores de prueba en la base de datos
			List<Schedule> listSchedExpected = ListScheduleCreator.getListSchedule();
			schedRep.save(listSchedExpected);
			
			List<Schedule> listSchedRetrieved = schedRep.findAll();
			Assert.assertEquals(listSchedExpected, listSchedRetrieved);
		} catch(Exception e) {
			e.printStackTrace();
			fail();
		}

		
		
	}

}
