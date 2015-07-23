package guiatv.schedule.publisher;

import guiatv.catalog.restcontroller.PublisherRestController;
import guiatv.persistence.domain.Event_old;
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
	
	public interface PublisherView {};
	
	@Autowired
	AmqpTemplate amqpTmp;
	
	@Autowired
	ScheduleService schedServ;
	
	public void publishTopics(Message<List<Schedule>> listSchedMsg ) {
		List<Schedule> publishedSched = new ArrayList<Schedule>();
		String routKey = null;
		for (Schedule sched: listSchedMsg.getPayload()) {
			try {
				routKey = sched.getProgramme().getHashNameProg();
				String schedJson = sched.toStringPublisher();
				schedJson = StringEscapeUtils.unescapeJava(schedJson);
				amqpTmp.convertAndSend(routKey, schedJson);
				publishedSched.add(sched);
				logger.debug("Published: "+sched.getProgramme().getNameProg()
						+" -> "+sched.getStart()+" --- "+sched.getEnd());
			
			} catch (AmqpException e) {
				logger.error("ERROR: Could NOT connect to RabbitMQ");
			} catch(Exception e) {
				logger.error("ERROR: Unknown error");
			}
		}
		schedServ.deleteSchedules(publishedSched);
	}
	
//	public void declareQueue(String queueName) {
//		Queue queue = new Queue(queueName);
//		this.rabbitAdmin.declareQueue(queue);
//		TopicExchange topicExchange = new TopicExchange(queueName);
//		this.rabbitAdmin.declareExchange(topicExchange);
//		this.rabbitAdmin
//		    .declareBinding(
//		        BindingBuilder
//		            .bind(queue)
//		            .to(topicExchange)
//		            .with(queueName));
//	}
//
//	public boolean sendMessage(String message){ 
//		try{ 
//			amqpTemplate.convertAndSend("amq.topic", "amq.topic.*", message); 
//	        return true;
//	    }catch(Exception ex){ 
//	    	return false;
//	    }
//	}
	
//	public String sendMsg(String id, String val) {
//		amqpTemplate.convertAndSend(id, val);
//		return (String) amqpTemplate.receiveAndConvert("myqueue");
//	}
}
