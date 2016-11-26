package com.cyberdynesystems.nxtremotecontrol_v0;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListAdapter extends BaseAdapter {

    //Instance variables
    private Context c2v_context;
    private ArrayList<BluetoothDevice> c2v_listData;
    private String layout;

    //CONSTRUCTORS----------------------------------------------------------------------------------
    public MyListAdapter(Context context, ArrayList<BluetoothDevice> listItems, String layout) {
        this.c2v_context = context;
        this.c2v_listData = listItems;
        this.layout = layout;
    }

    public MyListAdapter(Context context, String layout)
    {
        this.c2v_context = context;
        this.layout = layout;
    }
    //----------------------------------------------------------------------------------------------

    @Override
    public int getCount() {
        if (layout.equalsIgnoreCase("main"))
            return c2v_listData.size();
        if (layout.equalsIgnoreCase("poll"))
            return 5;
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return c2v_listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Layout for device list in MainActivity
        if(convertView == null && layout.equalsIgnoreCase("main")) {
            LayoutInflater mInflater =
                    (LayoutInflater) c2v_context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.cell, null);
            TextView lv_tvList = (TextView) convertView.findViewById(R.id.vv_tvList);
            lv_tvList.setText(c2v_listData.get(position).getName().toString());
        }

        //Layout for Poll Intent
        if(convertView == null && layout.equalsIgnoreCase("poll")) {
            LayoutInflater mInflater =
                    (LayoutInflater) c2v_context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.poll_listcell, null);
        }

        return convertView;
    }
}

