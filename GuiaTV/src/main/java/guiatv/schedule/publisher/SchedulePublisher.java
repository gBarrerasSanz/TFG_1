package guiatv.schedule.publisher;

import guiatv.catalog.restcontroller.PublisherRestController;
import guiatv.common.CommonUtility;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.RtSchedule;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.service.ScheduleService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourcesLinksVisible;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SchedulePublisher {
	
//	@Autowired
//	RabbitAdmin rabbitAdmin;
//	@Autowired
//	TopicExchange topicExch;
	
	private static Logger logger = Logger.getLogger("debugLog");
	
	public interface PublisherScheduleView {};
	
	public interface PublisherRtScheduleView {};
	
	@Autowired
	AmqpTemplate amqpTmp;
	
	@Autowired
	ScheduleService schedServ;
	
	public void publishListSchedules(Message<List<Schedule>> listSchedMsg ) {
		List<Schedule> publishedSched = new ArrayList<Schedule>();
		String routKey = null;
		for (Schedule sched: listSchedMsg.getPayload()) {
			try {
				assert(sched != null);
				routKey = sched.getProgramme().getHashNameProg();
				String schedJson = sched.toStringPublisher();
				schedJson = StringEscapeUtils.unescapeJava(schedJson);
				amqpTmp.convertAndSend(routKey, schedJson);
				publishedSched.add(sched);
				logger.debug("Published Schedule: "+sched.getProgramme().getNameProg()
						+" -> "+CommonUtility.timestampToString(sched.getStart())+
						" --- "+CommonUtility.timestampToString(sched.getEnd()));
			
			} catch (AmqpException e) {
				logger.error("ERROR: Could NOT connect to RabbitMQ");
			} catch(Exception e) {
				e.printStackTrace();
//				logger.error("ERROR: Unknown error");
			}
		}
		/**
		 * Actualizar el campo published de los schedules publicados a True.
		 */
		for (Schedule sched: publishedSched) {
			schedServ.setTruePublishedWhereIdSched(sched.getIdSched());
		}
//		schedServ.delete(publishedSched);
	}
	
	public void publishRtSchedule(Message<RtSchedule> rtScheduleMsg, Programme prog) {
		RtSchedule rtSched = rtScheduleMsg.getPayload();
		String routKey = null;
		try {
			assert(rtSched != null);
			routKey = prog.getHashNameProg();
			String rtSchedJsonString = rtSched.toString();
//			schedJson = StringEscapeUtils.unescapeJava(schedJson);
			amqpTmp.convertAndSend(routKey, rtSchedJsonString);
			logger.debug("Published RtSchedule: "+rtSched.getChannel()
					+" -> "+CommonUtility.timestampToString(rtSched.getInstant())
					+" -> "+rtSched.getType());
		
			} catch (AmqpException e) {
				logger.error("ERROR: Could NOT connect to RabbitMQ");
			} catch(Exception e) {
				e.printStackTrace();
//				logger.error("ERROR: Unknown error");
			}
	}
}
