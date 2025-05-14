package com.swapreporting.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ComplianceScheduler {
    @Scheduled(cron = "0 0 1 * * *")
    public void dailyCheck() {
        // TODO: Daily compliance verification
    }
}