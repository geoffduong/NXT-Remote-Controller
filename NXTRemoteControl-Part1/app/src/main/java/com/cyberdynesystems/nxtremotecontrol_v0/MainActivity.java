package com.cyberdynesystems.nxtremotecontrol_v0;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {

    //Instance variables
    Button cv_connectBtn, closeBtn;
    ImageView bluetoothIcon;
    TextView cv_connectionStatus, cv_deviceName;
    RobotController controller;
    BottomBar bottomBar;
    TextView BatteryPercent;
    ProgressBar cv_batteryLevel;
    Double batteryPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize variables/Views
        cv_connectionStatus = (TextView) findViewById(R.id.tv_connectionStatus);
        cv_deviceName = (TextView) findViewById(R.id.vv_tvDeviceName);
        controller = RobotController.getRobotController(MainActivity.this);
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bluetoothIcon = (ImageView) findViewById(R.id.iv_bluetooth);
        cv_connectBtn = (Button) findViewById(R.id.cv_connectBtn);
        closeBtn = (Button) findViewById(R.id.closeBtn);
        BatteryPercent = (TextView) findViewById(R.id.tv_BatteryPercent);
        cv_batteryLevel = (ProgressBar) findViewById(R.id.batteryLevelProgess);

        //Set default tab to connect
        BottomBarTab connectBar = bottomBar.getTabWithId(R.id.tab_connect);
        bottomBar.setDefaultTab(connectBar.getId());

        //Bottom bar listener
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

        //Check if robot is already connected
        if (controller.getConnectionStatus()) {
            closeBtn.setEnabled(true);
            cv_connectBtn.setEnabled(false);
            bluetoothIcon.setImageResource(R.drawable.ic_connected_bluetooth);
            cv_connectionStatus.setText("Connected");
            batteryPercentage = controller.cf_battery();
            BatteryPercent.setText(batteryPercentage + "%");
            cv_batteryLevel.setProgress(batteryPercentage.intValue());
            cv_deviceName.setText("Connected to: " + controller.getRobotName());
        } else {
            closeBtn.setEnabled(false);
            cv_connectBtn.setEnabled(true);
            bluetoothIcon.setImageResource(R.drawable.ic_disconnected_bluetooth);
            cv_connectionStatus.setText("Not connected");
            BatteryPercent.setText("0%");
        }


        //Connect button listener
        cv_connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View v) {
                boolean done = controller.cf_findRobot(MainActivity.this);
                if (done)
                    updateConnectScreen();
            }
        });

        //Close button listener
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View v) {
                controller.handleDisconnected();
                cv_connectBtn.setEnabled(true);
                closeBtn.setEnabled(false);
                bluetoothIcon.setImageResource(R.drawable.ic_disconnected_bluetooth);
                BatteryPercent.setText("0.0%");
                cv_batteryLevel.setProgress(0);
                cv_connectionStatus.setText("Not Connected");
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateConnectScreen() {
        closeBtn.setEnabled(true);
        cv_connectBtn.setEnabled(false);
        bluetoothIcon.setImageResource(R.drawable.ic_connected_bluetooth);
        cv_connectionStatus.setText("Connected");
    }
}
