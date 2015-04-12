package eventproducer;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.MqttCallback;


public class MQTTTestingClient {
	
	static Logger log = Logger.getLogger(MQTTTestingClient.class.getName());
	
	int qos = 0;
	
	MqttClient mqttclient;
	IMqttToken token;
	String username;
	char[] password;
	String BROKER_URL;
	MqttCallback mqttCallBack;

	public MQTTTestingClient(String clientId){
		this.BROKER_URL = "tcp://localhost:1883";
		this.username = "user";
		this.password = "userp".toCharArray();
		MemoryPersistence persistence = new MemoryPersistence();

		try {
			System.out.print(BROKER_URL);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			connOpts.setUserName(username);
			connOpts.setPassword(password);
			mqttclient = new MqttClient(BROKER_URL,clientId);
			System.out.println("Connecting to broker: "+ BROKER_URL);
			mqttclient.setCallback(mqttCallBack);
//			mqttclient.connect(connOpts);
			token = mqttclient.connectWithResult(connOpts);
			mqttCallBack = new CallbackFunction(clientId);

		} catch (MqttException e) {
			System.out.println("reason "+e.getReasonCode());
			System.out.println("msg "+e.getMessage());
			System.out.println("loc "+e.getLocalizedMessage());
			System.out.println("cause "+e.getCause());
			System.out.println("excep "+e);
			e.printStackTrace();
		}
	}
	
	public void subscribe(String[] topicArr) {
		try {
			int[] qosArr = new int[topicArr.length];
			for (int i=0; i<qosArr.length; i++) { qosArr[i] = qos; }
			mqttclient.subscribe(topicArr, qosArr);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void Notify(String Topic, String message){
		try {

			MqttMessage mqttmessage = new MqttMessage();
			mqttmessage.setPayload(message.getBytes());
			mqttclient.publish(Topic, mqttmessage);
			System.out.println("Message published to topic - " +  Topic);
		} catch(MqttException me) {
			System.out.println("reason "+me.getReasonCode());
			System.out.println("msg "+me.getMessage());
			System.out.println("loc "+me.getLocalizedMessage());
			System.out.println("cause "+me.getCause());
			System.out.println("excep "+me);
			me.printStackTrace();
		}
	}

	public void disconnect(){
		try {
			mqttclient.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

}