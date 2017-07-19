package com.example.jose.sensemonitor;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by jose on 18/07/17.
 */

public class BlueActivity extends Activity {

    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    ListView deviceList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        deviceList = (ListView) findViewById(R.id.blueListView);
        //display the logo during 4 seconds,
        new CountDownTimer(4000,1000){
            @Override
            public void onTick(long millisUntilFinished){}

            @Override
            public void onFinish(){
                splashFinished();
            }
        }.start();
    }

    protected void splashFinished(){
        BlueActivity.this.setContentView(R.layout.blue_layout);

        BA = BluetoothAdapter.getDefaultAdapter();
        if (!BA.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 0);
        }

        pairedDevices = BA.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size() > 0){

            for(BluetoothDevice bt : pairedDevices){
                list.add(bt.getName() + "\n" + bt.getAddress());
            }

        }else{
            Toast.makeText(getApplicationContext(), "No Paired Devices",Toast.LENGTH_SHORT).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        deviceList.setAdapter(adapter);

    }
}
