package com.example.jose.sensemonitor;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by jose on 5/07/17.
 */

public class GraphActivity extends AppCompatActivity {

    GraphView graph;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_layout);
        graph = (GraphView) findViewById(R.id.graph);
        Log.i("INFO","Graph activity created");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);
        Log.i("INFO","Graph activity started");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("INFO","Graph activity destroyed");
    }
}
