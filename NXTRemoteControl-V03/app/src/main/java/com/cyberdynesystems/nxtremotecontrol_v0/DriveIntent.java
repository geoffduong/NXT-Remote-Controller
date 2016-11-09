package com.cyberdynesystems.nxtremotecontrol_v0;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by mlindsa6 on 11/9/16.
 */

public class DriveIntent extends AppCompatActivity {

    ImageButton cv_btnUP,  cv_btnDown, cv_btnLeft,cv_btnRight,cv_btnReset,cv_btnForwradC,cv_btnBackwardC;
    SeekBar  cv_sbBMotor,cv_sbPowerC,cv_sbAMotor;
    TextView  cv_tvBMotorPower, cv_tvPowerC,cv_tvAMotorPower;






    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driveintent);
        cv_sbAMotor =(SeekBar) findViewById(R.id.vv_sbAMotor);
        cv_tvAMotorPower = (TextView) findViewById(R.id.vv_tvAMotorPower);
        cv_tvAMotorPower.setText("" + cv_sbBMotor.getProgress());

        setContentView(R.layout.driveintent);
        cv_sbBMotor =(SeekBar) findViewById(R.id.vv_sbBMotor);
        cv_tvBMotorPower = (TextView) findViewById(R.id.vv_tvBMotorPower);
        cv_tvBMotorPower.setText("" + cv_sbBMotor.getProgress());

        setContentView(R.layout.driveintent);
        cv_sbPowerC =(SeekBar) findViewById(R.id.vv_sbPowerC);
        cv_tvPowerC = (TextView) findViewById(R.id.vv_tvPowerC);
        cv_tvPowerC.setText("" + cv_sbPowerC.getProgress());


    }



}
