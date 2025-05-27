package com.example.swapreport.adapter.incoming.rest;

import com.example.swapreport.application.service.ReportingFlowService;
import com.example.swapreport.domain.report.model.Report;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportingFlowService service;

    public ReportController(ReportingFlowService service) {
        this.service = service;
    }

    @PostMapping
    public Report generate(@RequestParam String tradeId, @RequestParam String summary) {
        Report report = service.generateReport(tradeId, summary);
        service.submitReport(report);
        return report;
    }
}
