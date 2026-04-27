package com.example.ibmmq.service.messaging;

import javax.jms.JMSException;
import java.util.Map;

/**
 * Message Producer Service Interface
 * 
 * High-level interface for producing messages to MQ queues.
 * Supports various message types and delivery patterns.
 */
public interface IMessageProducer {

    /**
     * Publish a simple text message
     * 
     * @param queueName destination queue
     * @param messageBody message content
     * @throws JMSException if publishing fails
     */
    void publishTextMessage(String queueName, String messageBody) throws JMSException;

    /**
     * Publish a JSON message
     * 
     * @param queueName destination queue
     * @param jsonPayload JSON string
     * @throws JMSException if publishing fails
     */
    void publishJsonMessage(String queueName, String jsonPayload) throws JMSException;

    /**
     * Publish a message with custom headers
     * 
     * @param queueName destination queue
     * @param messageBody message content
     * @param headers custom headers/properties
     * @throws JMSException if publishing fails
     */
    void publishMessageWithHeaders(String queueName, String messageBody, 
                                   Map<String, String> headers) throws JMSException;

    /**
     * Publish a fire-and-forget message (no response expected)
     * 
     * @param queueName destination queue
     * @param messageBody message content
     * @throws JMSException if publishing fails
     */
    void publishFireAndForget(String queueName, String messageBody) throws JMSException;
}
