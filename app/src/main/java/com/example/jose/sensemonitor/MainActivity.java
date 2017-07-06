package com.example.jose.sensemonitor;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter BA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //display the logo during 4 seconds,
        new CountDownTimer(4000,1000){
            @Override
            public void onTick(long millisUntilFinished){}

            @Override
            public void onFinish(){
                MainActivity.this.setContentView(R.layout.activity_main);
            }
        }.start();

        BA = BluetoothAdapter.getDefaultAdapter();

    }

    public void showMore(View view){
        Intent intent = new Intent(MainActivity.this, GraphActivity.class);
        startActivity(intent);
    }
    
}
