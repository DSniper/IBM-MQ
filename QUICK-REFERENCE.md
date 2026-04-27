# 🚀 QUICK START GUIDE - IBM MQ Spring Boot Integration

## ✅ BUILD STATUS: SUCCESS

Your application has been **successfully built** and is ready to run! No compilation errors found.

---

## 📋 CREDENTIALS CONFIGURED ✅

| Component | Username | Password | Connection |
|-----------|----------|----------|------------|
| IBM MQ | `app` | `passw0rd` | localhost:1414 |
| Queue Manager | - | - | `QM1` |
| Channel | - | - | `DEV.APP.SVRCONN` |

---

## 🎯 FASTEST WAY TO RUN (Recommended)

### Option 1: Automated PowerShell Script ⭐ EASIEST

```powershell
cd "d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq"
.\start-app.ps1
```

**This script will automatically:**
1. ✅ Start IBM MQ Docker container
2. ✅ Wait for MQ to be ready
3. ✅ Build the application
4. ✅ Run the application with proper credentials

### Option 2: Manual Commands

```powershell
# Terminal 1: Start IBM MQ
cd "d:\Softwares\MQ\IBM MQ"
docker-compose up -d

# Wait 30-60 seconds for MQ to be ready...

# Terminal 2: Build and Run Application
cd "d:\Softwares\MQ\IBM MQ\spring-boot-ibm-mq"
mvn clean package -DskipTests
java -jar target/spring-boot-ibm-mq-1.0.0.jar
```

---

## ✅ VALIDATION

### 1. Check Application is Running

```powershell
Invoke-WebRequest http://localhost:8081/api/health
```

**Expected Response:** `{"status":"UP"}`

### 2. Run Validation Script

```powershell
.\validate-app.ps1
```

### 3. Check Application Logs

Look for these success messages:

```
✅ "Initializing IBM MQ Connection Factory"
✅ "IBM MQ Connection Factory initialized successfully"
✅ "Creating connection with credentials for user: app"
```

---

## 📊 FILES CREATED FOR YOU

| File | Purpose |
|------|---------|
| `.env` | Environment variables configuration |
| `application-dev.properties` | Development profile properties |
| `RUN-APPLICATION.md` | Detailed step-by-step guide |
| `start-app.ps1` | Automated startup script |
| `validate-app.ps1` | Health check script |

---

## 🔧 HOW CREDENTIALS ARE HANDLED

The application uses **`CredentialAwareConnectionFactory`** to inject credentials:

1. **Configuration Source**: `application.yml`
   ```yaml
   mq:
     user: app
     password: passw0rd
   ```

2. **Runtime Injection**: `CredentialAwareConnectionFactory.java`
   - Wraps the MQ connection factory
   - Automatically provides credentials when creating connections
   - Logs all connection attempts (check logs for "Creating connection with credentials...")

3. **Connection Sequence**:
   ```
   Spring Boot Startup
     ↓
   Load application.yml credentials
     ↓
   Create MQConfiguration bean
     ↓
   Wrap with CredentialAwareConnectionFactory
     ↓
   Inject credentials on every connection
     ↓
   Connect to IBM MQ with user: "app", password: "passw0rd"
   ```

---

## 🛑 TROUBLESHOOTING

### ❌ "Failed to connect to queue manager"

```powershell
# 1. Check if MQ is running
docker-compose ps

# 2. Check if port 1414 is open
netstat -an | findstr 1414

# 3. Restart MQ
docker-compose restart

# 4. Check MQ logs
docker-compose logs ibm-mq
```

### ❌ "Port 8081 already in use"

```powershell
# Run on different port
java -jar target/spring-boot-ibm-mq-1.0.0.jar --server.port=8082
```

### ❌ "Build fails"

```powershell
# Clean Maven cache
mvn clean
mvn package -DskipTests
```

### ❌ Can't find credentials

**Check these files:**
- `src/main/resources/application.yml` - Contains credentials
- `src/main/java/com/example/ibmmq/config/MQConfiguration.java` - Loads credentials
- `src/main/java/com/example/ibmmq/config/CredentialAwareConnectionFactory.java` - Injects credentials

---

## 🎨 COMMON OPERATIONS

### Publish a Message

```powershell
$uri = "http://localhost:8081/api/messages/publish"
$body = @{
    queue = "DEV.QUEUE.1"
    message = "Hello from Spring Boot!"
    messageType = "TEST"
} | ConvertTo-Json

Invoke-WebRequest -Uri $uri -Method Post -Body $body -ContentType "application/json"
```

### Check Queue Status

```powershell
Invoke-WebRequest http://localhost:8081/api/queues/status?queue=DEV.QUEUE.1
```

### Stop the Application

```
Press Ctrl+C in the terminal running the application
```

### Stop IBM MQ

```powershell
cd "d:\Softwares\MQ\IBM MQ"
docker-compose down
```

---

## 📚 DETAILED GUIDES

- **Full Setup Instructions**: See `RUN-APPLICATION.md`
- **Environment Configuration**: See `ENV-CONFIGURATION-EXAMPLES.md`
- **Getting Started**: See `GETTING-STARTED.md`

---

## ✅ VALIDATION CHECKLIST

Before reporting any issues, verify:

- [ ] Build is successful: `mvn clean package -DskipTests` shows "BUILD SUCCESS"
- [ ] IBM MQ container is running: `docker-compose ps` shows container as "Up"
- [ ] Application starts without errors
- [ ] Health endpoint responds: `http://localhost:8081/api/health` returns UP
- [ ] Logs show credentials being injected: "Creating connection with credentials for user: app"
- [ ] Can connect to IBM MQ on port 1414

**If all checks pass ✅, your setup is complete and working!**

---

## 📞 SUPPORT

If you still have issues:

1. Check the application logs for specific error messages
2. Review `RUN-APPLICATION.md` for detailed troubleshooting
3. Ensure Docker is running: `docker ps`
4. Ensure Java is installed: `java -version`
5. Ensure Maven is installed: `mvn -version`

---

**Status: ✅ READY TO RUN** | Last Updated: 2026-04-26
