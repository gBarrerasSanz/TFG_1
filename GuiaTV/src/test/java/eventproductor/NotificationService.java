package eventproductor;

import org.springframework.cglib.proxy.CallbackGenerator.Context;

public class NotificationService {
	 
    private String broker = "tcp://{rabbitmq_server}:1883";
    private int qos = 0;
 
    private MqttCallBack mqttCallBack;
    private Context context;
 
    private MqttClient notificationClient = null;
 
    private MemoryPersistence persistence = new MemoryPersistence();
 
    public NotificationService(Context context, MqttCallBack mqttCallBack) {
 
        this.context = context;
        this.mqttCallBack = mqttCallBack;
    }
 
    public void start() throws MqttException{
 
        this.notificationClient =
                new MqttClient(
                        this.broker,
                        MqttClient.generateClientId(),
                        this.persistence);
 
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName("user");
        connOpts.setPassword("user".toCharArray());
        connOpts.setCleanSession(true);
 
        this.notificationClient.setCallback(this.mqttCallBack);
 
        this.notificationClient.connect(connOpts);
 
        if(!this.notificationClient.isConnected()){
 
            Log.e("MQTT_ERROR", "Error Connecting on notification server.");
            return;
        }
 
        this.notificationClient.subscribe("amq.topic.*", this.qos);
 
        Log.i("MQTT_ERROR", "Connected");
    }
 
    public void disconnect(){
 
        try{
 
            this.notificationClient.disconnect();
        }catch(MqttException me){
 
            Log.e(
                    "MQTT_ERROR",
                    "Error disconnecting connection from notification server.\n" + me.getMessage());
        }
    }
 
}
