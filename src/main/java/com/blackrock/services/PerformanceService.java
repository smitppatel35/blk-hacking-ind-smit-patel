package com.blackrock.services;

import org.springframework.stereotype.Service;

@Service
public class PerformanceService {

    public String getMemoryUsage() {
        long used = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
        return used + " MB";
    }

    public int getThreadCount() {
        return Thread.activeCount();
    }

    public long measureExecution(Runnable task) {
        long start = System.currentTimeMillis();
        task.run();
        return System.currentTimeMillis() - start;
    }
}