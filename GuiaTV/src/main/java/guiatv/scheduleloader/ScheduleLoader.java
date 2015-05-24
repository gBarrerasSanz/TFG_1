package guiatv.scheduleloader;

import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ScheduleRepository;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ScheduleLoader {
	private static Logger logger = Logger.getLogger("debugLog");
	
	@Autowired
	ScheduleRepository schedRep;
	
	public ScheduleLoader() {
		
	}
	
	public void loadListSchedules(List<Schedule> listSchedule) {
		for (Schedule sched: listSchedule) {
			logger.info("sched to save: "+sched);
			schedRep.save(sched);
		}
	}
}
