package com.example.jose.sensemonitor;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment {

    TextView tvTitle;
    GraphView graph;
    private LineGraphSeries<DataPoint> mSeries;
    private double xPos = 0;

    private int plotID = 0;

    public GraphFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graph, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        graph = view.findViewById(R.id.graph);
        mSeries = new LineGraphSeries<>();
        graph.addSeries(mSeries);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-1.5d);
        graph.getViewport().setMaxY(1.5d);
        graph.getViewport().setMinX(0d);
        graph.getViewport().setMaxX(10d);

        tvTitle = view.findViewById(R.id.tvGraphTitle);
        plotID = getArguments().getInt(Constants.PLOT_ID);
        tvTitle.append(" " + plotID);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        ((BlueActivity) getActivity()).setmChatServiceHandler(graphHandler);
        super.onResume();
    }

    private final Handler graphHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_READ:

                    String message = (String) msg.obj;

                    String task = "none";
                    int idx = message.indexOf(Constants.TASK_DELIMITER);
                    if(idx != -1){
                        task = message.substring(0, idx);
                        message = message.substring(idx+1);
                    }

                    if(task.equals(Constants.REALTIME_TASK)){
                        String[] values = message.split(",");
                        if(values.length == 5){
                            double value = Double.valueOf(values[plotID-1].trim());
                            mSeries.appendData(new DataPoint(xPos, value), true, 50);
                            graph.addSeries(mSeries);
                            xPos = xPos + 0.5;
                        }
                    }

                    break;
            }
        }
    };
}
