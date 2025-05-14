package com.swapreporting.controller;

import com.swapreporting.reporting.ReportGeneratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportGeneratorService reportService;

    public ReportController(ReportGeneratorService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/{uti}")
    public ResponseEntity<String> generateReport(@PathVariable String uti) {
        reportService.generateReport(uti);
        return ResponseEntity.ok("Report generated for " + uti);
    }

	@GetMapping("/{uti}/download")
	public ResponseEntity<FileSystemResource> downloadReport(@PathVariable String uti) {
	    File file = new File("reports/" + uti + "_report.txt");
	
	    if (!file.exists()) {
	        return ResponseEntity.notFound().build();
	}
	
	    FileSystemResource resource = new FileSystemResource(file);
	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
	        .contentType(MediaType.APPLICATION_OCTET_STREAM)
	        .body(resource);
	}
}