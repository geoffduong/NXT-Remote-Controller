package com.cyberdynesystems.nxtremotecontrol_v0;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
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
    Button cv_connectBtn, cv_closeBtn;
    ImageView bluetoothIcon;
    TextView cv_connectionStatus, cv_deviceName;
    RobotController cv_robotController;
    BottomBar bottomBar;
    TextView batteryPercent;
    ProgressBar cv_batteryLevel;
    Double batteryPercentage;
    BroadcastReceiver cv_btMonitor;
    RobotThread robotThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize variables/Views
        cv_connectionStatus = (TextView) findViewById(R.id.tv_connectionStatus);
        cv_deviceName = (TextView) findViewById(R.id.vv_tvDeviceName);
        cv_robotController = RobotController.getRobotController(MainActivity.this);
        cv_btMonitor = cv_robotController.cf_getBTMonitor();
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bluetoothIcon = (ImageView) findViewById(R.id.iv_bluetooth);
        cv_connectBtn = (Button) findViewById(R.id.cv_connectBtn);
        cv_closeBtn = (Button) findViewById(R.id.closeBtn);
        batteryPercent = (TextView) findViewById(R.id.tv_BatteryPercent);
        cv_batteryLevel = (ProgressBar) findViewById(R.id.batteryLevelProgess);

        //Thread for updating battery level and connection status
        robotThread = new RobotThread("robotThread");
        robotThread.start();

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

                if (tabId == R.id.tab_Poll) {
                    Intent lv_intent = new Intent(MainActivity.this, PollIntent.class);
                    startActivity(lv_intent);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                }
            }
        });

        //Check if robot is already connected
        updateConnectScreen(cv_robotController.getConnectionStatus());

        //Connect button listener
        cv_connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View v) {
                boolean done = cv_robotController.cf_findRobot(MainActivity.this);
            }
        });

        //Close button listener
        cv_closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View v) {
                cv_robotController.cf_handleDisconnected();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(cv_btMonitor, new IntentFilter("android.bluetooth.device.action.ACL_CONNECTED"));
        registerReceiver(cv_btMonitor, new IntentFilter("android.bluetooth.device.action.ACL_DISCONNECTED"));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(cv_btMonitor);
        robotThread.terminate();
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

    private void updateConnectScreen(boolean connected) {
        if(connected) {
            cv_closeBtn.setEnabled(true);
            cv_connectBtn.setEnabled(false);
            bluetoothIcon.setImageResource(R.drawable.ic_connected_bluetooth);
            batteryPercentage = cv_robotController.cf_battery();
            batteryPercent.setText(batteryPercentage + "%");
            cv_batteryLevel.setProgress(batteryPercentage.intValue());
            cv_connectionStatus.setText("Connected");
            cv_deviceName.setText("Connected to: " + cv_robotController.getRobotName());
        }
        else if(!connected) {
            cv_connectBtn.setEnabled(true);
            cv_closeBtn.setEnabled(false);
            bluetoothIcon.setImageResource(R.drawable.ic_disconnected_bluetooth);
            batteryPercent.setText("0.0%");
            cv_batteryLevel.setProgress(0);
            cv_connectionStatus.setText("Not Connected");
        }
    }


    class RobotThread implements Runnable {
        private Thread t;
        private String threadName;
        private volatile boolean running;

        RobotThread(String threadName) {
            this.threadName = threadName;
            running = true;
        }

        public void terminate() {
            running = false;
        }

        public void run() {
            try {
                while(running) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateConnectScreen(cv_robotController.getConnectionStatus());
                        }
                    });
                    Thread.sleep(1000);
                }
            }
            catch(InterruptedException e) {
                System.out.println(e.toString());
            }
        }

        public void start() {
            if(t == null) {
                t = new Thread(this, threadName);
                t.start();
            }
        }
    }
}


