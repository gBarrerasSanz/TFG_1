package guiatv.schedule.loader;

import guiatv.common.CommonUtility;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ScheduleRepository;
import guiatv.persistence.repository.service.ChannelService;
import guiatv.persistence.repository.service.ProgrammeService;
import guiatv.persistence.repository.service.ScheduleService;
import guiatv.realtime.rtmpspying.MonitorMyCh;

import java.util.ArrayList;
import java.util.HashMap;
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
				if (sched.isValid()) {
					flSched.add(sched);
				}
			}
		}
//		saveSchedules(flSched);
		schedServ.save(flSched);
//		try {
//			schedServ.save(flSched);
//		} catch(Exception e) {
//			logger.debug("ScheduleLoader: Error on saving schedules");
//		}
//		System.out.println("Loaded "+numInserted+" schedules");
//		spyLaunchServ.loadAndSpyChannels();
	}
	
	private void saveSchedules(List<Schedule> lSched) {
		HashMap<Schedule, Integer> schedMap = new HashMap<Schedule, Integer>();
		int numSaved = 0;
		int numDup = 0;
		int numDeprecated = 0;
		for (Schedule sched: lSched) {
			// Si el final del schedule NO es posterior al momento actual -> Saltar a la siguiente iteración
			if ( ! CommonUtility.isScheduleOnTime(sched)) {
				numDeprecated++;
				continue;
			}
			try {
				Channel ch = chServ.findByIdChBusiness(sched.getChannel().getIdChBusiness(), true);
				Programme prog = progServ.findByNameProg(sched.getProgramme().getNameProg(), true);
				if (ch != null) { // Ya existe canal
					sched.setChannel(ch);
				}
				else { // No existe canal
					chServ.save(sched.getChannel());
				}
				if (prog != null) { // Ya existe programa
					sched.setProgramme(prog);
				}
				else { // No existe programa
					progServ.save(sched.getProgramme());
				}
				Schedule schedIn = schedServ.findOneByChannelAndProgrammeAndStartAndEnd(
						sched.getChannel(), sched.getProgramme(), sched.getStart(), sched.getEnd());
				if (schedIn == null && ! schedMap.containsKey(sched)) { // Si no está en la base de datos y No se ha introducido antes
//					logger.debug("sched = "+sched.toString());
					schedServ.save(sched);
					schedMap.put(sched, 1);
					numSaved++;
				}
				else {
//					logger.debug("Schedule REPETIDO: "+schedIn);
					numDup++;
				}
				
			} catch(Exception e) {
				logger.debug("Schedule REPETIDO: "+sched);
			}
		}
		
		logger.debug("SCHEDULE LOADER: numSaved = "+numSaved+"/"+lSched.size()+"; "+
				"numDeprecated = "+numDeprecated+"/"+lSched.size()+"; "+
				"numDuplicated = "+numDup+"/"+lSched.size());
	}
}
