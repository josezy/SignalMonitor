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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void showMore(View view){
        Intent intent = new Intent(MainActivity.this, GraphActivity.class);
        startActivity(intent);
    }
    
}
