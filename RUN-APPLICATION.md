# 🚀 Complete Setup & Execution Guide

## Prerequisites

✅ **Verified:**
- Java 11+ installed
- Maven installed
- Docker & Docker Compose installed

---

## Step 1: Start IBM MQ Container

Run IBM MQ in Docker using docker-compose:

```powershell
# Navigate to the parent directory where docker-compose.yml is located
cd "d:\Softwares\MQ\IBM MQ"

# Start IBM MQ container in background
docker-compose up -d

# Verify MQ container is running
docker-compose ps

# Check MQ logs to confirm it's started
docker-compose logs ibm-mq
```

**Expected Output:**
```
AMQ5026I: The listener port 1414 is now active.
```

**IBM MQ Web Console**: http://localhost:9443/ibmmq/console (if enabled)

---

## Step 2: Build the Spring Boot Application

```powershell
# Navigate to the Spring Boot project
cd "d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq"

# Clean and build the project (already tested - BUILD SUCCESSFUL ✅)
mvn clean package -DskipTests
```

**Expected Output:**
```
BUILD SUCCESS - spring-boot-ibm-mq-1.0.0.jar
```

**JAR Location**: `target/spring-boot-ibm-mq-1.0.0.jar`

---

## Step 3: Configure Credentials

The application uses credentials configured in `src/main/resources/application.yml`:

**Default Credentials:**
- **Username**: `app`
- **Password**: `passw0rd`

These credentials match the IBM MQ Docker setup. The application uses `CredentialAwareConnectionFactory` to inject credentials at connection time.

**To override credentials at runtime**, use environment variables:

```powershell
# Option A: Set environment variables before running
$env:MQ_USER = "app"
$env:MQ_PASSWORD = "passw0rd"

# Option B: Pass as Java system properties
java -jar target/spring-boot-ibm-mq-1.0.0.jar -Dmq.user=app -Dmq.password=passw0rd

# Option C: Use Spring Boot properties file
java -jar target/spring-boot-ibm-mq-1.0.0.jar --spring.config.location=file:./application-dev.properties
```

---

## Step 4: Run the Application

### Option 1: Direct JAR Execution ⭐ RECOMMENDED

```powershell
cd "d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq"

# Run with default configuration
java -jar target/spring-boot-ibm-mq-1.0.0.jar

# Or with custom profile
java -jar target/spring-boot-ibm-mq-1.0.0.jar --spring.profiles.active=dev

# Or with custom port
java -jar target/spring-boot-ibm-mq-1.0.0.jar --server.port=8080
```

**Expected Output:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_|\__, | / / / /
 =========|_|==============|___/=/_/_/_/

2026-04-26 10:15:23 - Starting Spring Boot Application
2026-04-26 10:15:24 - Initializing IBM MQ Connection Factory
2026-04-26 10:15:25 - IBM MQ Connection Factory initialized successfully
2026-04-26 10:15:25 - Started Application in 2.123 seconds (JVM running for 2.456s)
```

### Option 2: Maven Spring Boot Plugin

```powershell
cd "d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq"
mvn spring-boot:run
```

### Option 3: Using PowerShell Script (if available)

```powershell
cd "d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq"
.\quick-start.ps1
```

---

## Step 5: Validate Application is Running

### Check Application Health

```powershell
# Check if application is responding
Invoke-WebRequest -Uri "http://localhost:8081/api/health" -Method Get

# Or using curl
curl http://localhost:8081/api/health
```

**Expected Response:**
```json
{
  "status": "UP"
}
```

### Check IBM MQ Connection

Monitor application logs for successful connection:

```
INFO  - Initializing IBM MQ Connection Factory
DEBUG - Queue Manager: QM1, Channel: DEV.APP.SVRCONN, Connection: localhost(1414)
INFO  - IBM MQ Connection Factory initialized successfully
DEBUG - CredentialAwareConnectionFactory initialized with user: app
DEBUG - Creating connection with credentials for user: app
```

### Test Message Publishing

```powershell
# Publish a test message
$uri = "http://localhost:8081/api/messages/publish"
$body = @{
    queue = "DEV.QUEUE.1"
    message = "Test Message $(Get-Date)"
    messageType = "TEST"
} | ConvertTo-Json

Invoke-WebRequest -Uri $uri -Method Post -Body $body -ContentType "application/json"
```

---

## Step 6: Monitor Application

### View Real-Time Logs

```powershell
# Follow application output
# The console will show all logs in real-time
```

### Check Active Connections

```powershell
# Query IBM MQ running instances
docker-compose exec ibm-mq runmqsc QM1 << EOF
DISPLAY CONN(*)
EOF
```

---

## Troubleshooting

### ❌ Application fails to connect to MQ

**Error**: `JMSWMQ0018: Failed to connect to queue manager...`

**Solutions:**
1. Verify IBM MQ container is running:
   ```powershell
   docker-compose ps
   ```

2. Check MQ is listening on port 1414:
   ```powershell
   netstat -an | findstr 1414
   ```

3. Verify credentials match MQ setup:
   - Username: `app`
   - Password: `passw0rd`

### ❌ Port 8081 already in use

**Solution:**
```powershell
# Run on different port
java -jar target/spring-boot-ibm-mq-1.0.0.jar --server.port=8082
```

### ❌ Build fails with Maven

**Solution:**
```powershell
# Clear Maven cache and retry
mvn clean install -U -DskipTests
```

### ❌ Can't find credentials at runtime

**Check:**
1. Verify application.yml contains credentials:
   ```yaml
   mq:
     user: app
     password: passw0rd
   ```

2. Verify CredentialAwareConnectionFactory is being used

3. Check logs show: "Creating connection with credentials for user: app"

---

## API Endpoints

Once running, access these endpoints:

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/messages/publish` | POST | Publish message to queue |
| `/api/messages/consume` | GET | Consume message from queue |
| `/api/queues/status` | GET | Get queue status |
| `/api/queues/depth` | GET | Get queue message depth |
| `/api/health` | GET | Health check |

---

## Environment Variables Reference

```powershell
# Core MQ Settings
MQ_USER=app
MQ_PASSWORD=passw0rd
MQ_CONNECTION_NAME=localhost(1414)
MQ_QUEUE_MANAGER=QM1
MQ_CHANNEL=DEV.APP.SVRCONN

# Application Settings
SERVER_PORT=8081
LOGGING_LEVEL_COM_EXAMPLE_IBMMQ=DEBUG
```

---

## Success Validation Checklist

- ✅ IBM MQ container running and listening on port 1414
- ✅ Maven build successful (BUILD SUCCESS)
- ✅ Spring Boot application started without errors
- ✅ Connection to IBM MQ established with credentials
- ✅ Health endpoint responds with "UP" status
- ✅ Can publish messages to DEV.QUEUE.1
- ✅ Logs show "Creating connection with credentials for user: app"

**If all items above are checked ✅, your setup is complete and working!**

---
