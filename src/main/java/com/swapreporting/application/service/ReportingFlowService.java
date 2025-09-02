package com.swapreporting.application.service;

import com.swapreporting.domain.report.model.Report;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReportingFlowService {

    public Report generateReport(String tradeId, String summary) {
        Report report = new Report();
        report.setTradeId(tradeId);
        report.setSummary(summary);
        report.setTimestamp(LocalDateTime.now());
        return report;
    }

    public void submitReport(Report report) {
        // TODO: DB 저장 또는 외부 전송 로직
        System.out.println("Report submitted: " + report);
    }
}