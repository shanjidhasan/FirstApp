package com.example.firstapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class Fragment2 extends Fragment {
    private static final UUID MY_UUID = UUID.fromString("347bdc99-30e9-4095-89ea-7cb1812e14d6");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_2, container, false);

        BluetoothAdapter bluetoothAdapter;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        ListView listView = (ListView) view.findViewById(R.id.devicelistview);

        Set<BluetoothDevice> bt = bluetoothAdapter.getBondedDevices();
        String[] strings = new String[bt.size()];
        final BluetoothDevice[] btArray = new BluetoothDevice[bt.size()];
        int index  = 0;
        if(bt.size()>0)
        {
            for(BluetoothDevice device : bt){
                btArray[index] = device;
                strings[index] = device.getName();
                index++;
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.row,strings);
            listView.setAdapter(arrayAdapter);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Fragment3 fragment3 = new Fragment3();
                Bundle bundle = new Bundle();
                bundle.putInt("key", position);
                fragment3.setArguments(bundle);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.frgmntlay,fragment3,fragment3.getTag())
                        .commit();
            }
        });







        return view;
    }




}
