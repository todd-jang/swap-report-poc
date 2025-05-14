package com.swapreporting.reporting;

import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class ReportGeneratorService {

    public void generateReport(String tradeId) {
        // Simulated content
        String content = "Report for Trade ID: " + tradeId + "\nStatus: OK\nTimestamp: " + System.currentTimeMillis();

        try {
            File dir = new File("reports");
            if (!dir.exists()) dir.mkdir();

            File report = new File(dir, tradeId + "_report.txt");
            try (FileWriter writer = new FileWriter(report)) {
                writer.write(content);
            }

            System.out.println("Report saved to: " + report.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}