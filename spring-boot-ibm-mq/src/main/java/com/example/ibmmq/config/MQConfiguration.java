package com.example.ibmmq.config;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

/**
 * IBM MQ Configuration Class
 */
@Slf4j
@Configuration
@EnableJms
public class MQConfiguration {

    @Value("${mq.queue-manager}")
    private String queueManager;

    @Value("${mq.channel}")
    private String channel;

    @Value("${mq.connection-name}")
    private String connectionName;

    @Value("${mq.user}")
    private String userName;

    @Value("${mq.password}")
    private String password;

    @Value("${mq.connection-pool-size:10}")
    private int connectionPoolSize;

    @Bean
    public ConnectionFactory mqConnectionFactory() throws JMSException {
        log.info("Initializing IBM MQ Connection Factory");
        log.debug("Queue Manager: {}, Channel: {}, Connection: {}", 
                  queueManager, channel, connectionName);

        MQConnectionFactory factory = new MQConnectionFactory();

        try {
            factory.setQueueManager(queueManager);
            factory.setChannel(channel);
            factory.setConnectionNameList(connectionName);
            factory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
            factory.setClientID("spring-boot-ibm-mq-app");
            
            // Enable user authentication using MQCSP (MQ Security Protocol)
            factory.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);

            log.info("IBM MQ Connection Factory initialized successfully");
            
            // Return a wrapped factory that provides credentials on connection creation
            return new CredentialAwareConnectionFactory(factory, userName, password);

        } catch (JMSException e) {
            log.error("Failed to configure MQ Connection Factory", e);
            throw new JMSException("Failed to initialize IBM MQ connection: " + e.getMessage());
        }
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        log.info("Configuring JMS Template");

        JmsTemplate template = new JmsTemplate(connectionFactory);
        template.setReceiveTimeout(5000);
        template.setDeliveryPersistent(true);
        template.setDestinationResolver(new DynamicDestinationResolver());

        log.info("JMS Template configured successfully");
        return template;
    }
}
