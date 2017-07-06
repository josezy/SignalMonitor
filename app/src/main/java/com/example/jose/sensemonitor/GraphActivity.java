package com.example.jose.sensemonitor;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by jose on 5/07/17.
 */

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_layout);
        Log.i("INFO","Graph activity created");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("INFO","Graph activity started");
    }
}
