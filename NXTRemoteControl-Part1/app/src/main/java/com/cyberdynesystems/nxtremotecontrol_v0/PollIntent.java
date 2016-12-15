package com.cyberdynesystems.nxtremotecontrol_v0;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    //MyListAdapter lv_adapter;
    ArrayList<MyPollData> cv_pollData;
    ListView cv_pollList;
    MyPollListAdapter cv_pollAdapter;
    RobotThread robotThread;
    int port0, port1, port2, port3;
    final int[] sensorImages = {R.drawable.nxt_distance_120,R.drawable.nxt_light_120,R.drawable.nxt_touch_120,
            R.drawable.nxt_sound_120,R.drawable.nxt_servo_120,R.drawable.nxt_servo_120,
            R.drawable.nxt_servo_120};
    final int[] changeSensorImages = {R.drawable.nxt_distance_120,R.drawable.nxt_light_120,R.drawable.nxt_touch_120,
            R.drawable.nxt_sound_120};
    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_intent);
        
        cv_robotController = RobotController.getRobotController(PollIntent.this);
        cv_btMonitor = cv_robotController.cf_getBTMonitor();

        //Initalize variables
        bottomBar = (BottomBar) findViewById(R.id.pollBottomBar);

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
                if (tabId == R.id.tab_driveByDraw) {
                    Intent lv_intent = new Intent(PollIntent.this, GridDriveIntent.class);
                    startActivity(lv_intent);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                }
                if (tabId == R.id.tab_drive) {
                    Intent lv_intent = new Intent(PollIntent.this, DriveIntent.class);
                    startActivity(lv_intent);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                }
            }
        });

        cv_pollData = new ArrayList<>();
        for(int i=0; i<sensorImages.length; i++)
            cv_pollData.add(new MyPollData(sensorImages[i], true));

        cv_pollAdapter = new MyPollListAdapter(this, cv_pollData);
        //lv_adapter = new MyListAdapter(this, sensorImages, "poll");
        cv_pollList = (ListView) findViewById(R.id.poll_listView);
        cv_pollList.setAdapter(cv_pollAdapter);


        cv_pollList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final int listPosition = position;

                if (position >= 0 && position <= 3) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PollIntent.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogTitle = inflater.inflate(R.layout.dialog_header, null);
                    builder.setCustomTitle(dialogTitle);

                    ListView modeList = new ListView(PollIntent.this);
                    final MyListAdapter lv_adapter = new MyListAdapter(PollIntent.this, changeSensorImages, "sensorChange");
                    modeList.setAdapter(lv_adapter);

                    builder.setView(modeList);
                    final Dialog dialog = builder.create();

                    modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            cv_pollData.get(listPosition).setSensorImage(changeSensorImages[position]);
                            cv_pollAdapter.notifyDataSetChanged();
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                    dialog.getWindow().setLayout(800, 1030);
                }
            }
        });

        //cv_robotController.cf_setInputMode();
        cv_robotController.cf_setInputMode(0x07, 0x01);
        cv_robotController.cf_setInputMode(0x05, 0x02);
        cv_robotController.cf_setInputMode(0x01, 0x03);

        port0 = sensorImages[0];
        port1 = sensorImages[1];
        port2 = sensorImages[2];
        port3 = sensorImages[3];

        robotThread = new RobotThread("robotThread");
        robotThread.start();
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
        cv_robotController.cf_setInputMode(0x06, 0x02);
        robotThread.terminate();
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
//                            if(!cv_robotController.getConnectionStatus()) {
//                                Intent lv_intent = new Intent(PollIntent.this, MainActivity.class);
//                                startActivity(lv_intent);
//                            }
//                            else if(cv_robotController.getConnectionStatus()) {
                                for(int i=0; i<4; i++) {
                                    MyPollData lv_mpd = cv_pollData.get(i);
                                    if(lv_mpd.getActive() == true) {
                                        switch (lv_mpd.getSensorImage()) {
                                            case R.drawable.nxt_distance_120:
                                                lv_mpd.setValue(cv_robotController.cf_getInputValues(0x00));
                                                break;
                                            case R.drawable.nxt_sound_120:
                                                lv_mpd.setValue(cv_robotController.cf_getInputValues(0x01));
                                                break;
                                            case R.drawable.nxt_light_120:
                                                lv_mpd.setValue(cv_robotController.cf_getInputValues(0x02));
                                                break;
                                            case R.drawable.nxt_touch_120:
                                                lv_mpd.setValue(cv_robotController.cf_getInputValues(0x03));
                                                break;
                                            default:
                                                break;
                                        }
                                        cv_pollAdapter.notifyDataSetChanged();
                                    }
                                    else {
                                        cv_pollData.get(i).setValue(0);
                                    }
                                }
//                            }
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
