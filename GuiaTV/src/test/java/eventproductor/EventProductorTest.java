package eventproductor;

import static org.junit.Assert.*;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import application.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class EventProductorTest {
	
	@Autowired
	private ApplicationContext ctx;
	
	/*
	 * Este test se completa satisfactoriamente aun sin estar el RabbitMQ funcionando...
	 * */
	@Test
	public void sendMessage() {
		EventProductor eventProductor = new EventProductor();
		MQTTTestingClient mqttClient = null;
		try {
			boolean sendMsg;
			eventProductor.declareQueue("testqueue1");
			sendMsg = eventProductor.sendMessage("testmsg1");
			assertEquals(true, sendMsg);
			
			
			mqttClient = new MQTTTestingClient(5);
			mqttClient.subscribe("testqueue1");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
