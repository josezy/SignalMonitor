package com.example.jose.sensemonitor;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by jose on 21/07/17.
 */

public class BlueConnectionService {

    ConnectThread mConnectThread;
    ConnectedThread mConnectedThread;
    static private final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public void startClient(String address){

        /*mProgressDialog = ProgressDialog.show(mContext,"Connecting Bluetooth"
                ,"Please Wait...",true);*/

        mConnectThread = new ConnectThread(address);
        mConnectThread.start();
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;

        public ConnectThread(String address) {
            BluetoothSocket tmp = null;

            try {
                BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
                tmp = device.createRfcommSocketToServiceRecord(myUUID);
            } catch (IOException e) { e.printStackTrace();}
            mmSocket = tmp;
        }

        public void run() {
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) { closeException.printStackTrace();}
//                return;
            }
            // Do work to manage the connection (in a separate thread)
            mConnectedThread = new ConnectedThread(mmSocket);
            mConnectedThread.start();
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { e.printStackTrace();}
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { e.printStackTrace(); }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes; // bytes returned from read()

            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    //handle received messages
                    /*mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();*/
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { e.printStackTrace(); }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { e.printStackTrace(); }
        }
    }


}
