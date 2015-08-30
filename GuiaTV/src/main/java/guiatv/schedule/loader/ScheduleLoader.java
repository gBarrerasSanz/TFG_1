package guiatv.schedule.loader;

import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ScheduleRepository;
import guiatv.persistence.repository.service.ChannelService;
import guiatv.persistence.repository.service.ProgrammeService;
import guiatv.persistence.repository.service.ScheduleService;
import guiatv.realtime.rtmpspying.MonitorMyCh;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.apache.log4j.Logger;
import org.h2.jdbc.JdbcSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring4.SpringTemplateEngine;

@Transactional // Es necesario
public class ScheduleLoader {
	private static Logger logger = Logger.getLogger("debugLog");
	
	@Autowired
	ScheduleService schedServ;
	@Autowired
	ChannelService chServ;
	@Autowired
	ProgrammeService progServ;
	
	@Autowired 
	ApplicationContext appCtx;
	@Autowired
	MonitorMyCh monitorMyCh;
	
	public ScheduleLoader() {
	}
	
	public void loadListSchedules(List<Schedule> lSched) {
		// Filtrar lSched dejando solo los channels activos
		List<Schedule> flSched = new ArrayList<Schedule>();
		for (Schedule sched: lSched) {
			if(monitorMyCh.getByChannel(sched.getChannel()) != null) { // Si existe MyCh con este channel
				flSched.add(sched);
			}
		}
		int numInserted = schedServ.save(flSched);
		System.out.println("Loaded "+numInserted+" schedules");
//		spyLaunchServ.loadAndSpyChannels();
		
	}
}
