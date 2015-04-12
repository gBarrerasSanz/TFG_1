package eventproducer;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import persistence.EventServiceTests;

public class CallbackFunction implements MqttCallback {
	
	private static Logger log = Logger.getLogger("debugLog");
	String clientId;
	TaskExecutorMQTTClient clientThread;
	
	public CallbackFunction(String clientId, TaskExecutorMQTTClient clientThread) {
		this.clientId = clientId;
		this.clientThread = clientThread;
	}
	@Override
	public void connectionLost(Throwable arg0) {
		log.debug("MQTTTestingClient id="+clientId+" connectionLost");
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		log.debug("MQTTTestingClient id="+clientId+" deliveryComplete");
	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		log.debug("MQTT Client id="+clientId+"; messageArrived = "+arg1.toString());
		clientThread.addReceivedMessage(arg1.toString());
	}

}
