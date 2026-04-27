package com.example.ibmmq.service.messaging;

import com.example.ibmmq.service.mq.IMQService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Message Producer Service Implementation
 * 
 * Provides high-level message publishing capabilities with support
 * for multiple message types and delivery patterns.
 */
@Slf4j
@Service
public class MessageProducer implements IMessageProducer {

    private final IMQService mqService;
    private final ObjectMapper objectMapper;

    @Autowired
    public MessageProducer(IMQService mqService) {
        this.mqService = mqService;
        this.objectMapper = new ObjectMapper();
        log.info("Message Producer initialized");
    }

    @Override
    public void publishTextMessage(String queueName, String messageBody) throws JMSException {
        try {
            log.info("Publishing text message to queue: {}", queueName);
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "text/plain");
            headers.put("Message-ID", UUID.randomUUID().toString());
            
            mqService.sendMessageWithProperties(queueName, messageBody, headers);
            log.debug("Text message published successfully");

        } catch (JMSException e) {
            log.error("Failed to publish text message to queue: {}", queueName, e);
            throw e;
        }
    }

    @Override
    public void publishJsonMessage(String queueName, String jsonPayload) throws JMSException {
        try {
            log.info("Publishing JSON message to queue: {}", queueName);
            
            // Validate JSON format
            objectMapper.readTree(jsonPayload);
            
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Message-ID", UUID.randomUUID().toString());
            
            mqService.sendMessageWithProperties(queueName, jsonPayload, headers);
            log.debug("JSON message published successfully");

        } catch (Exception e) {
            log.error("Failed to publish JSON message to queue: {}", queueName, e);
            throw new JMSException("JSON message publishing failed: " + e.getMessage());
        }
    }

    @Override
    public void publishMessageWithHeaders(String queueName, String messageBody, 
                                          Map<String, String> headers) throws JMSException {
        try {
            log.info("Publishing message with custom headers to queue: {}", queueName);
            
            // Add standard headers if not present
            if (headers == null) {
                headers = new HashMap<>();
            }
            if (!headers.containsKey("Message-ID")) {
                headers.put("Message-ID", UUID.randomUUID().toString());
            }
            headers.put("Timestamp", String.valueOf(System.currentTimeMillis()));
            
            mqService.sendMessageWithProperties(queueName, messageBody, headers);
            log.debug("Message with headers published successfully");

        } catch (JMSException e) {
            log.error("Failed to publish message with headers to queue: {}", queueName, e);
            throw e;
        }
    }

    @Override
    public void publishFireAndForget(String queueName, String messageBody) throws JMSException {
        try {
            log.info("Publishing fire-and-forget message to queue: {}", queueName);
            mqService.sendMessage(queueName, messageBody);
            log.debug("Fire-and-forget message sent successfully");

        } catch (JMSException e) {
            log.error("Failed to publish fire-and-forget message to queue: {}", queueName, e);
            throw e;
        }
    }
}
