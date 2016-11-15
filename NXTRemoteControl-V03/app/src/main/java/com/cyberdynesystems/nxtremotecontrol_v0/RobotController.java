package com.cyberdynesystems.nxtremotecontrol_v0;

import android.app.Application;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * Created by geoffduong on 11/9/16.
 */

public class RobotController extends Application {
    private static RobotController singleton;

    final String CV_ROBOTNAME = "NXT06";

    // BT Variables
    private BluetoothAdapter cv_btInterface;
    private Set<BluetoothDevice> cv_pairedDevices;
    private BluetoothSocket cv_socket;
    private BroadcastReceiver cv_btMonitor = null;
    private InputStream cv_is;
    private OutputStream cv_os;

    public void setCv_bConnected(boolean cv_bConnected) {
        this.cv_bConnected = cv_bConnected;
    }

    private boolean cv_bConnected;

    private RobotController() {
        cv_btInterface = BluetoothAdapter.getDefaultAdapter();
        cv_pairedDevices = cv_btInterface.getBondedDevices();
        cv_bConnected = false;
    }

    public synchronized static RobotController getRobotController(Context context) {
        if(null == singleton) {
            singleton = new RobotController();
        }
        return singleton;
    }

    // page 390
    public synchronized String cf_findRobot(Context context) {
        try {
            final ArrayList<BluetoothDevice> lv_arr = new ArrayList<BluetoothDevice>();
            lv_arr.addAll(cv_pairedDevices);

            MyListAdapter lv_adapter = new MyListAdapter(context, lv_arr);

            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.list);

            ListView lv_listView = (ListView) dialog.findViewById(R.id.vv_listView);
            lv_listView.setAdapter(lv_adapter);

            lv_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cf_connectToRobot(lv_arr.get(position));
                    handleConnected();
                    cf_setupBTMonitor();
                    dialog.cancel();
                }
            });
            dialog.setCancelable(true);
            dialog.setTitle("");
            dialog.show();
        }
        catch (Exception e) {
            return "Failed in findRobot() " + e.getMessage();
        }
        return "";
    }

    public synchronized String cf_connectToRobot(BluetoothDevice bd) {
        try {
            cv_socket = bd.createRfcommSocketToServiceRecord(bd.getUuids()[0].getUuid());
            cv_socket.connect();
            return "Connect to " + bd.getName() + " at " + bd.getAddress();
        }
        catch (Exception e) {
            return "Error interacting with remote device [" + e.getMessage() + "]";
        }
    }

    public synchronized void handleConnected() {
        try {
            cv_is = cv_socket.getInputStream();
            cv_os = cv_socket.getOutputStream();
            cv_bConnected = true;
            //btnConnect.setVisibility(View.GONE);
            //btnDisconnect.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            handleDisconnected();
            cv_is = null;
            cv_os = null;
            //disconnectFromRobot(null);
        }
    }

    public synchronized void handleDisconnected() {
        try {
            cv_socket.close();
            cv_is.close();
            cv_os.close();
            cv_bConnected = false;
        }
        catch(Exception e) {

        }
    }

    public byte[] cf_moveMotor(int motor,int speed, int state) {

        try {
            byte[] buffer = new byte[15];

            buffer[0] = (byte) (15-2);			//length lsb
            buffer[1] = 0;						// length msb
            buffer[2] =  0;						// direct command (with response)
            buffer[3] = 0x04;					// set output state
            buffer[4] = (byte) motor;			// output 1 (motor B)
            buffer[5] = (byte) speed;			// power
            buffer[6] = 1 + 2;					// motor on + brake between PWM
            buffer[7] = 0;						// regulation
            buffer[8] = 0;						// turn ration??
            buffer[9] = (byte) state; //0x20;					// run state
            buffer[10] = 0;
            buffer[11] = 0;
            buffer[12] = 0;
            buffer[13] = 0;
            buffer[14] = 0;

            cv_os.write(buffer);
            cv_os.flush();
            return buffer;
        }
        catch (Exception e) {
            // cv_tvHello.setText("Error in MoveForward(" + e.getMessage() + ")");
        }
        return null;
    }

    public byte[] cf_moveMotor(byte[] move) {

        try {




            cv_os.write(move);
            cv_os.flush();
            return move;
        }
        catch (Exception e) {
            // cv_tvHello.setText("Error in MoveForward(" + e.getMessage() + ")");
        }
        return null;
    }


    private void cf_setupBTMonitor() {
        cv_btMonitor = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.bluetooth.device.action.ACL_CONNECTED")) {
                    try {
                        handleConnected();
                    }
                    catch (Exception e) {
                        handleDisconnected();
                        cv_is = null;
                        cv_os = null;
                    }
                }
                if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                }
            }
        };
    }
}