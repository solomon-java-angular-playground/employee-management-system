package com.teleconsys.consumerservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ReportController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/download/employee-report")
    public ResponseEntity<byte[]> downloadEmployeeReport() throws IOException {
        // Recupera tutti i dati da employee_logs
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> employeeLogs = (List<Map<String, Object>>) (List<?>) mongoTemplate.findAll(Map.class, "employee_logs");

        // Converte i dati in formato CSV
        String csvReport = convertToCSV(employeeLogs);

        // Prepara la risposta HTTP per scaricare il file
        InputStream inputStream = new ByteArrayInputStream(csvReport.getBytes(StandardCharsets.UTF_8));
        byte[] content = inputStream.readAllBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "employee_report.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(content.length)
                .body(content);
    }

    // Funzione per convertire i dati in formato CSV
    private String convertToCSV(List<Map<String, Object>> data) {
        if (data.isEmpty()) {
            return "";
        }

        // Preleva i titoli delle colonne
        String header = String.join(",", data.get(0).keySet());

        // Converte ogni riga di dati in una riga di CSV
        List<String> rows = data.stream()
                .map(map -> map.values().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(",")))
                .collect(Collectors.toList());

        // Combina il tutto in un'unica stringa CSV
        return header + "\n" + String.join("\n", rows);
    }
}
