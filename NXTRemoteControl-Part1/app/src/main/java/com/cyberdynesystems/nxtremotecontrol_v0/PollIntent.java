package com.cyberdynesystems.nxtremotecontrol_v0;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;

/**
 * Created by geoffduong on 11/21/16.
 */

public class PollIntent extends AppCompatActivity {

    //Global variables------------------------------------------------------------------------------
    RobotController cv_robotController;
    BroadcastReceiver cv_btMonitor;
    BottomBar bottomBar;
    MyListAdapter lv_adapter;
    ListView lv_pollList;
    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_intent);
        
        cv_robotController = RobotController.getRobotController(PollIntent.this);
        cv_btMonitor = cv_robotController.cf_getBTMonitor();

        //Initalize variables
        bottomBar = (BottomBar) findViewById(R.id.pollBottomBar);
        int[] sensorImages = {R.drawable.nxt_distance_120,R.drawable.nxt_light_120,R.drawable.nxt_touch_120,
                              R.drawable.nxt_sound_120,R.drawable.nxt_servo_120,R.drawable.nxt_servo_120,
                              R.drawable.nxt_servo_120};

        //Set default tab
        BottomBarTab pollTab = bottomBar.getTabWithId(R.id.tab_Poll);
        bottomBar.setDefaultTab(pollTab.getId());

        //Bottom bar listener
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_connect) {
                    Intent lv_intent = new Intent(PollIntent.this, MainActivity.class);
                    startActivity(lv_intent);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                }
            }
        });

        lv_adapter = new MyListAdapter(this, sensorImages, "poll");

        lv_pollList = (ListView) findViewById(R.id.poll_listView);

        lv_pollList.setAdapter(lv_adapter);

        lv_pollList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position <= 3) {
                    final Dialog dialog = new Dialog(getApplicationContext());
                    dialog.setContentView(R.layout.list);

                    ListView lv_listView = (ListView) dialog.findViewById(R.id.vv_listView);
                    lv_listView.setAdapter(lv_adapter);

                    lv_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            dialog.cancel();
                        }
                    });
                    dialog.setCancelable(true);
                    dialog.setTitle("");
                    dialog.show();
                }
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
        //robotThread.terminate();
    }

    class RobotThread implements Runnable {
        private Thread t;
        private String threadName;
        private volatile boolean running;

        public RobotThread(String threadName) {
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
                            if(!cv_robotController.getConnectionStatus()) {
                                Intent lv_intent = new Intent(PollIntent.this, MainActivity.class);
                                startActivity(lv_intent);
                            }
                            else if(cv_robotController.getConnectionStatus()) {
                                cv_robotController.cf_getInputValues(0x00);
                                cv_robotController.cf_getInputValues(0x01);
                                cv_robotController.cf_getInputValues(0x02);
                                cv_robotController.cf_getInputValues(0x03);
                            }
                        }
                    });
                    Thread.sleep(5000);
                }
            }
            catch(InterruptedException e) {
                System.out.println(e.toString());
                running = false;
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
