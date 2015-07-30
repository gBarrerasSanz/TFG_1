package guiatv.realtime.brain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Async;

import guiatv.persistence.domain.MLChannel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.RtSchedule;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.service.ProgrammeService;
import guiatv.schedule.publisher.SchedulePublisher;

public class RealtimeBrain {
	
	@Autowired
	SchedulePublisher schedPublisher;
	
	@Autowired
	ProgrammeService progServ;
	
	/**
	 * Decide qué hacer con el Schedule <sched>:
	 * - Publicarlo en RabbitMQ
	 * - Descartarlo
	 */
	public void manageRtSchedule(RtSchedule rtSched) {
		// TODO: Tomar la decisión
		MLChannel mlChannel = rtSched.getMlChannel();
		boolean mustNotify = mlChannel.addRtSched(rtSched);
		
		if (mustNotify) {
			Message<RtSchedule> rtSchedMsg = MessageBuilder.withPayload(rtSched).build();
			schedPublisher.publishRtSchedule(rtSchedMsg);
		}
		else { // send == false
			// Descartar
		}
	}
}
