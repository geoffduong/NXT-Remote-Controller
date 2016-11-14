package com.cyberdynesystems.nxtremotecontrol_v0;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {



    Button cv_connectBtn, closeBtn;
    ImageView bluetoothIcon;
    RobotController controller;
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //controller = RobotController.getRobotController(MainActivity.this);
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        BottomBarTab connectBar = bottomBar.getTabWithId(R.id.tab_connect);
        bottomBar.setDefaultTab(connectBar.getId());

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_drive) {
                    Intent lv_intent = new Intent(MainActivity.this, DriveIntent.class);
                    startActivity(lv_intent);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                }

            }
        });

        bluetoothIcon = (ImageView) findViewById(R.id.iv_bluetooth);
        bluetoothIcon.setImageResource(R.drawable.blackbluetooth);

        //cv_connectBtn.setEnabled(true);

//        if (cv_connectBtn.isEnabled()) {
//            closeBtn.setEnabled(false);
//
//        } else {
//            closeBtn.setEnabled(true);
//            bluetoothIcon.setImageResource(R.drawable.bluebluetooth);
//        }



        cv_connectBtn = (Button) findViewById(R.id.cv_connectBtn);
        closeBtn = (Button) findViewById(R.id.closeBtn);

        cv_connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.cf_findRobot();
                Intent lv_intent = new Intent(MainActivity.this, DriveIntent.class);
                startActivity(lv_intent);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
