package com.por.springbatch.config;

import org.joda.time.DateTime;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BatchListener implements JobExecutionListener {

    private DateTime startTime, stopTime;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = new DateTime();
        System.out.println("Job starts at " + startTime);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        stopTime = new DateTime();
        System.out.println("Job stops at " + stopTime);
        System.out.println("Total time taken in millis : " + getTimeInMillis(startTime, stopTime));

        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            System.out.println("Job Completed!");
        } else {
            System.out.println("Job not completed!");
            List<Throwable> exceptionList = jobExecution.getAllFailureExceptions();
            for (Throwable th : exceptionList){
                System.out.println("Exception error " + th.getLocalizedMessage());
            }
        }

    }

    private long getTimeInMillis(DateTime start, DateTime stop){
        return stop.getMillis() - start.getMillis();
    }

}
