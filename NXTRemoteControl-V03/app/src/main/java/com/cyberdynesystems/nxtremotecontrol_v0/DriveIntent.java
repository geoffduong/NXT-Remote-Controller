package com.cyberdynesystems.nxtremotecontrol_v0;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by mlindsa6 on 11/9/16.
 */

public class DriveIntent extends AppCompatActivity implements View.OnClickListener{

    ImageButton cv_btnUP,  cv_btnDown, cv_btnLeft,cv_btnRight,cv_btnReset,cv_btnForwradC,cv_btnBackwardC;
    SeekBar  cv_sbBMotor,cv_sbPowerC,cv_sbAMotor;
    TextView  cv_tvBMotorPower, cv_tvPowerC,cv_tvAMotorPower;
    RobotController cv_robotController;






    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driveintent);

        cv_tvAMotorPower = (TextView) findViewById(R.id.vv_tvAMotorPower);
        if(getIntent() != null) {
            cv_robotController = new RobotController();
            cv_robotController.cf_findRobot();

            cv_robotController.handleConnected();

        }



        cv_sbAMotor =(SeekBar) findViewById(R.id.vv_sbAMotor);
        cv_tvAMotorPower = (TextView) findViewById(R.id.vv_tvAMotorPower);
        //cv_tvAMotorPower.setText("" + cv_sbBMotor.getProgress());

        cv_sbBMotor =(SeekBar) findViewById(R.id.vv_sbBMotor);
        cv_tvBMotorPower = (TextView) findViewById(R.id.vv_tvBMotorPower);
        //cv_tvBMotorPower.setText("" + cv_sbBMotor.getProgress());

        cv_sbPowerC =(SeekBar) findViewById(R.id.vv_sbPowerC);
        cv_tvPowerC = (TextView) findViewById(R.id.vv_tvPowerC);
        //cv_tvPowerC.setText("" + cv_sbPowerC.getProgress());
        cv_btnUP = (ImageButton) findViewById(R.id.vv_btnUP);
        cv_btnDown = (ImageButton) findViewById(R.id.vv_btnDown);
        cv_btnLeft = (ImageButton) findViewById(R.id.vv_btnLeft);
        cv_btnRight = (ImageButton) findViewById(R.id.vv_btnRight);
        cv_btnReset = (ImageButton) findViewById(R.id.vv_btnReset);
        cv_btnForwradC = (ImageButton) findViewById(R.id.vv_btnForwradC);
        cv_btnBackwardC = (ImageButton) findViewById(R.id.vv_btnBackwardC);

        cv_btnUP.setEnabled(true);
        cv_btnReset.setEnabled(true);
        cv_btnUP.setOnClickListener(this);
        cv_btnReset.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){
        if (view.getId() == R.id.vv_btnUP){
            Log.e("--->", "------------------>1");
            cv_robotController.cf_moveMotor(0,75,0x20);
            cv_robotController.cf_moveMotor(1,cv_sbBMotor.getProgress(),0x20);
        }
        if (view.getId() == R.id.vv_btnReset){

            cv_robotController.cf_moveMotor(0,0,0x00);
            cv_robotController.cf_moveMotor(1,0,0x00);
        }





    }



}
