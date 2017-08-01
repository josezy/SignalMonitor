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
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ValuesFragment extends Fragment {

    TextView tvValue1, tvValue2, tvValue3, tvValue4, tvValue5;
    Button btViewMore1, btViewMore2, btViewMore3, btViewMore4, btViewMore5;

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

        btViewMore1 = view.findViewById(R.id.btn_s1);
        btViewMore2 = view.findViewById(R.id.btn_s2);
        btViewMore3 = view.findViewById(R.id.btn_s3);
        btViewMore4 = view.findViewById(R.id.btn_s4);
        btViewMore5 = view.findViewById(R.id.btn_s5);
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
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    String[] values;
                    values = readMessage.split(",");
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
