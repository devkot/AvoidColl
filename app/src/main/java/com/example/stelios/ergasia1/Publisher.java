package com.example.stelios.ergasia1;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

//code from eclass
public class Publisher {
    public static void main(String[] args) {
        String topic = "MQTT Example";
        String content = "Message from MqttPublishSample ";
        int qos = 2;
        String broker = " tcp ://localhost:1883";
        String clientId = " JavaSamplePublisher ";
        MemoryPersistence persistence = new MemoryPersistence();
        try {
//Connect to MQTT Broker
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
//Publish message to MQTT Broker
            System.out.println("Publishing message: "+content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("Message published");
            sampleClient.disconnect();
            System.out.println("Disconnected");
            System.exit(0);
        } catch(MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println(" msg " + me.getMessage());
            System.out.println(" loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println(" excep " + me);
            me.printStackTrace();
        }
    }
}

