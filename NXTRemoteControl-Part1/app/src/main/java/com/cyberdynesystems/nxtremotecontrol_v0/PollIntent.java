package com.cyberdynesystems.nxtremotecontrol_v0;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

/**
 * Created by geoffduong on 11/21/16.
 */

public class PollIntent extends AppCompatActivity {

    //Global variables------------------------------------------------------------------------------
    ListView lv_poll;
    BottomBar bottomBar;
    MyListAdapter lv_adapter;
    ListView lv_pollList;
    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_intent);

        //Initalize variables
        lv_poll = (ListView) findViewById(R.id.poll_listView);
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
            }
        });


        lv_adapter = new MyListAdapter(this, "poll");

        lv_pollList = (ListView) findViewById(R.id.poll_listView);

        lv_pollList.setAdapter(lv_adapter);
    }
}
