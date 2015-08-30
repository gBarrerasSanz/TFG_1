package guiatv.schedule.poller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ScheduleRepository;
import guiatv.persistence.repository.service.ScheduleService;
import guiatv.realtime.rtmpspying.MonitorMyCh;
import guiatv.realtime.servicegateway.CapturedBlobsGateway;

public class SchedulePoller {
	
	@Autowired
	ScheduleService schedServ;
	
	@Autowired
	MonitorMyCh monitorMyCh;
	
	private final int SECS_PER_MIN = 60;
	/** CONFIGURATION PARAMS */
	private final int SECONDS_FROM_START = SECS_PER_MIN * 10; // 5 min
	
	public List<Schedule> askForSchedules() {
		List<Schedule> lSched = schedServ.findByPublishedFalseAndSecondsFromStart(SECONDS_FROM_START);
		// Filtrar lSched dejando solo los channels activos
		List<Schedule> flSched = new ArrayList<Schedule>();
		for (Schedule sched: lSched) {
			if(monitorMyCh.getByChannel(sched.getChannel()).getMyChState().isActive()) { // Si channel activo
				flSched.add(sched);
			}
		}
		return lSched;
	}
	
	
}
