package com.test.or.beans;

public interface TimeoutDDSLS {

    public String sayHello();
    public void createTimer() throws Exception;
    public String getTimerStatus() throws Exception;
    public String getTimerHandleInfo() throws Exception;
}