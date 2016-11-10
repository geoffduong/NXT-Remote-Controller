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
    private Context c2v_context;
    private ArrayList<BluetoothDevice> c2v_listData;

    public MyListAdapter(Context context, ArrayList<BluetoothDevice> listItems) {
        this.c2v_context = context;
        this.c2v_listData = listItems;
    }

    @Override
    public int getCount() {
        return c2v_listData.size();
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
        if(convertView == null) {
            LayoutInflater mInflater =
                    (LayoutInflater) c2v_context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.cell, null);
        }
        TextView lv_tvList = (TextView) convertView.findViewById(R.id.vv_tvList);
        lv_tvList.setText(c2v_listData.get(position).getName().toString());
        return convertView;
    }
}

