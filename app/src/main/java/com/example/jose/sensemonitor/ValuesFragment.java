package com.example.jose.sensemonitor;


import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.os.EnvironmentCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ValuesFragment extends Fragment {

    TextView tvValue1, tvValue2, tvValue3, tvValue4, tvValue5;
    EditText etR1, etR2, etR3, etR4, etR5, etRata;

    Button btGetFile, btEnableRT, btGetRs, btSetRs;
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

        etR1 = view.findViewById(R.id.res_val1);
        etR2 = view.findViewById(R.id.res_val2);
        etR3 = view.findViewById(R.id.res_val3);
        etR4 = view.findViewById(R.id.res_val4);
        etR5 = view.findViewById(R.id.res_val5);

        etRata = view.findViewById(R.id.et_rata);

        progBar = view.findViewById(R.id.file_progressBar);
        progBar.setIndeterminate(true);

        btGetFile = view.findViewById(R.id.bt_getFile);
        btGetFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODOne disable button
                btGetFile.setEnabled(false);
                progBar.setVisibility(View.VISIBLE);
                ((BlueActivity) getActivity()).requestDB();
            }
        });

        btEnableRT = view.findViewById(R.id.bt_enableRT);
        btEnableRT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btEnableRT.getText().equals(getString(R.string.bt_enableRT))){
                    btEnableRT.setText(R.string.bt_disableRT);
                    //Toast.makeText(getContext(), "ON", Toast.LENGTH_SHORT).show();
                    ((BlueActivity) getActivity()).enableRT(true);
                }else{
                    btEnableRT.setText(R.string.bt_enableRT);
                    //Toast.makeText(getContext(), "OFF", Toast.LENGTH_SHORT).show();
                    ((BlueActivity) getActivity()).enableRT(false);
                }
            }
        });

        btGetRs = view.findViewById(R.id.bt_getRs);
        btSetRs = view.findViewById(R.id.bt_setRs);

        btGetRs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BlueActivity) getActivity()).requestStatus();
            }
        });

        btSetRs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODOne check for empty fields
                String etR1_text = etR1.getText().toString().trim();
                String etR2_text = etR2.getText().toString().trim();
                String etR3_text = etR3.getText().toString().trim();
                String etR4_text = etR4.getText().toString().trim();
                String etR5_text = etR5.getText().toString().trim();
                String etRata_text = etRata.getText().toString().trim();

                if(etR1_text.isEmpty()||etR2_text.isEmpty()||etR3_text.isEmpty()||etR4_text.isEmpty()||etR5_text.isEmpty()||etRata_text.isEmpty()) {
                    Toast.makeText(getContext(), R.string.empty_fields, Toast.LENGTH_SHORT).show();
                } else {
                    //send res values
                    List<String> Values = new ArrayList<>();
                    Values.add(etR1_text+',');
                    Values.add(etR2_text+',');
                    Values.add(etR3_text+',');
                    Values.add(etR4_text+',');
                    Values.add(etR5_text+',');
                    Values.add(etRata_text+'\n');
                    ((BlueActivity) getActivity()).sendRsValues(Values);
                }

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
        ((BlueActivity) getActivity()).requestStatus();
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
                    int idx = message.indexOf(Constants.TASK_DELIMITER);
                    if(idx != -1){
                        task = message.substring(0, idx);
                        message = message.substring(idx+1);
                    }

                    switch (task) {
                        case Constants.DATABASE_TASK:
                            saveCSVdata(message);
                            tvData.setText("(" + message.length() + ")");
                            //tvData.append(message);

                            //TODOne enable button
                            btGetFile.setEnabled(true);
                            progBar.setVisibility(View.GONE);

                            break;
                        case Constants.REALTIME_TASK: {
                            String[] values;
                            values = message.split(",");
                            if (values.length == 5) {
                                tvValue1.setText(values[0].trim());
                                tvValue2.setText(values[1].trim());
                                tvValue3.setText(values[2].trim());
                                tvValue4.setText(values[3].trim());
                                tvValue5.setText(values[4].trim());
                            }

                            break;
                        }
                        case Constants.UPDATE_STATUS: {
                            String[] values;
                            values = message.split(",");
                            if (values.length == 7) {
                                if (values[0].trim().equals("1")) {
                                    btEnableRT.setText(R.string.bt_disableRT);
                                } else {
                                    btEnableRT.setText(R.string.bt_enableRT);
                                }
                                etR1.setText(values[1].trim());
                                etR2.setText(values[2].trim());
                                etR3.setText(values[3].trim());
                                etR4.setText(values[4].trim());
                                etR5.setText(values[5].trim());
                                etRata.setText(values[6].trim());
                                Toast.makeText(getContext(), R.string.RsUpdated, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Error reading status", Toast.LENGTH_SHORT).show();
                            }

                            break;
                        }
                        default:
                            Toast.makeText(getContext(), R.string.invalid_task, Toast.LENGTH_SHORT).show();
                            break;
                    }

                    break;
            }
        }
    };

    private void saveCSVdata(String message) {
        List<String[]> data = new ArrayList<>();
        String[] rows = message.split(";");

        for (String row : rows) {
            data.add(row.split(","));
        }

        switch (Environment.getExternalStorageState()){
            case Environment.MEDIA_MOUNTED:
                Log.d("TAG", "Media mounted");
                break;
            case Environment.MEDIA_MOUNTED_READ_ONLY:
                Log.d("TAG", "READ ONLY");
                break;

        }

        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "SignalMonitorData.csv";//TODO name file with date and time
        String filePath = baseDir + File.separator + fileName;
        Log.d("Path: ", filePath);
        File f = new File(filePath );
        CSVWriter writer;

        if(!f.isDirectory()){
            try {
                writer = new CSVWriter(new FileWriter(filePath));
                writer.writeAll(data);
                writer.close();
                Toast.makeText(getContext(), R.string.file_saved, Toast.LENGTH_LONG).show();
                Log.d("[BICHO]", "File saved??");
            } catch (IOException e) {
                Log.d("[BICHO]", "Writer ERROR");
                e.printStackTrace();
            }
        }


    }

}
