package com.example.ibmmq.controller;

import com.example.ibmmq.model.ApiResponse;
import com.example.ibmmq.service.mq.IMQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import java.util.HashMap;
import java.util.Map;

/**
 * Queue Management Controller
 * 
 * REST endpoints for managing IBM MQ queues.
 * Provides operations like queue status, depth, purge, etc.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/queue")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class QueueManagementController {

    private final IMQService mqService;

    @Autowired
    public QueueManagementController(IMQService mqService) {
        this.mqService = mqService;
        log.info("Queue Management Controller initialized");
    }

    /**
     * Get queue depth (number of messages)
     * 
     * @param queueName queue name
     * @return API response with queue depth
     */
    @GetMapping("/depth/{queueName}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getQueueDepth(
            @PathVariable String queueName) {
        try {
            log.info("Getting queue depth for: {}", queueName);
            
            int depth = mqService.getQueueDepth(queueName);
            
            if (depth < 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error("Failed to get queue depth", "MQ_ERROR"));
            }
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("queue_name", queueName);
            responseData.put("message_count", depth);
            
            return ResponseEntity.ok(
                    ApiResponse.success(responseData, "Queue depth retrieved successfully")
            );

        } catch (Exception e) {
            log.error("Failed to get queue depth for: {}", queueName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to get queue depth", "MQ_ERROR"));
        }
    }

    /**
     * Check if queue is accessible
     * 
     * @param queueName queue name
     * @return API response with accessibility status
     */
    @GetMapping("/status/{queueName}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkQueueStatus(
            @PathVariable String queueName) {
        try {
            log.info("Checking status for queue: {}", queueName);
            
            boolean accessible = mqService.isQueueAccessible(queueName);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("queue_name", queueName);
            responseData.put("accessible", accessible);
            responseData.put("status", accessible ? "ACTIVE" : "UNAVAILABLE");
            
            HttpStatus status = accessible ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
            
            return ResponseEntity.status(status)
                    .body(ApiResponse.success(responseData, "Queue status retrieved"));

        } catch (Exception e) {
            log.error("Failed to check queue status for: {}", queueName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to check queue status", "ERROR"));
        }
    }

    /**
     * Purge all messages from a queue
     * 
     * @param queueName queue name
     * @return API response with number of messages purged
     */
    @DeleteMapping("/purge/{queueName}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> purgeQueue(
            @PathVariable String queueName) {
        try {
            log.info("Purging queue: {}", queueName);
            
            int messagesPurged = mqService.purgeQueue(queueName);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("queue_name", queueName);
            responseData.put("messages_purged", messagesPurged);
            
            return ResponseEntity.ok(
                    ApiResponse.success(responseData, "Queue purged successfully")
            );

        } catch (Exception e) {
            log.error("Failed to purge queue: {}", queueName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to purge queue", "MQ_ERROR"));
        }
    }

    /**
     * Check MQ connection status
     * 
     * @return API response with connection status
     */
    @GetMapping("/connection/status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getConnectionStatus() {
        try {
            log.debug("Checking MQ connection status");
            
            boolean connected = mqService.isConnected();
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("connected", connected);
            responseData.put("status", connected ? "CONNECTED" : "DISCONNECTED");
            
            HttpStatus status = connected ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
            
            return ResponseEntity.status(status)
                    .body(ApiResponse.success(responseData, "Connection status retrieved"));

        } catch (Exception e) {
            log.error("Failed to check connection status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to check connection", "ERROR"));
        }
    }
}
