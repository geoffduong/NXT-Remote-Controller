package com.cyberdynesystems.nxtremotecontrol_v0;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyPollListAdapter extends BaseAdapter {
    private Context c2v_context;
    private ArrayList<MyPollData> c2v_listData;
    private String layout;

    //CONSTRUCTORS----------------------------------------------------------------------------------
    public MyPollListAdapter(Context context, ArrayList<MyPollData> listItems, String layout) {
        this.c2v_context = context;
        this.c2v_listData = listItems;
        this.layout = layout;
    }
    //----------------------------------------------------------------------------------------------

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
        //Layout for Poll Intent
        if (convertView == null) {
            LayoutInflater mInflater =
                    (LayoutInflater) c2v_context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.poll_listcell, null);

            ImageView lv_sensorImage = (ImageView) convertView.findViewById(R.id.poll_listcell_image);
            TextView lv_sensorIndex = (TextView) convertView.findViewById(R.id.poll_listcell_index);
            TextView lv_sensorValue = (TextView) convertView.findViewById(R.id.poll_listcell_reading);

            lv_sensorImage.setImageResource(c2v_listData.get(position).getSensorImage());
            lv_sensorValue.setText("" + c2v_listData.get(position).getValue());
            //If position is at motor image
            if (position >= 4) {
                char index = (char) (position + 61);
                lv_sensorIndex.setText(Character.toString(index));
            } else
                lv_sensorIndex.setText(Integer.toString(position + 1));

        }

        return convertView;
    }
}
