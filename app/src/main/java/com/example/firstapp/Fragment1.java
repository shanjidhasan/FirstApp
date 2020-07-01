package com.example.firstapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class Fragment1 extends Fragment {



    BluetoothAdapter bluetoothAdapter;
    FrameLayout frml;
    private static final String APP_NAME = "FirstApp";
    private static final UUID MY_UUID = UUID.fromString("347bdc99-30e9-4095-89ea-7cb1812e14d6");
    static final int STATE_LISTINING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;
    static final int STATE_MESSAGE_RECEIVED = 5;
    SendReceive sendReceive;

    TextView showmsgbox1;
    EditText getmsgbox1;
    Button sendbtn1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        showmsgbox1 = (TextView) view.findViewById(R.id.msgbox1);
        getmsgbox1 = (EditText) view.findViewById(R.id.msgedt1);
        sendbtn1 = (Button) view.findViewById(R.id.sendmsg1);

        sendbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = String.valueOf(getmsgbox1.getText());
                sendReceive.write(string.getBytes());
            }
        });

        ServerClass serverClass = new ServerClass();
        serverClass.start();





        return view;
    }
    private class ServerClass extends Thread
    {
        private BluetoothServerSocket serverSocket;

        public ServerClass(){
            try {
                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME,MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run()
        {
            BluetoothSocket socket = null;

            while (socket == null)
            {
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(socket!=null)
                {
                    sendReceive = new SendReceive(socket);
                    sendReceive.start();
                }
            }
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            switch (msg.what){
                case STATE_LISTINING:
                    //
                    break;
                case STATE_CONNECTING:
                    //
                    break;
                case STATE_CONNECTED:
                    //
                    break;
                case STATE_CONNECTION_FAILED:
                    //
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuff = (byte[]) msg.obj;
                    String tempMsg = new String(readBuff,0,msg.arg1);
                    showmsgbox1.setText(tempMsg);
                    break;
            }

            return false;
        }
    });


    private class SendReceive extends Thread {

        private BluetoothSocket bluetoothSocket;
        private InputStream inputStream;
        private OutputStream outputStream;

        TextView msgtv;
        public SendReceive(BluetoothSocket socket)
        {
            bluetoothSocket = socket;
            InputStream tempIn = null;
            OutputStream tempOut = null;

            try {
                tempIn = bluetoothSocket.getInputStream();
                tempOut = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inputStream = tempIn;
            outputStream = tempOut;
        }

        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;

            while (true)
            {
                try {

                    bytes = inputStream.read(buffer);
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes){
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
