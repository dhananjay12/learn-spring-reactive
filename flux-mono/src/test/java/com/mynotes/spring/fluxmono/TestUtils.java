package com.mynotes.spring.fluxmono;

import org.apache.commons.lang3.time.StopWatch;

public class TestUtils {

    public static StopWatch stopWatch = new StopWatch();

    public static void delay(long delayMilliSeconds)  {
        try{
            Thread.sleep(delayMilliSeconds);
        }catch (Exception e){
            System.out.println("Exception is :" + e.getMessage());
        }
    }

    public static void startTimer(){
        stopWatch.reset();
        stopWatch.start();
    }

    public static void stopTimer(){
        stopWatch.stop();
        System.out.println("Total Time Taken : " +stopWatch.getTime());
    }
}
