package eventproductor;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class CallbackFunction implements MqttCallback {
	
	int id;
	
	public CallbackFunction(int id) {
		this.id = id;
	}
	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		// TODO Auto-generated method stub
		System.err.println("CallbackFunction.messageArrived (id="+id+"): "+"arg0 = "+arg0 + "\narg1 = "+arg1.toString());
	}

}
