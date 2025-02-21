package com.test.or.beans;

import javax.annotation.Resource;
import javax.ejb.Timer;
import javax.ejb.TimerHandle;
import javax.ejb.TimerService;
import javax.naming.Context;
import javax.naming.InitialContext;

public class TimeoutDDSLSBean implements TimeoutDDSLS {

    @Resource
    TimerService ts ;
    
    TimerHandle th;
    Timer timer;
    
    TimerStatusSFS tstatus;
    
    String jndiName = "java:global.EJBSessionBeanApp.TimerStatusSFSBean!com.test.or.beans.TimerStatusSFS";
    
    @Override
    public String sayHello() {
        return ("Hello from TimerSLS");
    }

    @Override
    public void createTimer() throws Exception {
        timer = ts.createTimer(5000, "TimerSLS");
        th = timer.getHandle();
        Context ctx = new InitialContext();
        tstatus = (TimerStatusSFS) ctx.lookup(jndiName);
        tstatus.setTimerHandleInfo((String) (th.getTimer().getInfo()));
    }
    
    @Override
    public String getTimerStatus() throws Exception {
	  Context ctx = new InitialContext();
	  tstatus = (TimerStatusSFS) ctx.lookup(jndiName);
	  return tstatus.getTimerStatus();
    }
    
    @Override
    public String getTimerHandleInfo() throws Exception {
	  Context ctx = new InitialContext();
	  tstatus = (TimerStatusSFS) ctx.lookup(jndiName);
	  return tstatus.getTimerHandleInfo();
    }
    
    public void timeout(Timer timer) {
        try {
            System.out.println("Timer expired");
            Context ctx = new InitialContext();
            tstatus = (TimerStatusSFS) ctx.lookup("TimerStatusSFS");
            tstatus.setTimerStatus();
        } catch (Exception e) {
            System.out.println("Caught an exception in the timeoutDI method : \n" + e);
        }
    }
}