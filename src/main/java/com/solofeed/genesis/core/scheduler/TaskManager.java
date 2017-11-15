package com.solofeed.genesis.core.scheduler;

import org.springframework.context.annotation.Bean;

import javax.ws.rs.ext.Provider;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by LM-G on 15/11/2017.
 */
@Provider
public class TaskManager {
    @Bean
    public Executor taskScheduler() {
        return Executors.newScheduledThreadPool(5);
    }
}
