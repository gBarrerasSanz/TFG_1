package guiatv.schedule.publisher;

import guiatv.persistence.domain.Event_old;
import guiatv.persistence.domain.Schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SchedulePublisher {
	
//	@Autowired
//	RabbitAdmin rabbitAdmin;
//	@Autowired
//	TopicExchange topicExch;
	
	@Autowired
	AmqpTemplate amqpTmp;
	
	public void publishTopics(Message<List<Schedule>> listSchedMsg ) {
		try {
			String routKey = null;
			for (Schedule sched: listSchedMsg.getPayload()) {
				routKey = sched.getChannel().getNameIdCh()+"."+sched.getProgramme().getNameProg();
				amqpTmp.convertAndSend(routKey, sched);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
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