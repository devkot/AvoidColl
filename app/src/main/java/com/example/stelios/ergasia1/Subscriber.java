package com.example.stelios.ergasia1;

import android.content.Context;
import android.location.LocationListener;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.sql.Timestamp;

//code from eclass
public class Subscriber implements MqttCallback{
    static String clientId = MainActivity.DeviceID;
    public static void main(String top, String id) {
        String topic = top;
        int qos = 2;
        String broker = MQTTSettings.str;
        MemoryPersistence persistence = new MemoryPersistence();
        try {
//Connect client to MQTT Broker
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
//Set callback
            Subscriber main = new Subscriber();
            sampleClient.setCallback(main);
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
//Subscribe to a topic
            System.out.println("Subscribing to topic\""+topic+"\" qos "+ qos);
            sampleClient.subscribe("Acceleration/Danger"+"/"+clientId, qos);
            sampleClient.subscribe("Proximity/Danger"+"/"+clientId,qos);
            sampleClient.subscribe("Light/Danger"+"/"+clientId,qos);
            sampleClient.subscribe("Acceleration/Confirmed"+"/"+clientId,qos);
            sampleClient.subscribe("Proximity/Confirmed"+"/"+clientId,qos);
            sampleClient.subscribe("Light/Confirmed"+"/"+clientId,qos);
        } catch(MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println(" msg " + me.getMessage());
            System.out.println(" loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println(" excep " + me);me.printStackTrace();
        }
    }

    /***@seeMqttCallback#connectionLost(Throwable)*/
    public void connectionLost(Throwable cause) {
// This method is called when the connection to the server is lost.
        System.out.println("Connection lost!" + cause);
       // System.exit(1);
    }
    /***@seeMqttCallback#deliveryComplete(IMqttDeliveryToken)*/
    public void deliveryComplete(IMqttDeliveryToken token) {
//Called when delivery for a message has been completed, and all acknowledgments have been received
    }
    /***@seeMqttCallback#messageArrived(String,MqttMessage)*/
    public void
    messageArrived(String topic, final MqttMessage message) throws MqttException
    {
//This method is called when a message arrives from the server.
        String time = new Timestamp(System.currentTimeMillis()).toString();
        System.out.println("Time:\t" +time +
                "  Topic:\t" + topic +
                "  Message:\t" + new String(message.getPayload()) +
                "QoS:\t" + message.getQos());
        Looper.prepare();
        if(topic==("Acceleration/Danger"+"/"+clientId) || topic == ("Proximity/Danger"+"/"+clientId) || topic == ("Light/Danger"+"/"+clientId)) {
            OnMode.returnHandler().sendEmptyMessage(0);//show toast
        }
        if (topic == ("Acceleration/Confirmed"+"/"+clientId) || topic == ("Proximity/Confirmed"+"/"+clientId) || topic == ("Light/Confirmed"+"/"+clientId)) {
            OnMode.returnConfirmedHandler().sendEmptyMessage(0);//show confirmed message
        }
    }
}

