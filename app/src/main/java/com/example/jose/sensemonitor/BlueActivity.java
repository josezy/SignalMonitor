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
import android.support.annotation.Nullable;
import android.telecom.ConnectionService;
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

public class BlueActivity extends Activity {

    private BluetoothAdapter BA = null;
    ArrayAdapter devListArrayAdapter;
    ListView lvDevices;
    ProgressBar progBar;
    BluetoothSocket btSocket = null;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    BlueConnectionService mBlueConnection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //display the logo during 3 seconds,
        new CountDownTimer(3000,1000){
            @Override
            public void onTick(long millisUntilFinished){}

            @Override
            public void onFinish(){ splashFinished(); }
        }.start();
    }


    protected void splashFinished(){
        BlueActivity.this.setContentView(R.layout.blue_layout);
        lvDevices = findViewById(R.id.blueListView);
        progBar = findViewById(R.id.progBar);

        BA = BluetoothAdapter.getDefaultAdapter();
        if (!BA.isEnabled()) {
            BA.enable();
            Toast.makeText(getApplicationContext(), "Bluetooth ON", Toast.LENGTH_SHORT).show();
        }

        devListArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        lvDevices.setAdapter(devListArrayAdapter);
        lvDevices.setOnItemClickListener(deviceListClickListener);

        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);
        BA.startDiscovery();
    }

    private AdapterView.OnItemClickListener deviceListClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick (AdapterView av, View v, int arg2, long arg3) {
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            BA.cancelDiscovery();
            //connect
            //change activity
            mBlueConnection = new BlueConnectionService();
            mBlueConnection.startClient(address);
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Toast.makeText(getApplicationContext(), "Scanning...",Toast.LENGTH_SHORT).show();
                devListArrayAdapter.clear();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Toast.makeText(getApplicationContext(), "Searching finished",Toast.LENGTH_SHORT).show();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                devListArrayAdapter.add(device.getName() + "\n" + device.getAddress());

            }


        }
    };

    public void searchDevices(View view){ BA.startDiscovery(); }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        try {
            btSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

}
