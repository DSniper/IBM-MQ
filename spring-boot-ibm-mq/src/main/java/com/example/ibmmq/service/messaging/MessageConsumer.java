package com.example.ibmmq.service.messaging;

import com.example.ibmmq.service.mq.IMQService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.util.Optional;

/**
 * Message Consumer Service Implementation
 * 
 * Provides high-level message consumption capabilities with support
 * for multiple message types and processing patterns.
 */
@Slf4j
@Service
public class MessageConsumer implements IMessageConsumer {

    private final IMQService mqService;
    private final ObjectMapper objectMapper;

    @Autowired
    public MessageConsumer(IMQService mqService) {
        this.mqService = mqService;
        this.objectMapper = new ObjectMapper();
        log.info("Message Consumer initialized");
    }

    @Override
    public Optional<String> consumeMessage(String queueName, long timeoutMs) throws JMSException {
        try {
            log.info("Consuming message from queue: {} with timeout: {} ms", queueName, timeoutMs);
            Optional<String> message = mqService.receiveMessage(queueName, timeoutMs);
            
            if (message.isPresent()) {
                log.debug("Message consumed successfully from queue: {}", queueName);
            } else {
                log.debug("No message available in queue: {} within timeout", queueName);
            }
            
            return message;

        } catch (JMSException e) {
            log.error("Failed to consume message from queue: {}", queueName, e);
            throw e;
        }
    }

    @Override
    public void consumeAndProcess(String queueName, long timeoutMs, 
                                  MessageProcessor processor) throws JMSException {
        try {
            log.info("Consuming and processing message from queue: {}", queueName);
            
            Optional<String> message = mqService.receiveMessage(queueName, timeoutMs);
            
            if (message.isPresent()) {
                try {
                    processor.process(message.get());
                    log.debug("Message processed successfully from queue: {}", queueName);
                } catch (Exception e) {
                    log.error("Failed to process message from queue: {}", queueName, e);
                    // Re-throw as JMSException for consistency
                    throw new JMSException("Message processing failed: " + e.getMessage());
                }
            } else {
                log.warn("No message available to process from queue: {}", queueName);
            }

        } catch (JMSException e) {
            log.error("Failed to consume and process message from queue: {}", queueName, e);
            throw e;
        }
    }

    @Override
    public <T> Optional<T> consumeJsonMessage(String queueName, long timeoutMs, 
                                              Class<T> targetClass) throws JMSException {
        try {
            log.info("Consuming JSON message from queue: {} to deserialize to: {}", 
                    queueName, targetClass.getSimpleName());
            
            Optional<String> message = mqService.receiveMessage(queueName, timeoutMs);
            
            if (message.isPresent()) {
                try {
                    T deserializedObject = objectMapper.readValue(message.get(), targetClass);
                    log.debug("JSON message deserialized successfully to: {}", 
                             targetClass.getSimpleName());
                    return Optional.of(deserializedObject);
                } catch (Exception e) {
                    log.error("Failed to deserialize JSON message to: {}", targetClass, e);
                    throw new JMSException("JSON deserialization failed: " + e.getMessage());
                }
            } else {
                log.debug("No message available in queue: {} within timeout", queueName);
                return Optional.empty();
            }

        } catch (JMSException e) {
            log.error("Failed to consume JSON message from queue: {}", queueName, e);
            throw e;
        }
    }
}
