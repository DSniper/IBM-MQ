package com.example.ibmmq.service.mq;

import javax.jms.JMSException;
import java.util.Map;
import java.util.Optional;

/**
 * IBM MQ Service Interface
 * 
 * This interface defines core operations for interacting with IBM MQ.
 * Implementations should handle message sending, receiving, and error handling.
 */
public interface IMQService {

    /**
     * Send a text message to a specified queue
     * 
     * @param queueName the destination queue name
     * @param message the message content
     * @throws JMSException if message sending fails
     */
    void sendMessage(String queueName, String message) throws JMSException;

    /**
     * Send a message with custom properties/headers
     * 
     * @param queueName the destination queue name
     * @param message the message content
     * @param properties custom message properties
     * @throws JMSException if message sending fails
     */
    void sendMessageWithProperties(String queueName, String message, 
                                   Map<String, String> properties) throws JMSException;

    /**
     * Send and receive pattern (synchronous request-response)
     * 
     * @param requestQueueName the queue to send the request
     * @param responseQueueName the queue to receive the response
     * @param message the request message
     * @param timeoutMs timeout in milliseconds
     * @return Optional containing the response message if received within timeout
     * @throws JMSException if operation fails
     */
    Optional<String> sendAndReceive(String requestQueueName, String responseQueueName, 
                                     String message, long timeoutMs) throws JMSException;

    /**
     * Receive a message from a queue (blocking)
     * 
     * @param queueName the source queue name
     * @param timeoutMs timeout in milliseconds
     * @return Optional containing the message if available
     * @throws JMSException if operation fails
     */
    Optional<String> receiveMessage(String queueName, long timeoutMs) throws JMSException;

    /**
     * Purge all messages from a queue
     * 
     * @param queueName the queue to purge
     * @return number of messages purged
     */
    int purgeQueue(String queueName);

    /**
     * Get queue depth (number of messages in queue)
     * 
     * @param queueName the queue name
     * @return queue depth, or -1 if operation fails
     */
    int getQueueDepth(String queueName);

    /**
     * Check if queue is accessible
     * 
     * @param queueName the queue name to check
     * @return true if queue is accessible, false otherwise
     */
    boolean isQueueAccessible(String queueName);

    /**
     * Get connection status
     * 
     * @return true if connected to MQ, false otherwise
     */
    boolean isConnected();
}
