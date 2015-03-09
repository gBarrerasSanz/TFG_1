package eventproductor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class EventProductorTest {
	
	EventProductor eventProductor;
	MQTTTestingClient mqttClient;
	
	@Before
	public void initTest() {
		eventProductor = new EventProductor();
	}
	
	@Test
	public void sendMessage() {
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
