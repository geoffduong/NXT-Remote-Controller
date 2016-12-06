package com.cyberdynesystems.nxtremotecontrol_v0;

public class MyPollData {
    private int sensorImage, value;
    private boolean active;

    public MyPollData(int sensorImage, boolean active) {
        this.sensorImage = sensorImage;
        this.active = active;
        this.value = 0;
    }

    public void setSensorImage(int sensorImage) {
        this.sensorImage = sensorImage;
    }
    public int getSensorImage() {
        return sensorImage;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    public boolean getActive() {
        return active;
    }

    public void setValue(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
