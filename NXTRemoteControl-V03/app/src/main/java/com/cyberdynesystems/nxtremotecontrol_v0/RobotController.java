package com.cyberdynesystems.nxtremotecontrol_v0;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * Created by geoffduong on 11/9/16.
 */

public class RobotController implements Serializable {

    final String CV_ROBOTNAME = "NXT06";

    // BT Variables
    private BluetoothAdapter cv_btInterface;
    private Set<BluetoothDevice> cv_pairedDevices;
    private BluetoothSocket cv_socket;
    private BroadcastReceiver cv_btMonitor;
    private InputStream cv_is;
    private OutputStream cv_os;

    public void setCv_bConnected(boolean cv_bConnected) {
        this.cv_bConnected = cv_bConnected;
    }

    private boolean cv_bConnected;

    public RobotController() {
        cv_btInterface = BluetoothAdapter.getDefaultAdapter();
        cv_pairedDevices = cv_btInterface.getBondedDevices();
        cv_bConnected = false;
    }

    // page 390
    public void cf_findRobot() {
        try {

            Iterator<BluetoothDevice> lv_it = cv_pairedDevices.iterator();
            while (lv_it.hasNext()) {
                BluetoothDevice lv_bd = lv_it.next();
                if (lv_bd.getName().equalsIgnoreCase(CV_ROBOTNAME)) {
                    cf_connectToRobot(lv_bd);
                    return;
                }
            }
        } catch (Exception e) {
            //cv_tvHello.setText("Failed in findRobot() " + e.getMessage());
            Log.e("", "\"Failed in findRobot() \" + e.getMessage()");
        }
    }

    // page 391
    public void cf_connectToRobot(BluetoothDevice bd) {
        try {


            cv_socket = bd.createRfcommSocketToServiceRecord(bd.getUuids()[0].getUuid());
            cv_socket.connect();
            //cv_tvHello.setText("Connect to " + bd.getName() + " at " + bd.getAddress());
        } catch (Exception e) {
            //cv_tvHello.setText("Error interacting with remote device [" +
            //e.getMessage() + "]");
            Log.e("", "\"Failed in findRobot() \" + e.getMessage()");
        }
    }

    // modify from cf_findRobot
    public void cf_findBTList() {
        try {
            Iterator<BluetoothDevice> lv_it = cv_pairedDevices.iterator();
            while (lv_it.hasNext()) {
                BluetoothDevice lv_bd = lv_it.next();
                if (lv_bd.getName().equalsIgnoreCase(CV_ROBOTNAME)) {
                    //cv_tvHello.setText(CV_ROBOTNAME + " is in paired list");
                    return;
                }
            }
            //cv_tvHello.setText(CV_ROBOTNAME + " is NOT in paired list");
        } catch (Exception e) {
            //cv_tvHello.setText("Failed in findRobot() " + e.getMessage());
        }
    }

    public void cf_setupBTMonitor () {
        cv_btMonitor = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(
                        "android.bluetooth.device.action.ACL_CONNECTED")) {
                    //cv_tvHello.setText("Connection is good");

                    handleConnected();
                }
                if (intent.getAction().equals(
                        "android.bluetooth.device.action.ACL_DISCONNECTED")) {
                    //cv_tvHello.setText("Connection is broken");

                    handleDisconnected();
                }
            }
        };
    }

    public void handleConnected() {
        try {
            cv_is = cv_socket.getInputStream();
            cv_os = cv_socket.getOutputStream();
            cv_bConnected = true;
            //btnConnect.setVisibility(View.GONE);
            //btnDisconnect.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            cv_is = null;
            cv_os = null;
            //disconnectFromRobot(null);
        }
    }

    private void handleDisconnected() {
        try {
            cv_socket.close();
            cv_is.close();
            cv_os.close();
            cv_bConnected = false;
        }
        catch(Exception e) {

        }
    }

    public void cf_moveMotor(int motor,int speed, int state) {

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
        }
        catch (Exception e) {
            // cv_tvHello.setText("Error in MoveForward(" + e.getMessage() + ")");
        }
    }

}