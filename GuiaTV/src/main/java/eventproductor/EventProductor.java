package eventproductor;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class EventProductor {
	
	AmqpTemplate amqpTemplate;
	RabbitAdmin rabbitAdmin;
	
	public EventProductor() {
		createContext();
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
	        //Logging.
	    } 
	    return false;
	}
	
	private void createContext() {
		ApplicationContext context =
			    new GenericXmlApplicationContext("/WebContent/WEB-INF/rabbitmq-context.xml");
			amqpTemplate = context.getBean(AmqpTemplate.class);
			rabbitAdmin = context.getBean(RabbitAdmin.class); 
	}
	
//	public String sendMsg(String id, String val) {
//		amqpTemplate.convertAndSend(id, val);
//		return (String) amqpTemplate.receiveAndConvert("myqueue");
//	}
}
