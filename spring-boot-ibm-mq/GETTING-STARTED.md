# Getting Started Guide

Complete step-by-step guide to get the Spring Boot IBM MQ application running locally and deployed to the cloud.

## Prerequisites

Before starting, ensure you have the following installed:

- **Java 17 or higher** - [Download](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- **Maven 3.6+** - [Download](https://maven.apache.org/download.cgi)
- **Docker & Docker Compose** - [Download](https://www.docker.com/products/docker-desktop)
- **Git** (optional) - [Download](https://git-scm.com/)

### Verify Installations

```bash
java -version
mvn -version
docker --version
docker-compose --version
```

---

## 5-Minute Quick Start

### Option 1: Linux/Mac (Using Bash)

```bash
# Navigate to project directory
cd spring-boot-ibm-mq

# Make script executable
chmod +x quick-start.sh

# Run quick start
./quick-start.sh
```

### Option 2: Windows (Using PowerShell)

```powershell
# Navigate to project directory
cd spring-boot-ibm-mq

# Run quick start
.\quick-start.ps1
```

### Option 3: Manual Steps (All Platforms)

```bash
# Step 1: Start IBM MQ
docker-compose up -d

# Step 2: Build application
mvn clean package -DskipTests

# Step 3: Run application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

---

## Detailed Setup Instructions

### Step 1: Prepare Environment

#### Windows
```powershell
# Open PowerShell as Administrator
# Navigate to project directory
cd C:\path\to\spring-boot-ibm-mq

# Verify prerequisites
java -version
mvn -version
docker --version
```

#### Linux/Mac
```bash
# Navigate to project directory
cd ~/path/to/spring-boot-ibm-mq

# Verify prerequisites
java -version
mvn -version
docker --version
```

### Step 2: Start IBM MQ Container

```bash
# Start IBM MQ
docker-compose up -d

# Verify container is running
docker-compose ps

# Wait for IBM MQ to be ready (check health)
docker-compose logs ibm-mq
```

**Expected output:**
```
✓ Container ibm-mq-container is running
✓ Port 1414 is accessible
```

### Step 3: Verify IBM MQ Connection

```bash
# Test MQ listener port
telnet localhost 1414

# Or check with netstat
netstat -tuln | grep 1414    # Linux/Mac
netstat -ano | findstr :1414  # Windows

# Access MQ Console
# HTTPS: https://localhost:9443/ibm/console
# HTTP: http://localhost:9080/ibm/console
# Username: app
# Password: passw0rd
```

### Step 4: Build Application

```bash
# Navigate to application directory
cd spring-boot-ibm-mq

# Clean and build
mvn clean package

# Or skip tests for faster build
mvn clean package -DskipTests

# Or use Maven wrapper
./mvnw clean package -DskipTests  # Linux/Mac
mvnw.cmd clean package -DskipTests # Windows
```

**Expected output:**
```
BUILD SUCCESS
[INFO] Spring Boot IBM MQ - 1.0.0.jar
```

### Step 5: Run Application

#### Option A: Spring Boot Maven Plugin

```bash
mvn spring-boot:run
```

#### Option B: Run JAR File

```bash
java -jar target/spring-boot-ibm-mq-1.0.0.jar
```

#### Option C: With Environment Variables

```bash
# Linux/Mac
export MQ_CONNECTION_NAME="localhost(1414)"
export MQ_USER="app"
export MQ_PASSWORD="passw0rd"
java -jar target/spring-boot-ibm-mq-1.0.0.jar

# Windows
set MQ_CONNECTION_NAME=localhost(1414)
set MQ_USER=app
set MQ_PASSWORD=passw0rd
java -jar target/spring-boot-ibm-mq-1.0.0.jar
```

**Expected output:**
```
2024-XX-XX HX:XX:XX INFO main SpringBootIbmMqApplication - Started application
2024-XX-XX HX:XX:XX INFO main TomcatWebServer - Tomcat started on port(s): 8080
```

---

## Testing the Application

### 1. Health Check

```bash
curl http://localhost:8080/api/v1/messages/health
```

**Expected response:**
```json
{
  "success": true,
  "message": "Service is healthy",
  "data": {
    "status": "UP",
    "request_queue": "DEV.QUEUE.1",
    "response_queue": "DEV.QUEUE.2"
  }
}
```

### 2. Publish a Message

```bash
curl -X POST "http://localhost:8080/api/v1/messages/publish/text?message=HelloWorld"
```

**Expected response:**
```json
{
  "success": true,
  "message": "Message published successfully",
  "data": {
    "message_id": "550e8400-e29b-41d4-a716-446655440000",
    "status": "SENT",
    "queue": "DEV.QUEUE.1"
  }
}
```

### 3. Check Queue Depth

```bash
curl http://localhost:8080/api/v1/queue/depth/DEV.QUEUE.1
```

**Expected response:**
```json
{
  "success": true,
  "data": {
    "queue_name": "DEV.QUEUE.1",
    "message_count": 1
  }
}
```

### 4. Consume a Message

```bash
curl http://localhost:8080/api/v1/messages/consume
```

**Expected response:**
```json
{
  "success": true,
  "message": "Message consumed successfully",
  "data": "HelloWorld"
}
```

---

## Project Structure

```
spring-boot-ibm-mq/
├── src/main/
│   ├── java/
│   │   └── com/example/ibmmq/
│   │       ├── config/             # MQ Configuration
│   │       ├── controller/         # REST Controllers
│   │       ├── service/            # Business Logic
│   │       │   ├── mq/            # Core MQ Services
│   │       │   └── messaging/     # High-level Message Services
│   │       └── model/             # DTOs
│   └── resources/
│       ├── application.yml        # Default configuration
│       └── application-prod.yml   # Production configuration
├── pom.xml                         # Maven configuration
├── docker-compose.yml             # Docker setup
├── Dockerfile                      # Application Docker image
└── README.md                       # Documentation
```

---

## Key Configuration Files

### application.yml (Default)

```yaml
server.port=8080
mq.queue-manager=QM1
mq.connection-name=localhost(1414)
mq.user=app
mq.password=passw0rd
```

### application-prod.yml (Production)

```yaml
server.port=8080
mq.queue-manager=${MQ_QUEUE_MANAGER:QM1}
mq.connection-name=${MQ_CONNECTION_NAME:localhost(1414)}
mq.user=${MQ_USER:app}
mq.password=${MQ_PASSWORD:passw0rd}
```

---

## Common Tasks

### Check Logs

```bash
# Spring Boot logs (running in terminal)
# Logs appear in the console where mvn spring-boot:run is executed

# Docker logs
docker logs ibm-mq-container

# Follow logs
docker logs -f ibm-mq-container

# Get last N lines
docker logs --tail 100 ibm-mq-container
```

### Stop Application

```bash
# Press Ctrl+C in terminal where application is running

# Or kill process
kill -9 <PID>  # Linux/Mac
taskkill /PID <PID> /F  # Windows
```

### Stop IBM MQ

```bash
docker-compose down

# Also remove volumes (persistent data)
docker-compose down -v
```

### Restart Everything

```bash
# Stop all
docker-compose down

# Verify containers are stopped
docker-compose ps

# Start MQ again
docker-compose up -d
```

### View Queue Messages

1. Access MQ Console: `https://localhost:9443/ibm/console`
2. Login: app / passw0rd
3. Navigate to Queues → DEV.QUEUE.1
4. View or delete messages

---

## Troubleshooting

### Issue: Cannot Connect to MQ

```bash
# Check if MQ container is running
docker-compose ps

# Check logs
docker logs ibm-mq-container

# Verify port is listening
netstat -tuln | grep 1414  # Linux/Mac
netstat -ano | findstr :1414  # Windows

# Try reconnecting
docker-compose restart ibm-mq
```

### Issue: Port Already in Use

```bash
# Find process using port 1414
lsof -i :1414      # Mac
netstat -ltnp | grep 1414  # Linux
netstat -ano | findstr :1414  # Windows

# Stop the process or change port in docker-compose.yml
```

### Issue: Build Fails

```bash
# Clear Maven cache
mvn clean

# Check Java version
java -version

# Rebuild
mvn clean package -DskipTests -X  # -X for debug output
```

### Issue: Application Won't Start

```bash
# Check logs
tail -f logs/application.log

# Verify environment variables
echo $MQ_CONNECTION_NAME  # Linux/Mac
echo %MQ_CONNECTION_NAME%  # Windows

# Try with explicit connection
java -Dmq.connection-name=localhost(1414) -jar target/app.jar
```

---

## Next Steps

### Development

1. Review [README.md](README.md) for full documentation
2. Check [API Documentation](README.md#-api-documentation) for endpoints
3. Explore [Services & Components](README.md#-services--components)
4. Read [Architecture](README.md#-architecture) overview

### Deployment

1. For cloud deployment, see [CLOUD-DEPLOYMENT-GUIDE.md](../CLOUD-DEPLOYMENT-GUIDE.md)
2. For environment configuration, see [ENV-CONFIGURATION-EXAMPLES.md](ENV-CONFIGURATION-EXAMPLES.md)
3. For IBM MQ configuration details, see [IBM-MQ-PORT-CONFIGURATION.md](../IBM-MQ-PORT-CONFIGURATION.md)

### Customization

1. Modify queue names in `application.yml`
2. Add custom message processing in service layer
3. Extend REST controllers for new endpoints
4. Add authentication/authorization as needed

---

## Useful Commands Reference

```bash
# Docker
docker-compose up -d                 # Start MQ
docker-compose down                  # Stop MQ
docker-compose ps                    # List containers
docker logs ibm-mq-container        # View logs
docker exec -it ibm-mq-container /bin/bash  # Shell access

# Maven
mvn clean                            # Clean build directory
mvn compile                          # Compile
mvn test                             # Run tests
mvn package                          # Create JAR
mvn spring-boot:run                  # Run application
mvn clean package -DskipTests        # Fast build

# Java
java -version                        # Check version
java -jar target/app.jar             # Run JAR
java -Xmx512m -jar target/app.jar   # Run with memory limit

# API Testing
curl http://localhost:8080/api/v1/messages/health  # Health check
curl -X POST "http://localhost:8080/api/v1/messages/publish/text?message=test"
curl http://localhost:8080/api/v1/queue/depth/DEV.QUEUE.1
```

---

## Getting Help

- **Documentation**: Review [README.md](README.md)
- **Troubleshooting**: Check [README.md#-troubleshooting](README.md#-troubleshooting)
- **API Issues**: See [API Documentation](README.md#-api-documentation)
- **Deployment**: Visit [CLOUD-DEPLOYMENT-GUIDE.md](../CLOUD-DEPLOYMENT-GUIDE.md)

---

## What's Next?

✅ Application is running successfully
✅ IBM MQ is accessible on port 1414
✅ APIs are available on port 8080

📊 **Monitor** your application using health check endpoint
📤 **Send and receive** messages using the REST APIs
🚀 **Deploy** to cloud using CLOUD-DEPLOYMENT-GUIDE.md
🔧 **Customize** to match your business requirements

---

**Happy coding! 🎉**

