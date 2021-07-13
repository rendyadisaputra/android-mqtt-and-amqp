package com.example.myapplication;

import android.app.Service;
import android.os.AsyncTask;
import android.os.IBinder;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MyBackgroundService  extends Service {
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
            ConnectionFactory factory;
            Connection conn;
            Channel channel;
            Log.i("My-Tag","Creating Factory");
            try{
                factory = new ConnectionFactory();
                factory.setUsername("guest");
                factory.setPassword("guest");
                factory.setVirtualHost("/");
                factory.setHost("192.168.0.9");
                factory.setPort(5672);
                conn = factory.newConnection();
                channel = conn.createChannel();

                String QUEUE_NAME = "fanout";
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                String message = "Hello World!";
                Log.i("My-Tag","Hello sir");
                int i =0;

                while (true) {
                    Log.i("My-Tag","Hello wolrd " + i);
                    channel.basicPublish("logs", QUEUE_NAME, null, ("Hello world " + i).getBytes());
                    Thread.sleep(2 * 1000);
                    i+=1;
                }

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
