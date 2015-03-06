package eventproductor;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class NotificationService {

	String clientId     = "RTNClient";
	MqttDeliveryToken token;
	int qos = 2;

	MqttClient mqttclient;
	String username;
	char[] password;
	String broker;

	public NotificationService(){
		this.broker = "http://localhost:1883";
		this.username = "guest";
		this.password = "guest".toCharArray();
		MemoryPersistence persistence = new MemoryPersistence();

		try {
			System.out.print(broker);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			connOpts.setUserName(username);
			connOpts.setPassword(password);
			mqttclient = new MqttClient(broker, clientId, persistence);
			System.out.println("Connecting to broker: "+ broker);
			mqttclient.connect(connOpts);

		} catch (MqttException e) {
			System.out.println("reason "+e.getReasonCode());
			System.out.println("msg "+e.getMessage());
			System.out.println("loc "+e.getLocalizedMessage());
			System.out.println("cause "+e.getCause());
			System.out.println("excep "+e);
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