package com.example.ibmmq.service.messaging;

import javax.jms.JMSException;
import java.util.Optional;

/**
 * Message Consumer Service Interface
 * 
 * High-level interface for consuming messages from MQ queues.
 * Supports both synchronous and asynchronous message consumption patterns.
 */
public interface IMessageConsumer {

    /**
     * Consume a message synchronously from a queue
     * 
     * @param queueName source queue
     * @param timeoutMs receive timeout in milliseconds
     * @return Optional containing the message if available
     * @throws JMSException if consumption fails
     */
    Optional<String> consumeMessage(String queueName, long timeoutMs) throws JMSException;

    /**
     * Consume and process a message
     * 
     * @param queueName source queue
     * @param timeoutMs receive timeout in milliseconds
     * @param processor function to process the message
     * @throws JMSException if consumption fails
     */
    void consumeAndProcess(String queueName, long timeoutMs, 
                          MessageProcessor processor) throws JMSException;

    /**
     * Consume a JSON message and convert to object
     * 
     * @param queueName source queue
     * @param timeoutMs receive timeout in milliseconds
     * @param targetClass class to deserialize to
     * @param <T> type parameter
     * @return Optional containing the deserialized object if available
     * @throws JMSException if consumption fails
     */
    <T> Optional<T> consumeJsonMessage(String queueName, long timeoutMs, 
                                       Class<T> targetClass) throws JMSException;

    /**
     * Functional interface for message processing
     */
    @FunctionalInterface
    interface MessageProcessor {
        void process(String message) throws Exception;
    }
}
