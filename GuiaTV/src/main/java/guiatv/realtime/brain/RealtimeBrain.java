package guiatv.realtime.brain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

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
	public void manageRtSchedule(RtSchedule rtsched) {
		
		
		boolean send = true; // TODO: Tomar la decisión
		
		if (send) {
			// Determinar a qué programa afecta
			Programme prog = progServ.findOneByChannelAndInstant(rtsched.getChannel(), rtsched.getInstant());
			if (prog != null) {
				Message<RtSchedule> rtSchedMsg = MessageBuilder.withPayload(rtsched).build();
				schedPublisher.publishRtSchedule(rtSchedMsg, prog);
			}
			else {
				// Descartar
			}
		}
		else { // send == false
			// Descartar
		}
	}
}
