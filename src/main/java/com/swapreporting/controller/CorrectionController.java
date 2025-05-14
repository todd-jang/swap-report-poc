package com.swapreporting.controller;

import com.swapreporting.correction.CorrectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/corrections")
public class CorrectionController {

    private final CorrectionService correctionService;

    public CorrectionController(CorrectionService correctionService) {
        this.correctionService = correctionService;
    }

    @PutMapping("/{uti}")
    public ResponseEntity<String> correctTrade(@PathVariable String uti) {
        correctionService.correctTrade(uti);
        return ResponseEntity.ok("Correction initiated for " + uti);
    }
}