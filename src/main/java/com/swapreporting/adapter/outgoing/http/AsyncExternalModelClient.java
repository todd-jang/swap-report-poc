package com.swapreporting.adapter.outgoing.http;

import com.swapreporting.domain.inference.model.TradeData;
import com.swapreporting.domain.inference.model.InferenceResult;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AsyncExternalModelClient {

    private final WebClient webClient;

    public AsyncExternalModelClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<InferenceResult> inferAsync(TradeData tradeData) {
        return webClient.post()
            .uri("http://ai-model-service/infer")
            .bodyValue(tradeData)
            .retrieve()
            .bodyToMono(InferenceResult.class);
    }
}
