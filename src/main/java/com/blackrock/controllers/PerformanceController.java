package com.blackrock.controllers;

import com.blackrock.services.PerformanceService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blackrock/challenge/v1/performance")
public class PerformanceController {

    @Autowired
    private PerformanceService performanceService;

    @GetMapping
    public PerformanceResponse getPerf() {
        PerformanceResponse resp = new PerformanceResponse();

        resp.setMemory(performanceService.getMemoryUsage());
        resp.setThreads(performanceService.getThreadCount());
        resp.setTime("N/A");

        return resp;
    }

    @Data
    static class PerformanceResponse {
        private String time;
        private String memory;
        private int threads;
    }
}