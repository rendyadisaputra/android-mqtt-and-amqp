package com.example.myapplication;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class OnMessageCallback implements MqttCallback {
    public void connectionLost(Throwable cause) {
        // After the connection is lost, it usually reconnects here
        Log.i("My-Tag","disconnect，you can reconnect");
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // The messages obtained after subscribe will be executed here
        Log.i("My-Tag","Received message topic:" + topic);
        Log.i("My-Tag","Received message Qos:" + message.getQos());
        Log.i("My-Tag","Received message content:" + new String(message.getPayload()));
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.i("My-Tag","deliveryComplete---------" + token.isComplete());
    }
}