package com.example.ibmmq.config;

import lombok.extern.slf4j.Slf4j;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.JMSContext;

/**
 * Credential-Aware Connection Factory Wrapper
 * 
 * Wraps the IBM MQ ConnectionFactory to provide credentials when creating connections.
 * This is necessary because IBM MQ's MQConnectionFactory requires credentials to be
 * passed at connection creation time rather than being set on the factory itself.
 */
@Slf4j
public class CredentialAwareConnectionFactory implements ConnectionFactory {

    private final ConnectionFactory wrappedFactory;
    private final String userName;
    private final String password;

    public CredentialAwareConnectionFactory(ConnectionFactory factory, String userName, String password) {
        this.wrappedFactory = factory;
        this.userName = userName;
        this.password = password;
        log.debug("CredentialAwareConnectionFactory initialized with user: {}", userName);
    }

    @Override
    public Connection createConnection() throws JMSException {
        log.debug("Creating connection with credentials for user: {}", userName);
        try {
            return wrappedFactory.createConnection(userName, password);
        } catch (JMSException e) {
            log.error("Failed to create connection with credentials", e);
            throw e;
        }
    }

    @Override
    public Connection createConnection(String clientID, String clientSecret) throws JMSException {
        log.debug("Creating connection with provided credentials for user: {}", clientID);
        try {
            return wrappedFactory.createConnection(clientID, clientSecret);
        } catch (JMSException e) {
            log.error("Failed to create connection", e);
            throw e;
        }
    }

    @Override
    public JMSContext createContext() {
        log.debug("Creating context with default credentials for user: {}", userName);
        return wrappedFactory.createContext(userName, password);
    }

    @Override
    public JMSContext createContext(String userName, String password) {
        log.debug("Creating context with provided credentials for user: {}", userName);
        return wrappedFactory.createContext(userName, password);
    }

    @Override
    public JMSContext createContext(String userName, String password, int sessionMode) {
        log.debug("Creating context with provided credentials and session mode for user: {}", userName);
        return wrappedFactory.createContext(userName, password, sessionMode);
    }

    @Override
    public JMSContext createContext(int sessionMode) {
        log.debug("Creating context with session mode for user: {}", userName);
        return wrappedFactory.createContext(userName, password, sessionMode);
    }
}
