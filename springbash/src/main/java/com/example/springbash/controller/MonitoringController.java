package com.example.springbash.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/monitoring")
public class MonitoringController {

    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    @Value("${app.environment}")
    private String environment;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("application", appName);
        health.put("version", appVersion);

        Map<String, String> checks = new HashMap<>();
        checks.put("database", "UP");
        checks.put("diskSpace", "UP");
        health.put("checks", checks);

        return ResponseEntity.ok(health);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("application", appName);
        info.put("version", appVersion);
        info.put("environment", environment);
        info.put("description", "Application de gestion avec scripts d'administration");
        return ResponseEntity.ok(info);
    }

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("uptime", System.currentTimeMillis());
        metrics.put("timestamp", LocalDateTime.now());
        metrics.put("javaVersion", System.getProperty("java.version"));
        metrics.put("osName", System.getProperty("os.name"));
        metrics.put("availableProcessors", Runtime.getRuntime().availableProcessors());
        metrics.put("freeMemory", Runtime.getRuntime().freeMemory());
        metrics.put("totalMemory", Runtime.getRuntime().totalMemory());
        metrics.put("maxMemory", Runtime.getRuntime().maxMemory());

        return ResponseEntity.ok(metrics);
    }
}