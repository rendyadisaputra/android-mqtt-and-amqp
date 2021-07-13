package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;



public class MyBackgroundServiceMQTT extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        AsyncProcess ap = new AsyncProcess();
        ap.execute("");
        return super.onStartCommand(intent, flags, startId);
    }

    static class AsyncProcess extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... args) {
            String subTopic = "testtopic/#";
            String pubTopic = "testtopic/";
            String content = "Hello World";
            int qos = 2;
            String broker = "tcp://192.168.0.9:1883";
            String clientId = "emqx_test";
            MemoryPersistence persistence = new MemoryPersistence();

            try{
                MqttClient client = new MqttClient(broker, clientId, persistence);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setUserName("emqx_test");
                connOpts.setPassword("emqx_test_password".toCharArray());
                connOpts.setCleanSession(true);

                client.setCallback(new OnMessageCallback());
                Log.i("My-tag", "connecting to BRoker " + broker);
                client.connect(connOpts);

                Log.i("My-tag","Connected");
                Log.i("My-tag","Publishing message: " + content);

                // Subscribe
                client.subscribe(subTopic);
                // Required parameters for message publishing
                MqttMessage message ;
                int i =0;

//                while (true) {
                    Log.i("My-Tag",content + i);
                    message = new MqttMessage((content + i ).getBytes());
                    message.setQos(qos);
                    client.publish(pubTopic, message);
                    Thread.sleep(2 * 1000);
                    i+=1;
//                }



            } catch(Exception e){
                Log.e("My-Tag", e.toString());
                e.printStackTrace();
            }

            return "Download Completed!";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("DownloadFragment ", s);
//            mCallbacks.onPostExecute(s);
        }
    }


}

