package com.example.upt.servicebind;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.security.Timestamp;

public class MainActivity extends AppCompatActivity {


    BoundService mBoundService;
    boolean mServiceBound = false;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BoundService.MyBinder myBinder =(BoundService.MyBinder) service;
            mBoundService = myBinder.getService();
            mServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    };

    @Override
    protected void onStart(){
        super.onStart();

        Intent intent = new Intent(this,BoundService.class);
        startService(intent);
        bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop(){
        super.onStop();

        if (mServiceBound){
            unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView timestampText = (TextView)findViewById(R.id.timestamp_text);
        Button printTime = (Button)findViewById(R.id.print_timestamp);
        final Button stopService = (Button)findViewById(R.id.stop_service);


        printTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timestampText.setText(mBoundService.getTimeStamp());
            }
        });

        stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mServiceBound){
                    unbindService(mServiceConnection);
                    mServiceBound = false;
                }
                Intent intent = new Intent(getBaseContext(),BoundService.class);
                stopService(intent);
            }
        });

    }
}
