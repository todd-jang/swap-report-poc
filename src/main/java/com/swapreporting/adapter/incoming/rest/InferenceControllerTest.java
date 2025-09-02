package com.swapreporting.adapter.incoming.rest;

//import com.swapreporting.application.service.InferenceFlowService;
//import com.swapreporting.domain.inference.model.TradeData;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.*;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(InferenceController.class)
//class InferenceControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private InferenceFlowService inferenceFlowService;
//
//    @Test
//    void runInferenceShouldReturn200() throws Exception {
//        TradeData dummy = new TradeData();
//        dummy.setTradeId("T-123");
//        mockMvc.perform(post("/inference")
//                .contentType("application/json")
//                .content("{\"tradeId\":\"T-123\",\"counterparty\":\"ABC\"}"))
//            .andExpect(status().isOk());
//    }
//}