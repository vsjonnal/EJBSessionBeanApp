package com.test.or.beans;

import javax.ejb.Stateful;

@Stateful
public class TimerStatusSFSBean implements TimerStatusSFS{

    static String status = "Timer NOT Expired";
    static String thandle;
    
    @Override
    public String getTimerStatus() {
        return status;
    }

    @Override
    public void setTimerStatus() {
        status = "Timer Expired";
    }

    @Override
    public void resetTimerStatus() {
        status = "Timer NOT Expired";
    }

    @Override
    public String getTimerHandleInfo() {
        return thandle;
    }

    @Override
    public void setTimerHandleInfo(String th) {
        thandle = th;
    }


}
