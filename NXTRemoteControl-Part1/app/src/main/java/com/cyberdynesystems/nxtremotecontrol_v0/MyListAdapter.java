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

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyListAdapter extends BaseAdapter {

    //Instance variables
    private Context c2v_context;
    private ArrayList<BluetoothDevice> c2v_listData;
    private String layout;
    private int[] sensorImages;

    //CONSTRUCTORS----------------------------------------------------------------------------------
    public MyListAdapter(Context context, ArrayList<BluetoothDevice> listItems, String layout) {
        this.c2v_context = context;
        this.c2v_listData = listItems;
        this.layout = layout;
    }

    public MyListAdapter(Context context, int[] sensorImages, String layout) {
        this.c2v_context = context;
        this.sensorImages = sensorImages;
        this.layout = layout;
    }
    //----------------------------------------------------------------------------------------------

    @Override
    public int getCount() {
        if (layout.equalsIgnoreCase("main"))
            return c2v_listData.size();
        if (layout.equalsIgnoreCase("poll") || layout.equalsIgnoreCase("sensorChange"))
            return sensorImages.length;
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
        if (convertView == null && layout.equalsIgnoreCase("main")) {
            LayoutInflater mInflater =
                    (LayoutInflater) c2v_context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.cell, null);
            TextView lv_tvList = (TextView) convertView.findViewById(R.id.vv_tvList);
            lv_tvList.setText(c2v_listData.get(position).getName().toString());
            TextView lv_tvAddress = (TextView) convertView.findViewById(R.id.vv_tvList);
            lv_tvAddress.setText(c2v_listData.get(position).getAddress().toString());
        }

        //Layout for Poll Intent
        if (convertView == null && layout.equalsIgnoreCase("poll")) {
            LayoutInflater mInflater =
                    (LayoutInflater) c2v_context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.poll_listcell, null);
            ImageView lv_sensorImage = (ImageView) convertView.findViewById(R.id.poll_listcell_image);
            TextView lv_sensorIndex = (TextView) convertView.findViewById(R.id.poll_listcell_index);
            lv_sensorImage.setImageResource(sensorImages[position]);
            //If position is at motor image
            if (position >= 4) {
                char index = (char) (position + 61);
                lv_sensorIndex.setText(Character.toString(index));
            }
            else
                lv_sensorIndex.setText(Integer.toString(position + 1));
        }

        //Layout for Changing sensor
        if (convertView == null && layout.equalsIgnoreCase("sensorChange")) {
                LayoutInflater mInflater =
                        (LayoutInflater) c2v_context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.cell, null);
                TextView lv_tvList = (TextView) convertView.findViewById(R.id.vv_tvList);
                ImageView lv_sensorImage = (ImageView) convertView.findViewById(R.id.dialogPicture);

                lv_sensorImage.setImageResource(sensorImages[position]);
            switch (position) {
                case 0:
                    lv_tvList.setText("Distance Sensor");
                    break;
                case 1:
                    lv_tvList.setText("Light Sensor");
                    break;
                case 2:
                    lv_tvList.setText("Touch Sensor");
                    break;
                case 3:
                    lv_tvList.setText("Sound Sensor");
                    break;
            }

            }

        return convertView;
    }
}

