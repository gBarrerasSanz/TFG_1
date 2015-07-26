package guiatv.realtime.brain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import guiatv.persistence.domain.Schedule;
import guiatv.schedule.publisher.SchedulePublisher;

public class RealtimeBrain {
	
	@Autowired
	SchedulePublisher schedPublisher;
	
	/**
	 * Decide qué hacer con el Schedule <sched>:
	 * - Publicarlo en RabbitMQ
	 * - Descartarlo
	 */
	public void manageSchedule(Schedule sched) {
		
		// TODO: Tomar la decisión
		List<Schedule> lSched = new ArrayList<Schedule>();
		lSched.add(sched);
		Message<List<Schedule>> schedMsg = MessageBuilder.withPayload(lSched).build();
		schedPublisher.publishTopics(schedMsg);
	}
}
