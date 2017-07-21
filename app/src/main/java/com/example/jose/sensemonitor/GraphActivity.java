package com.example.jose.sensemonitor;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by jose on 5/07/17.
 */

public class GraphActivity extends AppCompatActivity {

    GraphView graph;
    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_layout);
        graph = (GraphView) findViewById(R.id.graph);
        title = (TextView) findViewById(R.id.tv_graph_ly);
        Log.i("INFO","Graph activity created");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int plotID = bundle.getInt("PLOT_ID");
        switch (plotID){
            case R.id.btn_s1:
                title.setText("Plot 1");
                break;
            case R.id.btn_s2:
                title.setText("Plot 2");
                break;
            case R.id.btn_s3:
                title.setText("Plot 3");
                break;
            case R.id.btn_s4:
                title.setText("Plot 4");
                break;
            case R.id.btn_s5:
                title.setText("Plot 5");
                break;
            default:
                break;
        }
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
