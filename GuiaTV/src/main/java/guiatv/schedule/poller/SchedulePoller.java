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
import guiatv.realtime.service.CapturedFramesGateway;

public class SchedulePoller {
	
	@Autowired
	ScheduleRepository schedRep;
	
	public List<Schedule> askForSchedules() {
		// TODO: De momento devuelve todos los schedules
		List<Schedule> lSched = schedRep.findAll();
		return lSched;
	}
}
