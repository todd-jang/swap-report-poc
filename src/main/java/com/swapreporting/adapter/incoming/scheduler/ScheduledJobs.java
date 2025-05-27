package com.example.swapreport.adapter.incoming.scheduler;

import com.example.swapreport.application.service.InferenceFlowService;
import com.example.swapreport.domain.inference.model.TradeData;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledJobs {

    private final InferenceFlowService inferenceFlowService;

    public ScheduledJobs(InferenceFlowService inferenceFlowService) {
        this.inferenceFlowService = inferenceFlowService;
    }

    @Scheduled(fixedRate = 300000) // 5분마다
    public void triggerScheduledInference() {
        TradeData trade = new TradeData(); // TODO: 실제 데이터로 대체
        trade.setTradeId("T-001");
        trade.setCurrency("USD");
        trade.setNotional(10_000_000);
        inferenceFlowService.runInference(trade);
    }
}
