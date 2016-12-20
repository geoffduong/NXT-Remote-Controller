package com.cyberdynesystems.nxtremotecontrol_v0;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mlindsa6 on 11/30/16.
 */

public class GridDriveIntent extends AppCompatActivity {


    private static final int SPEED_OF_TURING = 4;
    private static final Float SPEED_OF_GOING_FORWARD = (float)4;
    RelativeLayout cv_gridLayout;
    ArrayList<Point> points;

    Button cv_btnRest;
    ArrayList<Float> distance;

    Button cv_btnPlayMovment;
    RobotController cv_RobotController;

    BottomBar bottomBar;


    Timer cv_timer;
    MyTimerClass cv_timerTask;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.griddriveintent);
        cv_RobotController = RobotController.getRobotController(this);
        cv_btnRest = (Button) findViewById(R.id.vv_btnResetGrid);
        distance = new ArrayList<>();

        points = new ArrayList<>();
        cv_btnPlayMovment = (Button) findViewById(R.id.vv_btnGridMovement);

        bottomBar = (BottomBar) findViewById(R.id.driveByDrawBottomBar);

        //Set default tab
        BottomBarTab driveByDrawTab = bottomBar.getTabWithId(R.id.tab_driveByDraw);
        bottomBar.setDefaultTab(driveByDrawTab.getId());

        //Bottom bar listener
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_connect) {
                    Intent lv_intent = new Intent(GridDriveIntent.this, MainActivity.class);
                    startActivity(lv_intent);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                }
                if (tabId == R.id.tab_Poll) {
                    Intent lv_intent = new Intent(GridDriveIntent.this, PollIntent.class);
                    startActivity(lv_intent);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                }
            }
        });
        cv_btnPlayMovment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(points.size()<2){
                    return;
                }
                double degree = angleBetween(points.get(0),points.get(1),new Point(points.get(0).x + 1, points.get(0).y));
                cv_timer = new Timer();
                cv_timerTask = new MyTimerClass(degree);
                cv_timer.schedule(cv_timerTask,0,cf_degreeToTime(degree));
                try {
                    Thread.sleep(100*cf_degreeToTime(degree));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cv_RobotController.cf_moveMotor(0,0,0);
                cv_RobotController.cf_moveMotor(1,0,0);
                cv_timerTask.setLv_Degree(0);
                cv_timer.schedule(cv_timerTask,0,distanceToTime(distance.get(0)));
                cv_RobotController.cf_moveMotor(0,0,0);
                try {
                    Thread.sleep(100*cf_degreeToTime(degree));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }





            }
        });


        cv_btnRest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                points.clear();

                cv_gridLayout.setForeground(new Drawable() {
                    @Override
                    public void draw(Canvas canvas) {

                    }

                    @Override
                    public void setAlpha(int alpha) {

                    }

                    @Override
                    public void setColorFilter(ColorFilter colorFilter) {

                    }

                    @Override
                    public int getOpacity() {
                        return 0;
                    }
                });
            }
        });

        cv_gridLayout = (RelativeLayout) findViewById(R.id.vv_myGridLayout);
        final Paint myPaint = new Paint();

        cv_gridLayout.setBackground(new Drawable() {

            @Override
            public void draw(Canvas canvas) {
                Log.e("------------------->", "--------------> " + canvas.getWidth());
                for(int i = 0; i < canvas.getWidth(); i = i + 200) {
                    myPaint.setColor(Color.BLACK);

                    Log.e("------------------->", "--------------> " + i);
                    canvas.drawLine(i, 0, i, canvas.getHeight(), myPaint);



                }
                for(int i = 0; i <canvas.getHeight(); i = i + 200){
                    canvas.drawLine(0, i, canvas.getWidth(), i, myPaint);

                }
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.UNKNOWN;
            }
        });
        cv_gridLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, final MotionEvent event) {


                if (event.getDownTime() > 5000) {

                    cv_gridLayout.setForeground(new Drawable() {
                        @Override
                        public void draw(Canvas canvas) {
                            myPaint.setColor(Color.RED);
                            points.add(new Point(event.getX(),event.getY()));


                            for (int i = 0; i < points.size(); i++) {
                                canvas.drawCircle(points.get(i).x, points.get(i).y, 15, myPaint);

                            }
                            if(points.size() > 1){
                                float perviosX = points.get(0).x;
                                float perviosY = points.get(0).y;
                                for(int i = 1; i < points.size(); i++){

                                    canvas.drawLine(perviosX, perviosY, points.get(i).x, points.get(i).y, myPaint);
                                    distance.add((float) Math.sqrt(Math.pow(perviosX - points.get(i).x,2)+ Math.pow(perviosY - points.get(i).y,2)));

                                    perviosX = points.get(i).x;
                                    perviosY = points.get(i).y;

                                }
                            }




                        }

                        @Override
                        public void setAlpha(int alpha) {

                        }

                        @Override
                        public void setColorFilter(ColorFilter colorFilter) {

                        }

                        @Override
                        public int getOpacity() {
                            return 0;
                        }
                    });
                }
                return true;
            }


        });



    }

    private long distanceToTime(Float aFloat) {
        return (long) (SPEED_OF_GOING_FORWARD*aFloat);
    }

    private long cf_degreeToTime(double degree) {
        return (long) (SPEED_OF_TURING/360 * degree);


    }


    private double angleBetween(Point center, Point current, Point previous) {

        return Math.toDegrees(Math.atan2(current.x - center.x,current.y - center.y)-
                Math.atan2(previous.x- center.x,previous.y- center.y));
    }

    class Point{
        float x;
        float y;
        public Point(float x, float y){
            this.x = x;
            this.y = y;

        }
    }


    private class MyTimerClass extends TimerTask {
        double lv_degree;
        public MyTimerClass(double degree){
            super();
            lv_degree = degree;

        }

        @Override
        public void run() {
            if (0 <lv_degree && lv_degree< 180){
                cv_RobotController.cf_moveMotor(1,5,0x20);
            }else if(180<=lv_degree && lv_degree<360){
                cv_RobotController.cf_moveMotor(0,5,0x20);
            }else if(lv_degree == 0 ){
                cv_RobotController.cf_moveMotor(0,5,0x20);
                cv_RobotController.cf_moveMotor(0,5,0x20);
            }







        }
        public void setLv_Degree(double degree){
            this.lv_degree = degree;
        }
    }
}




