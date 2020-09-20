package com.example;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

@Component
public class QuartzOneMinuteJob implements Job {

    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyMMdd-HH:mmss");

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        LocalDateTime date = LocalDateTime.of(2020, 7, 14, 21, 56, 47);
        System.out.println(" cache job ran at : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd-HH:mmss")));
        System.out.println("One Minute job");
        QueryUtilities.runIt();
        CompletableFuture.runAsync(() -> System.out.println("job is done!"));
    }

}
