package com.example.jose.sensemonitor;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ValuesFragment extends Fragment {

    private static final String DATABASE_TASK = "db";
    private static final char TASK_DELIMITER = ':';

    TextView tvValue1, tvValue2, tvValue3, tvValue4, tvValue5;

    Button btDisconnect;
    Button btGetFile;
    TextView tvData;

    ProgressBar progBar;

    public ValuesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_values, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tvValue1 = view.findViewById(R.id.tv_s1_value);
        tvValue2 = view.findViewById(R.id.tv_s2_value);
        tvValue3 = view.findViewById(R.id.tv_s3_value);
        tvValue4 = view.findViewById(R.id.tv_s4_value);
        tvValue5 = view.findViewById(R.id.tv_s5_value);

        progBar = view.findViewById(R.id.file_progressBar);
        progBar.setIndeterminate(true);

        btDisconnect = view.findViewById(R.id.bt_disconnect);
        btDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BlueActivity) getActivity()).disconnectBluetooth();
            }
        });

        btGetFile = view.findViewById(R.id.bt_getFile);
        btGetFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO show progressBar and disable button
                progBar.setVisibility(View.VISIBLE);
                ((BlueActivity) getActivity()).requestDB();
            }
        });

        tvData = view.findViewById(R.id.tv_data);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onResume() {
        ((BlueActivity) getActivity()).setmChatServiceHandler(mHandler);
        super.onResume();
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_READ:
                    //byte[] readBuf = (byte[]) msg.obj;
                    //String message = new String(readBuf, 0, msg.arg1);
                    String message = (String) msg.obj;

                    String task = "none";
                    int idx = message.indexOf(TASK_DELIMITER);
                    if(idx != -1){
                        task = message.substring(0, idx);
                        message = message.substring(idx+1);
                    }

                    if(task.equals(DATABASE_TASK)){
                        // TODO save data to csv file
                        tvData.setText("(" + message.length() + ")");
                        tvData.append(message);

                        //TODO hide progressBar and enable button
                        progBar.setVisibility(View.GONE);
                    }

                    String[] values;
                    values = message.split(",");
                    if(values.length == 5){
                        tvValue1.setText(values[0].trim());
                        tvValue2.setText(values[1].trim());
                        tvValue3.setText(values[2].trim());
                        tvValue4.setText(values[3].trim());
                        tvValue5.setText(values[4].trim());
                    }
                    break;
            }
        }
    };

}
