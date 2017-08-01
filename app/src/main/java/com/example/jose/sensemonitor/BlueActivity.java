package com.example.jose.sensemonitor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.telecom.ConnectionService;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Created by jose on 18/07/17.
 */

public class BlueActivity extends AppCompatActivity implements communicate{

    private BlueConnectionService mChatService = null;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.splash);

        //display the logo during 2 seconds,
        new CountDownTimer(2000,1000){
            @Override
            public void onTick(long millisUntilFinished){}

            @Override
            public void onFinish(){
                getSupportActionBar().show();
                setContentView(R.layout.blue_layout);
                splashFinished();
            }
        }.start();
    }

    void splashFinished(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        BlueFragment fragment = new BlueFragment();
        transaction.replace(R.id.sample_content_fragment, fragment);
        transaction.commit();
    }

    @Override
    public void connectToDevice(BluetoothDevice device) {
        mChatService.connect(device, true);
        // TODO: wait for successful connection and replace fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ValuesFragment ValsFragment = new ValuesFragment();
        ft.replace(R.id.sample_content_fragment,ValsFragment);
        ft.commit();
    }

    public void setmChatServiceHandler(Handler mHand){
        mChatService.setHandler(mHand);
    }

    @Override
    protected void onStart() {
        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BlueConnectionService(BlueActivity.this);

        super.onStart();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void showMore(View view){
        int plotID = 0;
        switch (view.getId()){
            case R.id.btn_s1:
                plotID = 1;
                break;
            case R.id.btn_s2:
                plotID = 2;
                break;
            case R.id.btn_s3:
                plotID = 3;
                break;
            case R.id.btn_s4:
                plotID = 4;
                break;
            case R.id.btn_s5:
                plotID = 5;
                break;
            default:break;
        }
        //Toast.makeText(getApplicationContext(), "Plot "+plotID, Toast.LENGTH_SHORT).show();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        GraphFragment graphyFragment = new GraphFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.PLOT_ID, plotID);
        graphyFragment.setArguments(args);
        ft.replace(R.id.sample_content_fragment, graphyFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

}
