package com.swapreporting.controller;

import com.swapreporting.dto.SwapTradeRequest;
import com.swapreporting.dto.SwapTradeResponse;
import com.swapreporting.ingestion.TradeIngestionService;
import com.swapreporting.validation.TradeValidationService;
import com.swapreporting.persistence.SwapTrade;
import com.swapreporting.persistence.TradeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    private final TradeIngestionService ingestionService;
    private final TradeValidationService validationService;
    private final TradeRepository tradeRepository;

    public TradeController(TradeIngestionService ingestionService,
                           TradeValidationService validationService,
                           TradeRepository tradeRepository) {
        this.ingestionService = ingestionService;
        this.validationService = validationService;
        this.tradeRepository = tradeRepository;
    }

    @PostMapping
    public ResponseEntity<SwapTradeResponse> ingestTrade(@RequestBody SwapTradeRequest request) {
        // Simulate ingestion
        ingestionService.ingestTrade(request.toString());

        // Save dummy data for demonstration
        SwapTrade trade = new SwapTrade();
        trade.setUti(request.getUti());
        trade.setAssetClass(request.getAssetClass());
        trade.setNotionalAmount(request.getNotionalAmount());
        tradeRepository.save(trade);

        return ResponseEntity.ok(new SwapTradeResponse(trade.getUti(), "Ingested successfully"));
    }

    @GetMapping("/{uti}")
    public ResponseEntity<SwapTradeResponse> getTrade(@PathVariable String uti) {
        return tradeRepository.findById(uti)
            .map(trade -> ResponseEntity.ok(new SwapTradeResponse(trade.getUti(), "Found")))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{uti}/validate")
    public ResponseEntity<SwapTradeResponse> validateTrade(@PathVariable String uti) {
        boolean isValid = validationService.validate(uti);
        return ResponseEntity.ok(new SwapTradeResponse(uti, isValid ? "Valid" : "Invalid"));
    }
    
    @GetMapping("/export/csv")
    public void exportTradesToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=swap_trades.csv");

        List<SwapTrade> trades = tradeRepository.findAll();

        try (var writer = response.getWriter()) {
            writer.write("UTI,AssetClass,NotionalAmount\n");
            for (SwapTrade trade : trades) {
                writer.write(String.format("%s,%s,%s\n",
                    trade.getUti(), trade.getAssetClass(), trade.getNotionalAmount()));
            }
        }
    }
}