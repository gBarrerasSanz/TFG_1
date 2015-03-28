package eventproductor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import xmltv.datatypes.Event;

public class EventProductorPublisher {
	
	@Autowired
	AmqpTemplate amqpTemplate;
	@Autowired
	RabbitAdmin rabbitAdmin;
	
//	@Autowired
//	private ApplicationContext ctx;
	
//	public EventProductorPublisher() {
//		amqpTemplate = ctx.getBean(AmqpTemplate.class);
//		rabbitAdmin = ctx.getBean(RabbitAdmin.class); 
//	}
	
	public Message<?> publishTopics(Message<List<Event>> lEvtMsg ) {
		List<Event> lEvt = lEvtMsg.getPayload();
		for (Event e: lEvt) {
			;
		}
		Message<List<Event>> respMsg = MessageBuilder.
				withPayload(lEvt).build();
		return respMsg;
	}
	
	public void declareQueue(String queueName) {
		Queue queue = new Queue(queueName);
		this.rabbitAdmin.declareQueue(queue);
		TopicExchange topicExchange = new TopicExchange(queueName);
		this.rabbitAdmin.declareExchange(topicExchange);
		this.rabbitAdmin
		    .declareBinding(
		        BindingBuilder
		            .bind(queue)
		            .to(topicExchange)
		            .with(queueName));
	}

	public boolean sendMessage(String message){ 
		try{ 
			amqpTemplate.convertAndSend("amq.topic", "amq.topic.*", message); 
	        return true;
	    }catch(Exception ex){ 
	    	return false;
	    }
	}
	
//	public String sendMsg(String id, String val) {
//		amqpTemplate.convertAndSend(id, val);
//		return (String) amqpTemplate.receiveAndConvert("myqueue");
//	}
}
