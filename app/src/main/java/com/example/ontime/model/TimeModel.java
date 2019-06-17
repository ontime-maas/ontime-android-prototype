package com.example.ontime.model;

public class TimeModel {
    private static TimeModel timeModel;

    int transTime;
    int busIntervalTime;
    int readyTime;

    int promiseTime_hour;
    int promiseTime_min;


    public TimeModel() {
        this.readyTime = 0;
    }

    public synchronized static TimeModel getInstance(){
        if(timeModel == null){
            timeModel = new TimeModel();
        }
        return timeModel;
    }

    public int getPromiseTime_hour() {
        return promiseTime_hour;
    }

    public synchronized void setPromiseTime_hour(int promiseTime_hour) {
        this.promiseTime_hour = promiseTime_hour;
    }

    public int getPromiseTime_min() {
        return promiseTime_min;
    }

    public synchronized void setPromiseTime_min(int promiseTime_min) {
        this.promiseTime_min = promiseTime_min;
    }

    public int getTransTime() {
        return transTime;
    }

    public synchronized void setTransTime(int transTime) {
        this.transTime = transTime;
    }

    public int getBusIntervalTime() {
        return busIntervalTime;
    }

    public synchronized void setBusIntervalTime(int busIntervalTime) {
        this.busIntervalTime = busIntervalTime;
    }

    public int getReadyTime() {
        return readyTime;
    }

    public synchronized void setReadyTime(int readyTime) {
        this.readyTime = readyTime;
    }
}
