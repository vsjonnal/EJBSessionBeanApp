package com.test.or.beans;

import javax.ejb.Remote;

@Remote
public interface TimeoutAnnotSLS {

    public String sayHello();

    public void createTimer() throws Exception;

    public String getTimerHandleInfo() throws Exception;

    public String getTimerStatus() throws Exception;  
}
