package eventproductor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class EventProductorTest {
	
	EventProductor eventProductor;
	
	@Before
	public void initTest() {
		eventProductor = new EventProductor();
	}
	
	@Test
	public void sendMessage() {
		boolean sendMsg;
		eventProductor.declareQueue("testqueue1");
		sendMsg = eventProductor.sendMessage("testmsg1");
		assertEquals(true, sendMsg);
	}
}
