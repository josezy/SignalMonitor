package com.example.jose.sensemonitor;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

/**
 * Created by jose on 18/07/17.
 */

public class BlueActivity extends AppCompatActivity implements communicate{

    public BlueConnectionService mChatService = null;
    final int PERMISSION_ALL = 1;

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.splash);

        //display the logo during 2 seconds,
        new CountDownTimer(2000,1000){
            @Override
            public void onTick(long millisUntilFinished){}

            @Override
            public void onFinish(){
                getSupportActionBar().show();
                setContentView(R.layout.blue_layout);
                splashFinished();
            }
        }.start();
    }


    void splashFinished(){

        String[] PERMISSIONS = {Manifest.permission.BLUETOOTH,
                                Manifest.permission.BLUETOOTH_ADMIN,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }else{
            //BT activation
            if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                if (!BluetoothAdapter.getDefaultAdapter().enable()) {
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                }else{
                    BlueFragmentTransaction();
                }
            } else {
                BlueFragmentTransaction();
            }
        }


    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_ALL:{
                Log.d("[PERMS]", String.valueOf(grantResults.length));
                if (grantResults.length == 4 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED){
                    Log.d("[PERMS]", "todo bien todo bonito");
                    BluetoothAdapter.getDefaultAdapter().enable();
                    BlueFragmentTransaction();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.noPermissions, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == Activity.RESULT_OK){
                    BlueFragmentTransaction();
                }else{
                    Toast.makeText(getApplicationContext(), "Bluetooth activation rejected", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    private void BlueFragmentTransaction(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        BlueFragment fragment = new BlueFragment();
        transaction.replace(R.id.sample_content_fragment, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void ValuesFragmentTransaction(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ValuesFragment ValsFragment = new ValuesFragment();
        ft.replace(R.id.sample_content_fragment, ValsFragment);
        ft.commit();
    }


    @Override
    public void connectToDevice(BluetoothDevice device) {
        mChatService.connect(device, false);
    }

    public void setmChatServiceHandler(Handler mHand){
        mChatService.setHandler(mHand);
    }

    @Override
    protected void onStart() {
        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BlueConnectionService(BlueActivity.this);

        super.onStart();
    }

    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.BT_CONNECT_SUCCESSFUL);
        filter.addAction(Constants.BT_CONNECT_FAILED);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
        super.onResume();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case Constants.BT_CONNECT_FAILED:
                    Log.d("BICHO","Failed to connect!");
                    Toast.makeText(getApplicationContext(), R.string.btFailed, Toast.LENGTH_SHORT).show();
                    BlueFragmentTransaction();
                    break;
                case Constants.BT_CONNECT_SUCCESSFUL:
                    Log.d("BICHO","Conectado!");
                    ValuesFragmentTransaction();
                    break;
            }
        }
    };

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void showMore(View view){
        int plotID = 0;
        switch (view.getId()){
            case R.id.btn_s1:
                plotID = 1;
                break;
            case R.id.btn_s2:
                plotID = 2;
                break;
            case R.id.btn_s3:
                plotID = 3;
                break;
            case R.id.btn_s4:
                plotID = 4;
                break;
            case R.id.btn_s5:
                plotID = 5;
                break;
            default:break;
        }
        //Toast.makeText(getApplicationContext(), "Plot "+plotID, Toast.LENGTH_SHORT).show();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        GraphFragment graphyFragment = new GraphFragment();

        Bundle args = new Bundle();
        args.putInt(Constants.PLOT_ID, plotID);
        graphyFragment.setArguments(args);

        ft.replace(R.id.sample_content_fragment, graphyFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void disconnectBluetooth() {
        //BlueFragmentTransaction();
        finish();
    }

    public void requestDB(){
        if(mChatService.getState() == BlueConnectionService.STATE_CONNECTED){
            Log.d("[BICHO]","BT is connected... requesting data");
            String b = "d\n";
            mChatService.write(b.getBytes());
        }else{
            Toast.makeText(getApplicationContext(), R.string.bt_unavailable, Toast.LENGTH_SHORT).show();
        }
    }

    public void enableRT(boolean enable){
        if(mChatService.getState() == BlueConnectionService.STATE_CONNECTED){
            Log.d("[BICHO]","BT is connected... configuring RT");
            String b;
            if (enable) {
                b = "r\n";
            }else{
                b = "t\n";
            }
            mChatService.write(b.getBytes());
        }else{
            Toast.makeText(getApplicationContext(), R.string.bt_unavailable, Toast.LENGTH_SHORT).show();
        }
    }

    public void requestStatus() {
        if(mChatService.getState() == BlueConnectionService.STATE_CONNECTED){
            Log.d("[BICHO]","BT is connected... requesting status");
            String b = "s\n";
            mChatService.write(b.getBytes());
        }else{
            Toast.makeText(getApplicationContext(), R.string.bt_unavailable, Toast.LENGTH_SHORT).show();
        }
    }

    public void sendRsValues(List<String> values) {
        if(mChatService.getState() == BlueConnectionService.STATE_CONNECTED){
            Log.d("[BICHO]","BT is connected... sending Rs values");
            String b = "z";
            for (String val : values) {
                b = b.concat(val);
            }
            Log.d("[WRT]", b);
            mChatService.write(b.getBytes());
            Toast.makeText(getApplicationContext(), R.string.RsChanged, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), R.string.bt_unavailable, Toast.LENGTH_SHORT).show();
        }
    }

    public void eraseData() {
        if(mChatService.getState() == BlueConnectionService.STATE_CONNECTED){
            Log.d("[BICHO]","BT is connected... erasing data");
            String b = "e\n";
            mChatService.write(b.getBytes());
            Toast.makeText(getApplicationContext(), R.string.dataErased, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), R.string.bt_unavailable, Toast.LENGTH_SHORT).show();
        }
    }
}
