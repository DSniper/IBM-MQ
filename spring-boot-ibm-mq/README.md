# 🚀 Spring Boot IBM MQ Integration - Complete Application

**Project**: `spring-boot-ibm-mq` | **Version**: `1.0.0` | **Java**: `17+` | **Spring Boot**: `2.7.18`

**Workspace Path**: `d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq\`

**Status**: ✅ **FULLY OPERATIONAL** - Application running on port 8081, IBM MQ running on port 1414

A production-ready, end-to-end Spring Boot application integrated with IBM MQ running on Docker. This application provides clean, reusable, and shareable services for message queue operations.

## 📋 Table of Contents

- [Quick Start](#-quick-start)
- [Project Structure](#-project-structure-with-absolute-paths)
- [Prerequisites](#-prerequisites)
- [Execution Flow](#-execution-flow)
- [Step-by-Step Execution](#-step-by-step-execution)
- [Configuration](#-configuration-reference)
- [API Endpoints](#-api-endpoints)
- [Troubleshooting](#-troubleshooting)

---

---

## ✨ Features

✅ **IBM MQ Integration** - Complete integration with IBM MQ using Spring JMS  
✅ **Reusable Services** - Clean, well-organized service layer for MQ operations  
✅ **REST APIs** - Comprehensive REST endpoints for message publishing and consumption  
✅ **Docker Support** - Pre-configured Docker Compose setup for IBM MQ  
✅ **Error Handling** - Robust error handling and logging throughout  
✅ **Queue Management** - APIs for queue operations (status, depth, purge)  
✅ **Local & Cloud Ready** - Easy configuration for local or cloud deployment  
✅ **Production Ready** - Enterprise-grade code structure and best practices  

---

## 📁 Project Structure with Absolute Paths

```
d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq\
├── pom.xml                                        # Maven dependencies (Java 17, Spring Boot 3.2.4)
├── Dockerfile                                     # Container build configuration
├── docker-compose.yml                             # IBM MQ Docker setup (in parent directory)
├── quick-start.ps1                                # Windows PowerShell automation script
├── quick-start.sh                                 # Linux/Mac bash automation script
├── README.md                                      # This file
├── GETTING-STARTED.md                             # Quick 5-minute setup guide
├── ENV-CONFIGURATION-EXAMPLES.md                  # Environment variable templates
│
├── target/                                        # Build output (auto-generated)
│   ├── classes/                                  # Compiled .class files
│   ├── spring-boot-ibm-mq-1.0.0.jar             # Executable JAR
│   └── maven-status/
│
└── src/
    └── main/
        ├── java/com/example/ibmmq/
        │   ├── SpringBootIbmMqApplication.java   # Main Spring Boot entry point
        │   │
        │   ├── config/
        │   │   └── MQConfiguration.java          # IBM MQ connection factory configuration
        │   │
        │   ├── controller/
        │   │   ├── MessagePublishController.java # REST APIs: POST /publish, GET /consume
        │   │   └── QueueManagementController.java# REST APIs: Queue status, purge, depth
        │   │
        │   ├── service/
        │   │   ├── mq/
        │   │   │   ├── IMQService.java          # Core MQ service interface
        │   │   │   └── MQService.java           # Core MQ service implementation
        │   │   │
        │   │   └── messaging/
        │   │       ├── IMessageProducer.java    # Producer service interface
        │   │       ├── MessageProducer.java     # Producer implementation
        │   │       ├── IMessageConsumer.java    # Consumer service interface
        │   │       └── MessageConsumer.java     # Consumer implementation
        │   │
        │   └── model/
        │       ├── MessageRequest.java          # Request DTO
        │       └── ApiResponse.java             # Response DTO wrapper
        │
        └── resources/
            ├── application.yml                   # Main configuration (dev/local)
            └── application-prod.yml              # Production configuration
```

---

## 🎯 Quick Start (One Command)

**Windows PowerShell (Recommended)**:
```powershell
cd "d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq"
.\quick-start.ps1
```

**Linux/Mac**:
```bash
cd d/Softwares/MQ/IBM\ MQ/spring-boot-ibm-mq
chmod +x quick-start.sh
./quick-start.sh
```

**Manual (All Platforms)**:
```bash
# 1. Start IBM MQ (takes 30-60 seconds)
docker-compose up -d

# 2. Build application
mvn clean package -DskipTests

# 3. Run application
mvn spring-boot:run

# App ready at: http://localhost:8081/api
```

---

## 📦 Prerequisites

### Required Tools
- **Java 17 or higher** - [Download](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- **Maven 3.6+** - [Download](https://maven.apache.org/download.cgi)
- **Docker & Docker Compose** - [Download](https://www.docker.com/products/docker-desktop)

### Verify Installations

```powershell
# Verify Java
java -version
# Expected: java version "17.0.x" or higher

# Verify Maven
mvn -version
# Expected: Apache Maven 3.8.x or higher

# Verify Docker
docker --version
docker-compose --version
```

---

## 🔄 Execution Flow

```
STEP 1: Start IBM MQ Docker Container (30-60s initialization)
        ↓
STEP 2: Build Spring Boot Application (download dependencies + compile)
        ↓
STEP 3: Run Spring Boot Application
        ↓
STEP 4: Test REST APIs
        ↓
STEP 5: Monitor & Troubleshoot (optional)
```

---

## 🎯 Step-by-Step Execution

### STEP 1: Start IBM MQ Container

**Parent Directory**: `d:\Softwares\MQ\IBM MQ\`

```powershell
# Option A: Using Docker Compose (RECOMMENDED)
cd "d:\Softwares\MQ\IBM MQ"
docker-compose up -d

# Option B: Using Docker run directly
docker run -d --name ibm-mq `
  -p 1414:1414 `
  -p 9443:9443 `
  -e LICENSE=accept `
  -e MQ_QMGR_NAME=QM1 `
  -e MQ_APP_PASSWORD=passw0rd `
  icr.io/ibm-messaging/mq:latest

# Verify container is running
docker ps | grep ibm-mq

# Check MQ startup logs (wait 30-60 seconds for full initialization)
docker logs -f ibm-mq
```

**Expected Startup Output**:
```
Listener DEV.APP.SVRCONN now active
Started MQ server
Queue Manager QM1 ready
```

**MQ Access Points**:
| Service | URL | Credentials |
|---------|-----|-------------|
| **MQ Client Connection** | `tcp://localhost:1414` | User: `app` / Pass: `passw0rd` |
| **MQ Console (HTTPS)** | `https://localhost:9443` | User: `admin` / Pass: `passw0rd` |
| **MQ Console (HTTP)** | `http://localhost:9080` | Web access (no auth) |

---

### STEP 2: Build Spring Boot Application

**Working Directory**: `d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq\`

```powershell
# Navigate to project
cd "d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq"

# Clean previous builds
mvn clean

# Build application (downloads dependencies, compiles Java, packages JAR)
mvn package -DskipTests

# Expected Output:
# [INFO] Building jar: ...\target\spring-boot-ibm-mq-1.0.0.jar
# [INFO] BUILD SUCCESS
```

**Build Process Details**:
```
pom.xml (read dependencies)
    ↓
Download from Maven Central Repository
    ↓
Compile: src/main/java/ → target/classes/
    ↓
Copy Resources: src/main/resources/ → target/classes/
    ↓
Package: target/classes/ → target/spring-boot-ibm-mq-1.0.0.jar
    ↓
✓ BUILD SUCCESS
```

**Generated Files**:
- **Compiled Classes**: `d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq\target\classes\`
- **Executable JAR**: `d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq\target\spring-boot-ibm-mq-1.0.0.jar`
- **Dependencies Cache**: `C:\Users\<YourUsername>\.m2\repository\`

---

### STEP 3: Run Spring Boot Application

**Working Directory**: `d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq\`

#### Method A: Run with Maven (Development - Recommended)

```powershell
# From project root
cd "d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq"

# Run application
mvn spring-boot:run

# Expected Output:
# ...
# Started SpringBootIbmMqApplication in X.XXX seconds (JVM running for Y.YYY)
# Applications running at: http://localhost:8080/api
# ...
```

**Application Startup Verification**:
```
✓ Main Class: com.example.ibmmq.SpringBootIbmMqApplication
✓ Web Server: Embedded Apache Tomcat/10.1.x (port 8080)
✓ JMS Provider: IBM MQ at tcp://localhost:1414
✓ Context Path: /api
✓ Config File: target/classes/application.yml (from src/main/resources/)
```

#### Method B: Run JAR Directly (Production)

```powershell
# From target directory
cd "d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq\target"

# Execute JAR
java -jar spring-boot-ibm-mq-1.0.0.jar

# Or from any directory
java -jar "d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq\target\spring-boot-ibm-mq-1.0.0.jar"

# Expected Output:
# ...
# Started SpringBootIbmMqApplication in X seconds
# Applications running at: http://localhost:8080/api
```

**Stop the Application**:
```powershell
# Press Ctrl+C in terminal, or kill Java process
Get-Process java | Stop-Process
```

---

### STEP 4: Test REST APIs

**Base URL**: `http://localhost:8080/api`

#### A. Publish Message to Queue

```powershell
# Send message to DEV.QUEUE.1
$body = @{
    message = "Hello IBM MQ from Spring Boot"
    correlationId = "12345"
} | ConvertTo-Json

Invoke-RestMethod `
  -Uri "http://localhost:8080/api/v1/messages/publish" `
  -Method POST `
  -Body $body `
  -ContentType "application/json"

# Expected Response:
# {
#   "status": "success",
#   "message": "Message published successfully",
#   "queueName": "DEV.QUEUE.1",
#   "timestamp": "2026-04-25T12:34:56"
# }
```

#### B. Consume Messages

```powershell
# Receive messages from queue
Invoke-RestMethod `
  -Uri "http://localhost:8080/api/v1/messages/consume" `
  -Method GET

# Expected Response: Array of messages from queue
```

#### C. Get Queue Status

```powershell
# Check queue depth and properties
Invoke-RestMethod `
  -Uri "http://localhost:8080/api/v1/queue/status" `
  -Method GET

# Expected Response:
# {
#   "queueName": "DEV.QUEUE.1",
#   "queueDepth": 5,
#   "status": "online",
#   "lastUpdated": "2026-04-25T12:34:56"
# }
```

---

### STEP 5: Monitor & Troubleshoot

#### Monitor Application Logs

```powershell
# View running Java processes
Get-Process java | Select-Object Name, Id, WorkingSet

# View application logs (if running in foreground)
# Logs stream in real-time with DEBUG level for com.example.ibmmq
```

#### Monitor MQ Container

```powershell
# View MQ logs
docker logs ibm-mq

# Follow live logs
docker logs -f ibm-mq

# Check container status
docker ps --filter "name=ibm-mq"

# Stop MQ container
docker stop ibm-mq

# Remove MQ container
docker rm ibm-mq
```

#### Health Check Endpoints

```powershell
# Spring Boot Health endpoint
Invoke-RestMethod -Uri "http://localhost:8080/api/health"

# Actuator endpoints (if enabled)
Invoke-RestMethod -Uri "http://localhost:8080/api/actuator"
```

---

## ⚙️ Configuration Reference

**File Location**: `d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq\src\main\resources\application.yml`

```yaml
# ============================================================================
# Server Configuration
# ============================================================================
server:
  port: 8080                           # Application port
  servlet:
    context-path: /api                 # API base path

# ============================================================================
# IBM MQ Configuration (Local Docker)
# ============================================================================
mq:
  queue-manager: QM1                   # Queue Manager name (must match docker-compose)
  channel: DEV.APP.SVRCONN             # Channel name
  connection-name: localhost(1414)     # Host and port
  user: app                            # MQ username
  password: passw0rd                   # MQ password
  connection-pool-size: 10             # Connection pool size
  connection-timeout: 30000            # Timeout in ms

# ============================================================================
# Spring JMS Configuration
# ============================================================================
spring:
  jms:
    provider-url: tcp://localhost:1414
    user: app
    password: passw0rd
    listener:
      concurrency: 1-5                 # Concurrent listeners
      acknowledge-mode: CLIENT          # Manual acknowledgment

# ============================================================================
# Logging Configuration
# ============================================================================
logging:
  level:
    root: INFO
    com.example.ibmmq: DEBUG             # Application debug logs
    org.springframework.jms: DEBUG
    com.ibm.mq: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

**Production Configuration**: `d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq\src\main\resources\application-prod.yml`

To use production config:
```powershell
# Run with production profile
java -jar target/spring-boot-ibm-mq-1.0.0.jar --spring.profiles.active=prod

# Or with Maven
mvn spring-boot:run -Dspring.profiles.active=prod
```

---

## 📡 API Endpoints

---

### Publish Text Message
```powershell
POST /api/v1/messages/publish/text
```
```powershell
$response = Invoke-RestMethod `
  -Uri "http://localhost:8080/api/v1/messages/publish/text?message=Hello%20IBM%20MQ" `
  -Method POST

$response | ConvertTo-Json
```

### Publish JSON Message
```powershell
POST /api/v1/messages/publish/json
Content-Type: application/json
```
```powershell
$body = @{
    content = "Hello World"
    sender = "my-app"
    priority = 4
} | ConvertTo-Json

$response = Invoke-RestMethod `
  -Uri "http://localhost:8081/api/v1/messages/publish/json" `
  -Method POST `
  -Body $body `
  -ContentType "application/json"

$response | ConvertTo-Json
```

### Consume Message
```powershell
GET /api/v1/messages/consume
```
```powershell
$response = Invoke-RestMethod `
  -Uri "http://localhost:8081/api/v1/messages/consume?timeoutMs=5000" `
  -Method GET

$response | ConvertTo-Json
```

### Get Queue Depth
```powershell
GET /api/v1/queue/depth/DEV.QUEUE.1
```
```powershell
$response = Invoke-RestMethod `
  -Uri "http://localhost:8081/api/v1/queue/depth/DEV.QUEUE.1" `
  -Method GET

$response | ConvertTo-Json
```

### Get Queue Status
```powershell
GET /api/v1/queue/status/DEV.QUEUE.1
```
```powershell
$response = Invoke-RestMethod `
  -Uri "http://localhost:8081/api/v1/queue/status/DEV.QUEUE.1" `
  -Method GET

$response | ConvertTo-Json
```

### Purge Queue
```powershell
DELETE /api/v1/queue/purge/DEV.QUEUE.1
```
```powershell
$response = Invoke-RestMethod `
  -Uri "http://localhost:8081/api/v1/queue/purge/DEV.QUEUE.1" `
  -Method DELETE

$response | ConvertTo-Json
```

### Check MQ Connection Status
```powershell
GET /api/v1/queue/connection/status
```
```powershell
$response = Invoke-RestMethod `
  -Uri "http://localhost:8081/api/v1/queue/connection/status" `
  -Method GET

$response | ConvertTo-Json
```

---

## ✅ Verification Checklist

Before executing, verify these are installed:

- [ ] **Java 17+**: `java -version` → Shows version 17 or higher
- [ ] **Maven 3.6+**: `mvn -version` → Shows Apache Maven 3.8+
- [ ] **Docker**: `docker --version` → Shows Docker version
- [ ] **Docker Compose**: `docker-compose --version` → Shows version

After starting MQ:
- [ ] **MQ Container Running**: `docker ps | grep ibm-mq` → Shows running container
- [ ] **MQ Port 1414 Accessible**: Wait 30-60s after startup
- [ ] **Project Builds**: `mvn clean package -DskipTests` → Shows BUILD SUCCESS

After starting Spring Boot:
- [ ] **Application Started**: Logs show "Started SpringBootIbmMqApplication"
- [ ] **API Responds**: `Invoke-RestMethod -Uri "http://localhost:8080/api/v1/queue/connection/status"`
- [ ] **Configuration Loaded**: MQ connection established to localhost:1414

---

## 🐛 Troubleshooting

### Issue: Java Not Found
**Solution**: Install Java 17+ and add to system PATH
```powershell
# Verify installation
java -version

# If not found, download from:
# https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
```

### Issue: Maven Not Found
**Solution**: Install Maven and add to PATH, or use Maven wrapper
```powershell
# Verify installation
mvn -version

# If not found, download from:
# https://maven.apache.org/download.cgi
```

### Issue: Docker Container Fails to Start
**Solution**: Check Docker daemon
```powershell
# Verify Docker is running
docker ps

# If error: "Cannot connect to Docker daemon"
# Start Docker Desktop or Docker daemon
```

### Issue: Port 1414 Already in Use
**Solution**: Stop or remove existing MQ container
```powershell
# Stop all MQ containers
docker stop ibm-mq

# Remove container
docker rm ibm-mq

# Remove image if needed
docker rmi icr.io/ibm-messaging/mq:latest

# Start fresh
cd "d:\Softwares\MQ\IBM MQ"
docker-compose up -d
```

### Issue: Connection Refused (localhost:1414)
**Solution**: Wait for MQ to initialize (30-60 seconds)
```powershell
# Check MQ logs for readiness
docker logs ibm-mq

# Look for: "Listener DEV.APP.SVRCONN now active"
# Then wait additional 10-20 seconds before connecting
```

### Issue: Spring Boot App Won't Start
**Solution**: Check logs for configuration errors
```powershell
# View current logs (if still running)
# Logs appear in terminal/console

# Check for common issues:
# 1. MQ not running: docker ps | grep ibm-mq
# 2. Port 8080 in use: netstat -ano | findstr :8080
# 3. Configuration file: Check src/main/resources/application.yml
```

### Issue: Message Send Fails
**Solution**: Verify queue exists and MQ connection
```powershell
# 1. Check queue status
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/queue/status/DEV.QUEUE.1" -Method GET

# 2. Check MQ connection
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/queue/connection/status" -Method GET

# 3. Check MQ logs
docker logs ibm-mq
```

### Issue: Connection Pool Exhausted
**Solution**: Increase pool size in configuration
```yaml
# File: d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq\src\main\resources\application.yml
mq:
  connection-pool-size: 20  # Increase from 10 to 20
```
Then rebuild and restart application.

---

## 📚 Quick Reference Commands

### Build & Run (All-in-One)
```powershell
# Windows PowerShell - Complete automation
cd "d:\Softwares\MQ\IBM MQ"; `
docker-compose up -d; `
Start-Sleep -Seconds 40; `
cd spring-boot-ibm-mq; `
mvn clean package -DskipTests; `
mvn spring-boot:run
```

### Development Workflow
```powershell
# Terminal 1: Start MQ
cd "d:\Softwares\MQ\IBM MQ"
docker-compose up -d

# Terminal 2: Build and run app
cd "d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq"
mvn clean package -DskipTests
mvn spring-boot:run
```

### Test Message Flow
```powershell
# Send message
Invoke-RestMethod `
  -Uri "http://localhost:8080/api/v1/messages/publish/text?message=TestMessage" `
  -Method POST

# Receive message
Invoke-RestMethod `
  -Uri "http://localhost:8080/api/v1/messages/consume" `
  -Method GET

# Check queue depth
Invoke-RestMethod `
  -Uri "http://localhost:8080/api/v1/queue/depth/DEV.QUEUE.1" `
  -Method GET
```

### Cleanup & Stop
```powershell
# Stop Spring Boot app
# Press Ctrl+C in running terminal

# Stop MQ container
docker-compose -f "d:\Softwares\MQ\IBM MQ\docker-compose.yml" down

# Remove all MQ containers and volumes
docker-compose -f "d:\Softwares\MQ\IBM MQ\docker-compose.yml" down -v
```

---

## 🔗 Important Links & Paths

| Item | Location/URL |
|------|--------------|
| **Project Root** | `d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq\` |
| **Configuration** | `d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq\src\main\resources\application.yml` |
| **Main Class** | `d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq\src\main\java\com\example\ibmmq\SpringBootIbmMqApplication.java` |
| **Docker Compose** | `d:\Softwares\MQ\IBM MQ\docker-compose.yml` |
| **Build Output JAR** | `d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq\target\spring-boot-ibm-mq-1.0.0.jar` |
| **Spring Boot API** | `http://localhost:8081/api` |
| **MQ Console (HTTP)** | `http://localhost:9080` |
| **MQ Console (HTTPS)** | `https://localhost:9443` |
| **IBM MQ Docs** | https://www.ibm.com/docs/en/ibm-mq |
| **Spring Boot Docs** | https://spring.io/projects/spring-boot |

---

## 📞 Support & Documentation

- **Spring Boot JMS Guide**: https://spring.io/guides/gs/messaging-jms/
- **IBM MQ Documentation**: https://www.ibm.com/docs/en/ibm-mq/
- **Maven Documentation**: https://maven.apache.org/
- **Docker Documentation**: https://docs.docker.com/

---

## ✨ What's Included in This Project

| Component | Description | File Path |
|-----------|-------------|-----------|
| **MQ Configuration** | Spring JMS connection factory setup | `config/MQConfiguration.java` |
| **Message Producer** | Service to send messages to queue | `service/messaging/MessageProducer.java` |
| **Message Consumer** | Service to receive messages from queue | `service/messaging/MessageConsumer.java` |
| **Publishing APIs** | REST endpoints for message publishing | `controller/MessagePublishController.java` |
| **Queue Management APIs** | REST endpoints for queue operations | `controller/QueueManagementController.java` |
| **Data Models** | Request/Response DTOs | `model/MessageRequest.java`, `model/ApiResponse.java` |
| **Core MQ Service** | Business logic for MQ operations | `service/mq/MQService.java` |

---

**Ready to deploy production-grade IBM MQ Spring Boot application!** 🚀
    "response_queue": "DEV.QUEUE.2"
  }
}
```

**Example:**
```bash
curl -X GET http://localhost:8080/api/v1/messages/health
```

---

## 🔧 Services & Components

### 1. **MQConfiguration** (`config/MQConfiguration.java`)
Configures the IBM MQ connection factory and JMS template.

**Key Features:**
- Connection pooling
- SSL/TLS support ready
- Customizable connection parameters
- Exception handling

### 2. **IMQService / MQService** (`service/mq/`)
Core service for low-level MQ operations.

**Available Methods:**
- `sendMessage()` - Send text message
- `sendMessageWithProperties()` - Send message with headers
- `sendAndReceive()` - Request-response pattern
- `receiveMessage()` - Receive message from queue
- `purgeQueue()` - Clear all messages from queue
- `getQueueDepth()` - Get message count
- `isQueueAccessible()` - Check queue availability
- `isConnected()` - Check connection status

### 3. **IMessageProducer / MessageProducer** (`service/messaging/`)
High-level message publishing service.

**Available Methods:**
- `publishTextMessage()` - Publish text
- `publishJsonMessage()` - Publish JSON with validation
- `publishMessageWithHeaders()` - Publish with custom headers
- `publishFireAndForget()` - Fire-and-forget delivery

### 4. **IMessageConsumer / MessageConsumer** (`service/messaging/`)
High-level message consumption service.

**Available Methods:**
- `consumeMessage()` - Synchronous message consumption
- `consumeAndProcess()` - Consume and process with callback
- `consumeJsonMessage()` - Consume and deserialize JSON

---

## 🐳 Docker Setup

### Docker Compose Configuration

The `docker-compose.yml` file includes:
- **Image**: `icr.io/ibm-messaging/mq:latest`
- **Port 1414**: MQ Listener (client connections)
- **Port 9080**: MQ Console (HTTP)
- **Port 9443**: MQ Console (HTTPS)

### Common Docker Commands

```bash
# Start MQ container
docker-compose up -d

# Stop MQ container
docker-compose down

# View logs
docker-compose logs -f ibm-mq

# Check container status
docker-compose ps

# Access MQ Console
# HTTPS: https://localhost:9443/ibm/console
# HTTP: http://localhost:9080/ibm/console
# Username: app
# Password: passw0rd
```

### Verify MQ is Running

```bash
# Check container health
docker-compose ps

# Test connection
telnet localhost 1414

# View MQ logs
docker logs ibm-mq-container

# Execute command inside container
docker exec -it ibm-mq-container dspmq -m QM1
```

---

## 💻 Local Deployment

### Step-by-Step Guide

1. **Prerequisites Check**
   ```bash
   java -version          # Java 17+
   mvn -version          # Maven 3.6+
   docker -version       # Docker
   docker-compose -version  # Docker Compose
   ```

2. **Start IBM MQ**
   ```bash
   docker-compose up -d
   sleep 30  # Wait for MQ to start
   ```

3. **Build Application**
   ```bash
   mvn clean package -DskipTests
   ```

4. **Run Application**
   ```bash
   java -jar target/spring-boot-ibm-mq-1.0.0.jar
   ```

5. **Test APIs**
   ```bash
   # Health check
curl http://localhost:8081/api/v1/messages/health

# Publish message
curl -X POST "http://localhost:8081/api/v1/messages/publish/text?message=Test"
---

## ☁️ Cloud Deployment

### AWS Elastic Beanstalk

1. **Prepare Application**
   ```bash
   mvn clean package
   ```

2. **Create `.ebextensions/config.yml`**
   ```yaml
   option_settings:
     aws:elasticbeanstalk:application:environment:
       MQ_QUEUE_MANAGER: QM1
       MQ_CHANNEL: DEV.APP.SVRCONN
       MQ_CONNECTION_NAME: your-mq-server:1414
       MQ_USER: your-user
       MQ_PASSWORD: your-password
   ```

3. **Deploy**
   ```bash
   eb init
   eb create spring-mq-env
   eb deploy
   ```

### Docker to Cloud Registry

```bash
# Build Docker image
docker build -t spring-boot-mq:latest .

# Tag for registry (e.g., ECR, Docker Hub)
docker tag spring-boot-mq:latest your-registry/spring-boot-mq:latest

# Push to registry
docker push your-registry/spring-boot-mq:latest

# Deploy on Kubernetes
kubectl apply -f k8s-deployment.yaml
```

---

## 🧪 Testing

### Unit Tests

```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=MQServiceTest

# Run with coverage
mvn clean test jacoco:report
```

### Integration Tests

```bash
# Ensure Docker is running with MQ
docker-compose up -d

# Run integration tests
mvn verify
```

### Manual Testing with cURL

**Publish and Consume Flow:**
```bash
# 1. Publish a message
curl -X POST "http://localhost:8080/api/v1/messages/publish/text?message=HelloMQ"

# 2. Check queue depth
curl -X GET http://localhost:8080/api/v1/queue/depth/DEV.QUEUE.1

# 3. Consume the message
curl -X GET http://localhost:8080/api/v1/messages/consume

# 4. Check queue is empty
curl -X GET http://localhost:8080/api/v1/queue/depth/DEV.QUEUE.1
```

### Testing with Postman

Import the Postman collection:
1. Create collection "IBM MQ API"
2. Add requests for each endpoint
3. Use Pre-request Script for authentication if needed
4. Run tests sequentially

---

## 🔍 Troubleshooting

### IBM MQ Container Won't Start

**Problem:** Docker container exits immediately
**Solution:**
```bash
# Check logs
docker logs ibm-mq-container

# Verify license acceptance in docker-compose.yml
# Ensure: LICENSE: accept is set
```

### Connection Refused (Connection to 1414 failed)

**Problem:** Application cannot connect to MQ
**Solution:**
```bash
# 1. Verify MQ is running
docker-compose ps

# 2. Check if port 1414 is accessible
telnet localhost 1414

# 3. Verify connection parameters in application.yml
# mq.connection-name should be: localhost(1414) for local
```

### Queue Not Found

**Problem:** javax.jms.JMSException: Queue not found
**Solution:**
```bash
# 1. Verify queue names match MQ configuration
# 2. Create queues manually in MQ Console
# 3. Check queue names don't have spaces

# Default queues in docker-compose:
# - DEV.QUEUE.1
# - DEV.QUEUE.2
# - DEV.QUEUE.ERROR
# - DEV.DLQ
```

### Authentication Failed

**Problem:** javax.jms.JMSSecurityException: Not authorized
**Solution:**
```bash
# Verify credentials in application.yml match docker-compose.yml
# Default:
# mq.user: app
# mq.password: passw0rd
```

### Timeout on Message Receive

**Problem:** Messages not being received from queue
**Solution:**
```bash
# 1. Check queue has messages
curl http://localhost:8080/api/v1/queue/depth/DEV.QUEUE.1

# 2. Increase timeout
curl -X GET "http://localhost:8080/api/v1/messages/consume?timeoutMs=30000"

# 3. Check MQ console for messages
# https://localhost:9443/ibm/console
```

### Out of Memory

**Problem:** Application crashes with OOM error
**Solution:**
```bash
# Increase heap size
java -Xmx512m -Xms256m -jar target/spring-boot-ibm-mq-1.0.0.jar

# Or set in environment
export JAVA_OPTS="-Xmx512m -Xms256m"
mvn spring-boot:run
```

---

## 📝 License

This project is provided as-is for responsible use.

---

## 🤝 Support

For issues or questions:
1. Check the Troubleshooting section
2. Review logs: `logs/application.log`
3. Check IBM MQ documentation
4. Review Spring Boot JMS documentation

---

## 🎓 Key Takeaways

✅ Clean separation of concerns (controller → service → MQ)
✅ Reusable service interfaces for flexibility
✅ Comprehensive error handling and logging
✅ Production-ready configuration management
✅ Easy local and cloud deployment
✅ RESTful API design following best practices
✅ Thread-safe operations with connection pooling

---

**Version**: 1.0.0
**Last Updated**: 2024
**Java Version**: 17+
**Spring Boot Version**: 3.2.4

