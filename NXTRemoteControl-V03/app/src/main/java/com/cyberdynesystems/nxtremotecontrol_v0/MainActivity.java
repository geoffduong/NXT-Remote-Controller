package com.cyberdynesystems.nxtremotecontrol_v0;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    final String CV_ROBOTNAME = "NXT";
    TextView cv_tvHello;

    // BT Variables
    private BluetoothAdapter cv_btInterface;
    private Set<BluetoothDevice> cv_pairedDevices;
    private BluetoothSocket cv_socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView cvTextView = (TextView) findViewById(R.id.vv_tvHello);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cf_findRobot(view);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            cf_findBTList();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // modify from cf_findRobot
    private void cf_findBTList() {
        try {
            cv_btInterface = BluetoothAdapter.getDefaultAdapter();
            cv_pairedDevices = cv_btInterface.getBondedDevices();
            Iterator<BluetoothDevice> lv_it = cv_pairedDevices.iterator();
            while (lv_it.hasNext())  {
                BluetoothDevice lv_bd = lv_it.next();
                if (lv_bd.getName().equalsIgnoreCase(CV_ROBOTNAME)) {
                    cv_tvHello.setText(CV_ROBOTNAME + " is in paired list");
                    return;
                }
            }
            cv_tvHello.setText(CV_ROBOTNAME + " is NOT in paired list");
        }
        catch (Exception e) {
            cv_tvHello.setText("Failed in findRobot() " + e.getMessage());
        }
    }

    // page 390
    private void cf_findRobot(View v) {
        try {
            cv_btInterface = BluetoothAdapter.getDefaultAdapter();
            cv_pairedDevices = cv_btInterface.getBondedDevices();
            Iterator<BluetoothDevice> lv_it = cv_pairedDevices.iterator();
            while (lv_it.hasNext())  {
                BluetoothDevice lv_bd = lv_it.next();
                if (lv_bd.getName().equalsIgnoreCase(CV_ROBOTNAME)) {
                    cf_connectToRobot(lv_bd);
                    return;
                }
            }
        }
        catch (Exception e) {
            cv_tvHello.setText("Failed in findRobot() " + e.getMessage());
            Log.e("", "\"Failed in findRobot() \" + e.getMessage()");
        }
    }

    // page 391
    private void cf_connectToRobot(BluetoothDevice bd) {
        try  {
            cv_socket = bd.createRfcommSocketToServiceRecord
                    (UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            cv_socket.connect();
            cv_tvHello.setText("Connect to " + bd.getName() + " at " + bd.getAddress());
        }
        catch (Exception e) {
            cv_tvHello.setText("Error interacting with remote device [" +
                    e.getMessage() + "]");
            Log.e("", "\"Failed in findRobot() \" + e.getMessage()");
        }
    }
}
