package com.cyberdynesystems.nxtremotecontrol_v0;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * Created by geoffduong on 11/9/16.
 */

public class RobotController extends AppCompatActivity {

    final String CV_ROBOTNAME = "NXT06";
    TextView cv_tvHello;

    // BT Variables
    private BluetoothAdapter cv_btInterface;
    private Set<BluetoothDevice> cv_pairedDevices;
    private BluetoothSocket cv_socket;


    public RobotController() {
        super();
        cv_tvHello = (TextView) findViewById(R.id.vv_tvHello);
        cv_btInterface = BluetoothAdapter.getDefaultAdapter();
        cv_pairedDevices = cv_btInterface.getBondedDevices();

    }



    // page 390
    public void cf_findRobot(View v) {
        try {

            Iterator<BluetoothDevice> lv_it = cv_pairedDevices.iterator();
            while (lv_it.hasNext()) {
                BluetoothDevice lv_bd = lv_it.next();
                if (lv_bd.getName().equalsIgnoreCase(CV_ROBOTNAME)) {
                    cf_connectToRobot(lv_bd);
                    return;
                }
            }
        } catch (Exception e) {
            cv_tvHello.setText("Failed in findRobot() " + e.getMessage());
            Log.e("", "\"Failed in findRobot() \" + e.getMessage()");
        }
    }

    // page 391
    public void cf_connectToRobot(BluetoothDevice bd) {
        try {
            cv_socket = bd.createRfcommSocketToServiceRecord
                    (UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            cv_socket.connect();
            cv_tvHello.setText("Connect to " + bd.getName() + " at " + bd.getAddress());
        } catch (Exception e) {
            cv_tvHello.setText("Error interacting with remote device [" +
                    e.getMessage() + "]");
            Log.e("", "\"Failed in findRobot() \" + e.getMessage()");
        }
    }

    // modify from cf_findRobot
    public void cf_findBTList() {
        try {
            Iterator<BluetoothDevice> lv_it = cv_pairedDevices.iterator();
            while (lv_it.hasNext()) {
                BluetoothDevice lv_bd = lv_it.next();
                if (lv_bd.getName().equalsIgnoreCase(CV_ROBOTNAME)) {
                    cv_tvHello.setText(CV_ROBOTNAME + " is in paired list");
                    return;
                }
            }
            cv_tvHello.setText(CV_ROBOTNAME + " is NOT in paired list");
        } catch (Exception e) {
            cv_tvHello.setText("Failed in findRobot() " + e.getMessage());
        }
    }
}
