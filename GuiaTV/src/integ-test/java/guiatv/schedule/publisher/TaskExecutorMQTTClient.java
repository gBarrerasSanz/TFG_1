package guiatv.schedule.publisher;

import guiatv.persistence.domain.Schedule;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;


public class TaskExecutorMQTTClient {
	
//	@Autowired
//	Jackson2JsonMessageConverter jackson;
	
	@Autowired
	ObjectMapper mapper;
	
	static Logger log = Logger.getLogger(TaskExecutorMQTTClient.class.getName());
	
	private TaskExecutorMQTTClient thisThread = this;
	
	private class MQTTClientWorker implements Runnable {
		
		int qos = 1;
		
		MqttClient mqttclient;
		IMqttToken token;
		String username;
		char[] password;
		String BROKER_URL;
		String clientId;
		String[] topicArr;
		List<String> receivedMessages;
		
		public MQTTClientWorker(String clientId, String[] topicArr){
			this.BROKER_URL = "tcp://localhost:1883";
			this.username = "user";
			this.password = "userp".toCharArray();
			this.clientId = clientId;
			this.topicArr = topicArr;
	
			try {
				MqttConnectOptions connOpts = new MqttConnectOptions();
				connOpts.setCleanSession(true);
				connOpts.setUserName(username);
				connOpts.setPassword(password);
				mqttclient = new MqttClient(BROKER_URL, this.clientId);
				log.debug("Connecting to broker: "+ BROKER_URL);
				token = mqttclient.connectWithResult(connOpts);
				mqttclient.setCallback(new CallbackFunction(clientId, thisThread));
				receivedMessages = new ArrayList<>();
				
			} catch (MqttException e) {
				System.out.println("reason "+e.getReasonCode());
				System.out.println("msg "+e.getMessage());
				System.out.println("loc "+e.getLocalizedMessage());
				System.out.println("cause "+e.getCause());
				System.out.println("excep "+e);
				e.printStackTrace();
			}
		}
		
		private void subscribe() {
			try {
				int[] qosArr = new int[topicArr.length];
				for (int i=0; i<qosArr.length; i++) { qosArr[i] = qos; }
				mqttclient.subscribe(topicArr, qosArr);
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}
		
//		public void Notify(String Topic, String message){
//			try {
//	
//				MqttMessage mqttmessage = new MqttMessage();
//				mqttmessage.setPayload(message.getBytes());
//				mqttclient.publish(Topic, mqttmessage);
//				System.out.println("Message published to topic - " +  Topic);
//			} catch(MqttException me) {
//				System.out.println("reason "+me.getReasonCode());
//				System.out.println("msg "+me.getMessage());
//				System.out.println("loc "+me.getLocalizedMessage());
//				System.out.println("cause "+me.getCause());
//				System.out.println("excep "+me);
//				me.printStackTrace();
//			}
//		}
	
		private void disconnect(){
			try {
				mqttclient.disconnect();
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			this.subscribe();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
//				e.printStackTrace();
			}
//			this.disconnect();
		}
	}
	
	private TaskExecutor taskExecutor;
	private MQTTClientWorker mqttClientWorker;
    public TaskExecutorMQTTClient(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void connectAndSubscribe(String clientId, String[] topicArr) {
    	mqttClientWorker = new MQTTClientWorker(clientId, topicArr);
    	taskExecutor.execute(mqttClientWorker);
    }
    
    public List<Schedule> getReceivedListSchedules() {
    	List<Schedule> listSchedules = new ArrayList<Schedule>();
    	
    	try {
	    	for (String msgStr: mqttClientWorker.receivedMessages) {
//	    		Message message = jackson.toMessage(msgStr, new MessageProperties());
	    		Schedule sched = mapper.readValue(msgStr, Schedule.class);
	    		listSchedules.add(sched);
	    	}
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
//    	JsonMessageConverter converter = new JsonMessageConverter();
//    	converter.setClassMapper(new DefaultClassMapper());
//    	Message message = converter.toMessage(new String[] { "test" }, new MessageProperties());
//    	Object fromMessage = converter.fromMessage(message);
    	return listSchedules;
    }
    
    public void addReceivedMessage(String msg) {
    	mqttClientWorker.receivedMessages.add(msg);
    }
	
    public void disconnect() {
    	mqttClientWorker.disconnect();
    }

}