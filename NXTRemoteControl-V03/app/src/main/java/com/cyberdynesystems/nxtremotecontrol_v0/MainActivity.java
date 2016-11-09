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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {



    Button connectBtn, closeBtn;
    ImageView bluetoothIcon;
    RobotController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        controller = new RobotController();

        bluetoothIcon = (ImageView) findViewById(R.id.iv_bluetooth);
        bluetoothIcon.setImageResource(R.drawable.blackbluetooth);

        connectBtn.setEnabled(true);

        if (connectBtn.isEnabled()) {
            closeBtn.setEnabled(false);

        } else {
            closeBtn.setEnabled(true);
            bluetoothIcon.setImageResource(R.drawable.bluebluetooth);
        }



        connectBtn = (Button) findViewById(R.id.connectBtn);
        closeBtn = (Button) findViewById(R.id.closeBtn);

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.cf_findRobot(v);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.cf_findRobot(view);
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

            controller.cf_findBTList();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
