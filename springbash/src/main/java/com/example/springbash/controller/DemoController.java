package com.example.springbash.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DemoController {

    @GetMapping("/")
    public Map<String, Object> accueil() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bienvenue sur SpringBash");
        response.put("status", "OK");
        response.put("port", "8081");
        response.put("timestamp", LocalDateTime.now().toString());
        return response;
    }

    @GetMapping("/api/health")
    public Map<String, String> health() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("application", "SpringBash");
        health.put("version", "1.0.0");
        health.put("port", "8081");
        return health;
    }

    @GetMapping("/api/info")
    public Map<String, String> info() {
        Map<String, String> info = new HashMap<>();
        info.put("nom", "Application Spring Bash");
        info.put("description", "Application de démonstration avec scripts");
        info.put("port", "8081");
        info.put("environnement", "development");
        return info;
    }
}