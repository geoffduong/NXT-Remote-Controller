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

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {



    Button connectBtn, closeBtn;
    ImageView bluetoothIcon;
    RobotController controller;
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //controller = new RobotController();

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        BottomBarTab driveBar = bottomBar.getTabWithId(R.id.tab_drive);
        bottomBar.setDefaultTab(driveBar.getId());

        bluetoothIcon = (ImageView) findViewById(R.id.iv_bluetooth);
        bluetoothIcon.setImageResource(R.drawable.blackbluetooth);

        //connectBtn.setEnabled(true);

//        if (connectBtn.isEnabled()) {
//            closeBtn.setEnabled(false);
//
//        } else {
//            closeBtn.setEnabled(true);
//            bluetoothIcon.setImageResource(R.drawable.bluebluetooth);
//        }



        connectBtn = (Button) findViewById(R.id.connectBtn);
        closeBtn = (Button) findViewById(R.id.closeBtn);

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.cf_findRobot(v);
            }
        });
        
        /*
         cv_btnConnect = (Button) findViewById(R.id.vv_btnConnect);
        cv_btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cv_btInterface = BluetoothAdapter.getDefaultAdapter();
                cv_pairedDevices = cv_btInterface.getBondedDevices();
                final ArrayList<BluetoothDevice> lv_arr = new ArrayList<BluetoothDevice>();
                lv_arr.addAll(cv_pairedDevices);

                MyListAdapter lv_adapter = new MyListAdapter(MainActivity.this, lv_arr);

                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.list);

                ListView lv_listView = (ListView) dialog.findViewById(R.id.vv_listView);
                lv_listView.setAdapter(lv_adapter);

                lv_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        cf_connectToRobot(lv_arr.get(position));
                    }
                });
                dialog.setCancelable(true);
                dialog.setTitle("");
                dialog.show();
                //cf_findRobot(view);
            }
        });
        */
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
