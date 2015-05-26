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
import guiatv.persistence.repository.notused.ChannelRepository;
import guiatv.persistence.repository.notused.ProgrammeRepository;
import guiatv.schedule.utils.ListScheduleCreator;

import org.apache.log4j.Logger;
import org.h2.jdbc.JdbcSQLException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
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
	
	@Autowired
	ChannelRepository chRep;
	
	@Autowired
	ProgrammeRepository progRep;
	
	@Test
	public void simplePersistenceTest() {
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
	
	@Test(expected=DataIntegrityViolationException.class)
	public void channelUniqueConstraintsTest() {
//		business-Id: nameIdCh
		Channel ch1 = new Channel("nameIdCh1");
		Channel ch2 = new Channel("nameIdCh1");
		chRep.save(ch1);
		chRep.save(ch2);
	}
	
	@Test(expected=DataIntegrityViolationException.class)
	public void programmeUniqueConstraintsTest() {
//		business-Id: nameProg
		Programme prog1 = new Programme("nameProg1");
		Programme prog2 = new Programme("nameProg1");
		progRep.save(prog1);
		progRep.save(prog2);
	}
	
	@Test
	public void scheduleUniqueConstraintsTest1() {
//		business-Id: nameProg
		Programme prog1 = new Programme("nameProg1");
		Programme prog2 = new Programme("nameProg2");
		Channel ch1 = new Channel("nameIdCh1");
		Channel ch2 = new Channel("nameIdCh2");
		Timestamp ts1 = new Timestamp(new Date().getTime());
		Timestamp ts2 = new Timestamp(new Date().getTime());
		Schedule sched1 = new Schedule(ch1, prog1, ts1, ts1);
		Schedule sched2 = new Schedule(ch2, prog2, ts2, ts2);
		schedRep.save(sched1);
		schedRep.save(sched2);
	}
	
	@Test(expected=DataIntegrityViolationException.class)
	public void scheduleUniqueConstraintsTest2() {
//		business-Id: nameProg
		Programme prog1 = new Programme("nameProg1");
		Programme prog2 = new Programme("nameProg1"); // El programme es el mismo
		Channel ch1 = new Channel("nameIdCh1");
		Channel ch2 = new Channel("nameIdCh2");
		Timestamp ts1 = new Timestamp(new Date().getTime());
		Timestamp ts2 = new Timestamp(new Date().getTime());
		Schedule sched1 = new Schedule(ch1, prog1, ts1, ts1);
		Schedule sched2 = new Schedule(ch2, prog2, ts2, ts2);
		schedRep.save(sched1);
		schedRep.save(sched2);
	}

}
