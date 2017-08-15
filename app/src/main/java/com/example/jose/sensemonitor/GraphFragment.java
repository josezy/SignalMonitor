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
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    String[] values;
                    values = readMessage.split(",");
                    if(values.length == 5){
                        double value = Double.valueOf(values[plotID-1].trim());
                        //Toast.makeText(getContext(), xPos+","+value,Toast.LENGTH_SHORT).show();
                        DataPoint point = new DataPoint(xPos, value);
                        mSeries.appendData(point, true, 50);
                        graph.addSeries(mSeries);
                        xPos = xPos + 0.5;
                    }
                    break;
            }
        }
    };
}
