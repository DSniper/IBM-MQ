# Spring Boot IBM MQ Integration - Complete Application Suite

## 📌 Project Overview

This is a **production-ready, end-to-end Spring Boot application** that demonstrates enterprise-grade integration with IBM Message Queue (MQ) running on Docker. The application features clean, reusable, and shareable services architecture designed for both local development and cloud deployment.

### ✨ Key Features

✅ **Complete Spring Boot Integration** with IBM MQ via Spring JMS  
✅ **Docker-based IBM MQ** with clear port configuration  
✅ **Clean Service Architecture** - Reusable, testable components  
✅ **Comprehensive REST APIs** for message operations  
✅ **Queue Management** - Status, depth, purge operations  
✅ **Production-Ready** - Error handling, logging, connection pooling  
✅ **Multi-Environment Support** - Local, staging, production profiles  
✅ **Cloud Deployment Ready** - AWS, Azure, GCP, Kubernetes  

---

## 📂 Project Structure

```
IBM MQ/
├── docker-compose.yml                    # IBM MQ Docker setup
├── IBM-MQ-PORT-CONFIGURATION.md         # Port setup guide
├── CLOUD-DEPLOYMENT-GUIDE.md            # Cloud deployment instructions
│
└── spring-boot-ibm-mq/                  # Main Application
    ├── pom.xml                          # Maven dependencies
    ├── Dockerfile                       # Application containerization
    ├── README.md                        # Full documentation
    ├── GETTING-STARTED.md              # Quick start guide
    ├── ENV-CONFIGURATION-EXAMPLES.md   # Environment setup examples
    ├── quick-start.sh                  # Linux/Mac quick start
    ├── quick-start.ps1                 # Windows quick start
    │
    └── src/main/
        ├── java/com/example/ibmmq/
        │   ├── SpringBootIbmMqApplication.java   # Main class
        │   │
        │   ├── config/
        │   │   └── MQConfiguration.java          # MQ connection config
        │   │
        │   ├── controller/
        │   │   ├── MessagePublishController.java # Publishing APIs
        │   │   └── QueueManagementController.java # Queue management APIs
        │   │
        │   ├── service/
        │   │   ├── mq/
        │   │   │   ├── IMQService.java           # Core MQ interface
        │   │   │   └── MQService.java            # Core MQ implementation
        │   │   └── messaging/
        │   │       ├── IMessageProducer.java     # Producer interface
        │   │       ├── MessageProducer.java      # Producer impl
        │   │       ├── IMessageConsumer.java     # Consumer interface
        │   │       └── MessageConsumer.java      # Consumer impl
        │   │
        │   └── model/
        │       ├── MessageRequest.java           # Request DTO
        │       └── ApiResponse.java              # Response DTO
        │
        └── resources/
            ├── application.yml                   # Default config
            └── application-prod.yml             # Production config
```

---

## 🚀 Quick Start (5 Minutes)

### Prerequisites
- Java 17+
- Docker & Docker Compose
- Maven 3.6+

### Start Application

**Linux/Mac:**
```bash
cd spring-boot-ibm-mq
chmod +x quick-start.sh
./quick-start.sh
```

**Windows (PowerShell):**
```powershell
cd spring-boot-ibm-mq
.\quick-start.ps1
```

**Manual:**
```bash
docker-compose up -d
mvn spring-boot:run
```

Application runs on: **http://localhost:8080**

---

## 📋 Documentation Guide

### Getting Started
- **[GETTING-STARTED.md](spring-boot-ibm-mq/GETTING-STARTED.md)** - Step-by-step setup guide
- **[README.md](spring-boot-ibm-mq/README.md)** - Complete documentation

### Configuration & Deployment
- **[IBM-MQ-PORT-CONFIGURATION.md](IBM-MQ-PORT-CONFIGURATION.md)** - Port setup and MQ configuration
- **[CLOUD-DEPLOYMENT-GUIDE.md](CLOUD-DEPLOYMENT-GUIDE.md)** - AWS, Azure, GCP, Kubernetes deployment
- **[ENV-CONFIGURATION-EXAMPLES.md](spring-boot-ibm-mq/ENV-CONFIGURATION-EXAMPLES.md)** - Environment setup templates

---

## 🔌 IBM MQ Configuration

### Port Mappings
| Service | Port | Protocol | Purpose |
|---------|------|----------|---------|
| **MQ Listener** | **1414** | TCP | Client connections |
| **MQ Console** | 9443 | HTTPS | Web UI (https://localhost:9443) |
| **MQ Console** | 9080 | HTTP | Web UI (http://localhost:9080) |

### Access Details
- **Username**: `app`
- **Password**: `passw0rd`
- **Queue Manager**: `QM1`
- **Connection**: `localhost(1414)`

---

## 📡 REST API Endpoints

### Message Publishing
- `POST /api/v1/messages/publish/text` - Publish text message
- `POST /api/v1/messages/publish/json` - Publish JSON message
- `POST /api/v1/messages/publish/with-headers` - Publish with headers

### Message Consumption
- `GET /api/v1/messages/consume` - Consume message from queue

### Queue Management
- `GET /api/v1/queue/depth/{queueName}` - Get queue depth
- `GET /api/v1/queue/status/{queueName}` - Check queue status
- `DELETE /api/v1/queue/purge/{queueName}` - Purge queue
- `GET /api/v1/queue/connection/status` - Check MQ connection

### Health & Monitoring
- `GET /api/v1/messages/health` - Health check

**[Full API Documentation →](spring-boot-ibm-mq/README.md#-api-documentation)**

---

## 🛠️ Core Services

### 1. MQConfiguration (`config/MQConfiguration.java`)
Configures IBM MQ connection factory and JMS template
- Connection pooling
- Timeout configuration
- Exception handling

### 2. IMQService / MQService (`service/mq/`)
**Core MQ operations** - Low-level message queue interface
- `sendMessage()` - Send text message
- `sendMessageWithProperties()` - Send with headers
- `receiveMessage()` - Receive from queue
- `sendAndReceive()` - Request-response pattern
- `getQueueDepth()` - Check queue size
- `purgeQueue()` - Clear queue
- `isQueueAccessible()` - Verify access
- `isConnected()` - Check connection

### 3. IMessageProducer / MessageProducer (`service/messaging/`)
**High-level message publishing** - Business-friendly interface
- `publishTextMessage()` - Text publishing
- `publishJsonMessage()` - JSON with validation
- `publishMessageWithHeaders()` - Custom headers
- `publishFireAndForget()` - Async delivery

### 4. IMessageConsumer / MessageConsumer (`service/messaging/`)
**High-level message consumption** - Business-friendly interface
- `consumeMessage()` - Synchronous consumption
- `consumeAndProcess()` - Consume with callback
- `consumeJsonMessage()` - JSON deserialization

---

## 🌐 REST Controllers

### MessagePublishController
Exposes message publishing and consumption as REST APIs
- Handles text and JSON payloads
- Custom header support
- Request/response mapping

### QueueManagementController
Exposes queue operations as REST APIs
- Queue depth monitoring
- Status checking
- Queue purging
- Connection verification

---

## 📊 Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Client / REST API                         │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│              Controllers Layer                               │
│  ├─ MessagePublishController                                │
│  └─ QueueManagementController                               │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│           High-Level Services Layer                          │
│  ├─ IMessageProducer / MessageProducer                       │
│  └─ IMessageConsumer / MessageConsumer                       │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│            Core MQ Services Layer                            │
│  ├─ IMQService / MQService                                  │
│  └─ JmsTemplate (Spring Framework)                          │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│          IBM MQ Configuration Layer                          │
│  └─ MQConfiguration (ConnectionFactory)                     │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│            IBM MQ (Docker Container)                         │
│  Port 1414: Client connections                              │
│  Port 9443: Management Console (HTTPS)                      │
│  Port 9080: Management Console (HTTP)                       │
└─────────────────────────────────────────────────────────────┘
```

---

## 🚀 Deployment Options

### Local Development
```bash
# Start all services
docker-compose up -d
mvn spring-boot:run
```

### Docker Container
```bash
# Build image
docker build -t spring-boot-mq:latest .

# Run container
docker run -p 8080:8080 spring-boot-mq:latest
```

### Cloud Platforms
- **AWS Elastic Beanstalk** - [Guide](CLOUD-DEPLOYMENT-GUIDE.md#aws-elastic-beanstalk)
- **Azure App Service** - [Guide](CLOUD-DEPLOYMENT-GUIDE.md#azure-app-service)
- **Google Cloud Run** - [Guide](CLOUD-DEPLOYMENT-GUIDE.md#google-cloud-run)
- **Kubernetes** - [Guide](CLOUD-DEPLOYMENT-GUIDE.md#kubernetes-deployment)
- **Heroku** - [Guide](CLOUD-DEPLOYMENT-GUIDE.md#heroku-deployment)

---

## ⚙️ Configuration

### Local Development (`application.yml`)
```yaml
mq.connection-name: localhost(1414)
mq.user: app
mq.password: passw0rd
server.port: 8080
```

### Production (`application-prod.yml`)
```yaml
mq.connection-name: ${MQ_CONNECTION_NAME}
mq.user: ${MQ_USER}
mq.password: ${MQ_PASSWORD}
spring.profiles.active: prod
```

**[Environment Configuration Examples →](spring-boot-ibm-mq/ENV-CONFIGURATION-EXAMPLES.md)**

---

## 🧪 Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
# Ensure Docker is running
docker-compose up -d
mvn verify
```

### Manual Testing
```bash
# Health check
curl http://localhost:8080/api/v1/messages/health

# Publish message
curl -X POST "http://localhost:8080/api/v1/messages/publish/text?message=Test"

# Check queue depth
curl http://localhost:8080/api/v1/queue/depth/DEV.QUEUE.1
```

---

## 🔍 Monitoring

### Application Health
```bash
curl http://localhost:8080/api/v1/messages/health
```

### MQ Connection Status
```bash
curl http://localhost:8080/api/v1/queue/connection/status
```

### Metrics
```bash
curl http://localhost:8080/actuator/metrics
```

### Logs
```bash
# Docker logs
docker logs ibm-mq-container

# Application logs
tail -f logs/application.log
```

---

## 🆘 Troubleshooting

### IBM MQ Container Won't Start
```bash
# Check logs
docker logs ibm-mq-container

# Restart container
docker-compose restart ibm-mq

# Verify port is free
netstat -tuln | grep 1414
```

### Connection Refused
```bash
# Verify MQ is running
docker-compose ps

# Test connectivity
telnet localhost 1414

# Check logs for errors
docker logs -f ibm-mq-container
```

### Application Won't Connect
```bash
# Verify connection parameters in application.yml
# Check environment variables are set
# Review logs for connection errors
```

**[Full Troubleshooting Guide →](spring-boot-ibm-mq/README.md#-troubleshooting)**

---

## 📚 Key Technologies

- **Spring Boot 3.2.4** - Java web framework
- **Spring JMS** - Java Message Service integration
- **IBM MQ 9.3+** - Enterprise message broker
- **Maven 3.6+** - Build automation
- **Docker** - Containerization
- **Lombok** - Boilerplate reduction
- **Jackson** - JSON processing

---

## 🎯 Use Cases

✅ Enterprise message queuing  
✅ Asynchronous processing  
✅ Microservices communication  
✅ Event-driven architecture  
✅ Workflow orchestration  
✅ System integration  
✅ Reliable message delivery  

---

## 📖 Complete Documentation

| Document | Purpose |
|----------|---------|
| [GETTING-STARTED.md](spring-boot-ibm-mq/GETTING-STARTED.md) | Setup and first steps |
| [README.md](spring-boot-ibm-mq/README.md) | Complete documentation |
| [IBM-MQ-PORT-CONFIGURATION.md](IBM-MQ-PORT-CONFIGURATION.md) | Port and MQ setup |
| [CLOUD-DEPLOYMENT-GUIDE.md](CLOUD-DEPLOYMENT-GUIDE.md) | Cloud deployment |
| [ENV-CONFIGURATION-EXAMPLES.md](spring-boot-ibm-mq/ENV-CONFIGURATION-EXAMPLES.md) | Environment setup |

---

## 🎓 Learning Path

1. **Start Here** → [GETTING-STARTED.md](spring-boot-ibm-mq/GETTING-STARTED.md)
2. **Understand APIs** → [API Documentation](spring-boot-ibm-mq/README.md#-api-documentation)
3. **Learn Services** → [Services Documentation](spring-boot-ibm-mq/README.md#-services--components)
4. **Deploy to Cloud** → [Cloud Deployment](CLOUD-DEPLOYMENT-GUIDE.md)
5. **Configure Environments** → [Environment Setup](spring-boot-ibm-mq/ENV-CONFIGURATION-EXAMPLES.md)

---

## 🤝 Architecture Principles

✅ **Separation of Concerns** - Controllers → Services → MQ layer  
✅ **Interface-Based Design** - Easy to mock and test  
✅ **Reusability** - Services can be used in different contexts  
✅ **Error Handling** - Comprehensive exception management  
✅ **Logging** - Detailed logging for debugging  
✅ **Configuration Management** - Environment-specific configs  
✅ **Connection Pooling** - Optimal resource utilization  

---

## 💡 Code Quality

- ✅ Clean code principles applied
- ✅ SOLID design patterns
- ✅ Comprehensive logging
- ✅ Exception handling
- ✅ Thread-safe operations
- ✅ Production-ready error messages

---

## 🔐 Security Considerations

- ✅ Credentials management via environment variables
- ✅ Connection authentication
- ✅ Error messages don't expose sensitive info
- ✅ Input validation
- ✅ CORS configured for API endpoints
- ✅ Ready for SSL/TLS configuration

---

## 📈 Performance Features

- ✅ Connection pooling (configurable)
- ✅ Message batching support
- ✅ Asynchronous processing capability
- ✅ Optimized for high throughput
- ✅ Memory-efficient operations
- ✅ Resource cleanup

---

## 🚢 Deployment Readiness

✅ Docker containerized  
✅ Cloud-agnostic architecture  
✅ Environment-specific configurations  
✅ Health check endpoints  
✅ Metrics and monitoring support  
✅ Graceful shutdown handling  
✅ Scalability ready  

---

## 📝 License

This project is provided for responsible use in enterprise environments.

---

## 🎉 Ready to Get Started?

👉 **[Begin with GETTING-STARTED.md](spring-boot-ibm-mq/GETTING-STARTED.md)**

---

## 📞 Need Help?

1. Check [GETTING-STARTED.md](spring-boot-ibm-mq/GETTING-STARTED.md) for setup issues
2. Review [README.md](spring-boot-ibm-mq/README.md#-troubleshooting) Troubleshooting section
3. Check IBM MQ Console: https://localhost:9443/ibm/console
4. Review application logs for errors

---

**Version**: 1.0.0  
**Last Updated**: 2024  
**Java**: 17+  
**Spring Boot**: 3.2.4  
**IBM MQ**: 9.3+

