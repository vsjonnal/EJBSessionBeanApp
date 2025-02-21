package com.test.or.beans;

import javax.ejb.Remote;

@Remote
public interface TimerStatusSFS {

    public String getTimerStatus();

    public void setTimerStatus();

    public void resetTimerStatus();

    public String getTimerHandleInfo();

    public void setTimerHandleInfo(String th);
}
