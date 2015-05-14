package guiatv.persistence;

import static org.junit.Assert.*;

import java.util.Date;

import guiatv.Application;
import guiatv.domain.Channel;
import guiatv.domain.Programme;
import guiatv.domain.Schedule;

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
		ch1.setIdCh("fdf"); ch1.setCountry("spain");
		ch1.setIdCh("neox"); ch1.setCountry("spain");
		prog1.setNomProg("aida");
		prog1.setNomProg("Los Simpsons");
		
		Schedule sched1 = new Schedule();
		sched1.setIdCh(ch1.getIdCh());
		sched1.setIdProg(prog1.getIdProg());
		sched1.setStart(new Date()); sched1.setEnd(new Date());
		
		Schedule sched2 = new Schedule();
		sched1.setIdCh(ch2.getIdCh());
		sched1.setIdProg(prog2.getIdProg());
		sched2.setStart(new Date()); sched2.setEnd(new Date());
		
		schedRep.save(sched1);
		schedRep.save(sched2);

		// fetch all customers
		System.out.println("Customers found with findAll():");
		System.out.println("-------------------------------");
		for (Schedule sched : schedRep.findAll()) {
			logger.info(sched);
		}
		
	}

}
