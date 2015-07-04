package guiatv.schedule.poller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import guiatv.persistence.domain.Event_old;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ScheduleRepository;
import guiatv.persistence.repository.service.ScheduleService;
import guiatv.realtime.servicegateway.CapturedFramesGateway;

public class SchedulePoller {
	
	@Autowired
	ScheduleService schedServ;
	
	private final int SECONDS_FROM_START = 60 * 10;
	
	public List<Schedule> askForSchedules() {
//		List<Schedule> lSched = schedServ.findAll(true);
		List<Schedule> lSched = schedServ.findBySecondsFromStart(SECONDS_FROM_START, true);
		return lSched;
	}
	
	
}
