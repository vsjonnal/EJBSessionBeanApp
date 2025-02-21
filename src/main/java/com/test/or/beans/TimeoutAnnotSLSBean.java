package com.test.or.beans;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerHandle;
import javax.naming.Context;
import javax.naming.InitialContext;

@Stateless
@Remote(TimeoutAnnotSLS.class)
public class TimeoutAnnotSLSBean implements TimeoutAnnotSLS {

    @Resource
    javax.ejb.TimerService ts;

    TimerHandle th;
    Timer timer;

    @EJB
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
    public String getTimerHandleInfo() throws Exception {
        Context ctx = new InitialContext();
        tstatus = (TimerStatusSFS) ctx.lookup(jndiName);
        return tstatus.getTimerHandleInfo();
    }

    @Override
    public String getTimerStatus() throws Exception {
        Context ctx = new InitialContext();
        tstatus = (TimerStatusSFS) ctx.lookup(jndiName);
        return tstatus.getTimerStatus();
    }

    @Timeout
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
