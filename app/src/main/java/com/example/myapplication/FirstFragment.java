package com.example.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
//import com.rabbitmq.client.;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;

import com.example.myapplication.databinding.FragmentFirstBinding;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Log.i("My-Tag", "on Create View");
//        activitySync();
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void activitySync(){

        new MyAsyncTask().execute("");

    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {

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
                    channel.basicPublish("logs", QUEUE_NAME, null, ("Hello world " + i).getBytes());
                    Thread.sleep(2 * 1000);
                    i +=1;
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

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("My-Tag", "onViewCreated");
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        Log.i("My-Tag", "onDestroyView");
        super.onDestroyView();
        binding = null;
    }


}