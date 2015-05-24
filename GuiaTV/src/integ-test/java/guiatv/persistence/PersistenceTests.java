package guiatv.persistence;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import guiatv.Application;
import guiatv.domain.Channel;
import guiatv.domain.Programme;
import guiatv.domain.Schedule;
import guiatv.persistence.repository.ScheduleRepository;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@DirtiesContext
public class PersistenceTests {
	
	private static Logger logger = Logger.getLogger("debugLog");
	
	@Autowired
	ScheduleRepository schedRep;
	
	@Test
	public void test() {
		Channel ch1 = new Channel();
		Channel ch2 = new Channel();
		Programme prog1 = new Programme();
		Programme prog2 = new Programme();
		ch1.setNomIdCh("fdf-123"); ch1.setCountry("spain");
		ch2.setNomIdCh("neox-123"); ch1.setCountry("spain");
		prog1.setNomProg("aida");
		prog2.setNomProg("Los Simpsons");
		
		Schedule sched1 = new Schedule();
		sched1.setChannel(ch1);
		sched1.setProgramme(prog1);
		sched1.setStart(new Date()); sched1.setEnd(new Date());
		
		Schedule sched2 = new Schedule();
		sched2.setChannel(ch2);
		sched2.setProgramme(prog2);
		sched2.setStart(new Date()); sched2.setEnd(new Date());
		
		schedRep.save(sched1);
		schedRep.save(sched2);

		// fetch all customers
		List<Schedule> schedList = schedRep.findAll();
		System.out.println("Customers found with findAll(): size="+schedList.size());
		System.out.println("-------------------------------");
		for (Schedule sched : schedList) {
			logger.info(sched);
		}
		
	}

}
