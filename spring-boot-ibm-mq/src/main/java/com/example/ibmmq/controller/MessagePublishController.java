package com.example.ibmmq.controller;

import com.example.ibmmq.model.ApiResponse;
import com.example.ibmmq.model.MessageRequest;
import com.example.ibmmq.service.messaging.IMessageConsumer;
import com.example.ibmmq.service.messaging.IMessageProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Message Publishing Controller
 * 
 * REST endpoints for publishing messages to IBM MQ queues.
 * All endpoints support both local and cloud deployments.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/messages")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MessagePublishController {

    private final IMessageProducer messageProducer;
    private final IMessageConsumer messageConsumer;
    private final ObjectMapper objectMapper;

    @Value("${app.mq.request-queue:DEV.QUEUE.1}")
    private String requestQueue;

    @Value("${app.mq.response-queue:DEV.QUEUE.2}")
    private String responseQueue;

    @Autowired
    public MessagePublishController(IMessageProducer messageProducer,
                                   IMessageConsumer messageConsumer) {
        this.messageProducer = messageProducer;
        this.messageConsumer = messageConsumer;
        this.objectMapper = new ObjectMapper();
        log.info("Message Publishing Controller initialized");
    }

    /**
     * Publish a simple text message
     * 
     * @param message message content
     * @return API response
     */
    @PostMapping("/publish/text")
    public ResponseEntity<ApiResponse<Map<String, String>>> publishTextMessage(
            @RequestParam String message) {
        try {
            log.info("Received request to publish text message");
            
            messageProducer.publishTextMessage(requestQueue, message);
            
            Map<String, String> responseData = new HashMap<>();
            responseData.put("message_id", UUID.randomUUID().toString());
            responseData.put("status", "SENT");
            responseData.put("queue", requestQueue);
            
            return ResponseEntity.ok(
                    ApiResponse.success(responseData, "Message published successfully")
            );

        } catch (JMSException e) {
            log.error("Failed to publish text message", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to publish message", "MQ_ERROR"));
        }
    }

    /**
     * Publish a JSON message
     * 
     * @param messageRequest message request object
     * @return API response
     */
    @PostMapping("/publish/json")
    public ResponseEntity<ApiResponse<Map<String, String>>> publishJsonMessage(
            @RequestBody MessageRequest messageRequest) {
        try {
            log.info("Received request to publish JSON message");
            
            if (messageRequest.getMessageId() == null) {
                messageRequest.setMessageId(UUID.randomUUID().toString());
            }
            if (messageRequest.getTimestamp() == null) {
                messageRequest.setTimestamp(LocalDateTime.now());
            }
            
            String jsonPayload = objectMapper.writeValueAsString(messageRequest);
            messageProducer.publishJsonMessage(requestQueue, jsonPayload);
            
            Map<String, String> responseData = new HashMap<>();
            responseData.put("message_id", messageRequest.getMessageId());
            responseData.put("status", "SENT");
            responseData.put("queue", requestQueue);
            
            return ResponseEntity.ok(
                    ApiResponse.success(responseData, "JSON message published successfully")
            );

        } catch (Exception e) {
            log.error("Failed to publish JSON message", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to publish JSON message", "PARSE_ERROR"));
        }
    }

    /**
     * Publish message with custom headers
     * 
     * @param message message content
     * @param headers custom headers as query parameters
     * @return API response
     */
    @PostMapping("/publish/with-headers")
    public ResponseEntity<ApiResponse<Map<String, String>>> publishWithHeaders(
            @RequestParam String message,
            @RequestParam Map<String, String> headers) {
        try {
            log.info("Received request to publish message with headers");
            
            messageProducer.publishMessageWithHeaders(requestQueue, message, headers);
            
            Map<String, String> responseData = new HashMap<>();
            responseData.put("message_id", UUID.randomUUID().toString());
            responseData.put("status", "SENT");
            responseData.put("queue", requestQueue);
            
            return ResponseEntity.ok(
                    ApiResponse.success(responseData, "Message with headers published successfully")
            );

        } catch (JMSException e) {
            log.error("Failed to publish message with headers", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to publish message", "MQ_ERROR"));
        }
    }

    /**
     * Consume a message from queue
     * 
     * @param timeoutMs timeout in milliseconds
     * @return API response with message content
     */
    @GetMapping("/consume")
    public ResponseEntity<ApiResponse<String>> consumeMessage(
            @RequestParam(defaultValue = "5000") long timeoutMs) {
        try {
            log.info("Received request to consume message with timeout: {} ms", timeoutMs);
            
            Optional<String> message = messageConsumer.consumeMessage(responseQueue, timeoutMs);
            
            if (message.isPresent()) {
                return ResponseEntity.ok(
                        ApiResponse.success(message.get(), "Message consumed successfully")
                );
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(ApiResponse.error("No message available in queue", "NO_MESSAGE"));
            }

        } catch (JMSException e) {
            log.error("Failed to consume message", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to consume message", "MQ_ERROR"));
        }
    }

    /**
     * Health check endpoint
     * 
     * @return API response with health status
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
        try {
            log.debug("Health check endpoint called");
            
            Map<String, Object> healthData = new HashMap<>();
            healthData.put("status", "UP");
            healthData.put("request_queue", requestQueue);
            healthData.put("response_queue", responseQueue);
            healthData.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(
                    ApiResponse.success(healthData, "Service is healthy")
            );

        } catch (Exception e) {
            log.error("Health check failed", e);
            Map<String, Object> healthData = new HashMap<>();
            healthData.put("status", "DOWN");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(ApiResponse.error("Service is unavailable", "HEALTH_CHECK_FAILED"));
        }
    }
}
