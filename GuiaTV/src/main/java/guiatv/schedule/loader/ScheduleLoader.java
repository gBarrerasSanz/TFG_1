package guiatv.schedule.loader;

import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ScheduleRepository;
import guiatv.persistence.repository.service.ScheduleService;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.apache.log4j.Logger;
import org.h2.jdbc.JdbcSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional // Es necesario
public class ScheduleLoader {
	private static Logger logger = Logger.getLogger("debugLog");
	
	@Autowired
	ScheduleService schedServ;
	
	public ScheduleLoader() {
		
	}
	
	public void loadListSchedules(List<Schedule> lSched) {
//		schedServ.mergeSchedules(lSched);
		try{
			schedServ.saveSchedules(lSched);
		} catch(Exception e) {
			
		}
	}
}
