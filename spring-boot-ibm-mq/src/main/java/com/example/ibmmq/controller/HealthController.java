package com.example.ibmmq.controller;

import com.example.ibmmq.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Health and Information Controller
 * 
 * Provides application health status and API documentation endpoints.
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HealthController {

    @Value("${spring.application.name:spring-boot-ibm-mq-app}")
    private String appName;

    @Value("${app.version:1.0.0}")
    private String appVersion;

    /**
     * Root endpoint - returns API information
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        log.info("Root endpoint accessed");
        
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("application", appName);
        info.put("version", appVersion);
        info.put("status", "RUNNING");
        info.put("timestamp", LocalDateTime.now());
        info.put("message", "IBM MQ Spring Boot Application - API is ready to use");
        info.put("dashboard", "Open http://localhost:8081/api/dashboard to see all operations");
        info.put("api_docs", "http://localhost:8081/api/info");
        
        return ResponseEntity.ok(info);
    }

    /**
     * Dashboard endpoint - serves the interactive dashboard
     */
    @GetMapping("/dashboard")
    public RedirectView dashboard() {
        log.info("Dashboard requested - redirecting to static HTML");
        return new RedirectView("/api/dashboard.html", true);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> health() {
        log.info("Health check requested");
        
        Map<String, Object> healthData = new HashMap<>();
        healthData.put("status", "UP");
        healthData.put("timestamp", LocalDateTime.now());
        healthData.put("application", appName);
        
        return ResponseEntity.ok(
                ApiResponse.success(healthData, "Application is healthy")
        );
    }

    /**
     * Simple test endpoint
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        log.info("Test endpoint called");
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "App is running 🚀");
        response.put("timestamp", LocalDateTime.now().toString());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Comprehensive API information endpoint
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getApiInfo() {
        log.info("API info requested");
        
        Map<String, Object> apiInfo = new LinkedHashMap<>();
        
        // Application info
        apiInfo.put("application", appName);
        apiInfo.put("version", appVersion);
        apiInfo.put("status", "RUNNING");
        apiInfo.put("timestamp", LocalDateTime.now());
        
        // Available endpoints
        Map<String, List<Map<String, String>>> endpoints = new LinkedHashMap<>();
        
        // Health endpoints
        List<Map<String, String>> healthEndpoints = new ArrayList<>();
        healthEndpoints.add(createEndpoint("GET", "/", "Root endpoint - API information"));
        healthEndpoints.add(createEndpoint("GET", "/health", "Health check"));
        healthEndpoints.add(createEndpoint("GET", "/test", "Test endpoint"));
        endpoints.put("Health & Status", healthEndpoints);
        
        // Message endpoints
        List<Map<String, String>> messageEndpoints = new ArrayList<>();
        messageEndpoints.add(createEndpoint("POST", "/v1/messages/publish/text", "Publish text message (param: message)"));
        messageEndpoints.add(createEndpoint("POST", "/v1/messages/publish/json", "Publish JSON message (body: MessageRequest)"));
        messageEndpoints.add(createEndpoint("GET", "/v1/messages/consume/text/{queueName}", "Consume text message from queue"));
        messageEndpoints.add(createEndpoint("GET", "/v1/messages/consume/json/{queueName}", "Consume JSON message from queue"));
        endpoints.put("Message Operations", messageEndpoints);
        
        // Queue management endpoints
        List<Map<String, String>> queueEndpoints = new ArrayList<>();
        queueEndpoints.add(createEndpoint("GET", "/v1/queue/depth/{queueName}", "Get queue depth (message count)"));
        queueEndpoints.add(createEndpoint("GET", "/v1/queue/status/{queueName}", "Check queue status"));
        queueEndpoints.add(createEndpoint("DELETE", "/v1/queue/purge/{queueName}", "Purge all messages from queue"));
        queueEndpoints.add(createEndpoint("GET", "/v1/queue/connection/status", "Check MQ connection status"));
        endpoints.put("Queue Management", queueEndpoints);
        
        apiInfo.put("endpoints", endpoints);
        
        // Default queues
        Map<String, String> defaultQueues = new HashMap<>();
        defaultQueues.put("request_queue", "DEV.QUEUE.1");
        defaultQueues.put("response_queue", "DEV.QUEUE.2");
        apiInfo.put("default_queues", defaultQueues);
        
        // Access information
        Map<String, String> accessInfo = new HashMap<>();
        accessInfo.put("api_base", "http://localhost:8081/api");
        accessInfo.put("dashboard", "http://localhost:8081/api/dashboard");
        accessInfo.put("mq_console", "http://localhost:9080");
        apiInfo.put("access", accessInfo);
        
        return ResponseEntity.ok(apiInfo);
    }

    /**
     * Helper method to create endpoint information
     */
    private Map<String, String> createEndpoint(String method, String path, String description) {
        Map<String, String> endpoint = new LinkedHashMap<>();
        endpoint.put("method", method);
        endpoint.put("path", path);
        endpoint.put("description", description);
        return endpoint;
    }
}
