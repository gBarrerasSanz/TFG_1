package guiatv.schedule.loader;

import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ScheduleRepository;
import guiatv.persistence.repository.service.ChannelService;
import guiatv.persistence.repository.service.ProgrammeService;
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
	@Autowired
	ChannelService chServ;
	@Autowired
	ProgrammeService progServ;
	
	public ScheduleLoader() {
		
	}
	
	public void loadListSchedules(List<Schedule> lSched) {
		
		schedServ.insertSchedules(lSched);
		System.out.println("Done");
		
//		for (Schedule sched: lSched) {
//			Channel ch = chServ.findByIdChBusiness(sched.getChannel().getIdChBusiness());
//			Programme prog = progServ.findByNameProg(sched.getProgramme().getNameProg());
//			if (ch != null) {
//				sched.setChannel(ch);
//			}
//			if (prog != null) {
//				sched.setProgramme(prog);
//			}
//			schedServ.saveSchedule(sched);
//		}
		
//		for (Schedule sched: lSched) {
//			Channel ch = chServ.findByIdChBusiness(sched.getChannel().getIdChBusiness());
//			Programme prog = progServ.findByNameProg(sched.getProgramme().getNameProg());
//			if (ch != null) {
//				chServ.deleteChannel(ch);
//			}
//			if (prog != null) {
//				progServ.deleteProgramme(prog);
//			}
//			schedServ.insertSchedule(sched);
//		}
		
	}
}
