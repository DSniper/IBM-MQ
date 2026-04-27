# IBM MQ Configuration & Port Setup Guide

## ⚙️ IBM MQ on Docker Configuration

### Container Details

The `docker-compose.yml` file spins up a complete IBM MQ instance with the following configuration:

```yaml
Image: icr.io/ibm-messaging/mq:latest
Container: ibm-mq-container
```

### Port Mappings

| Service | Port | Protocol | Purpose |
|---------|------|----------|---------|
| **MQ Listener** | 1414 | TCP | Client connections (Spring Boot application connects here) |
| **MQ Console** | 9443 | HTTPS | Web management console (IBM MQ UI) |
| **MQ Console** | 9080 | HTTP | Web management console alternative |

### Important: Port Access

When running:

1. **Local Development:**
   - Spring Boot App connects to: `localhost:1414`
   - MQ Console accessible at: `https://localhost:9443`

2. **Docker Network:**
   - Container name: `ibm-mq-container`
   - Internal network: `mq-network`
   - Other containers access via: `ibm-mq-container:1414`

3. **Remote/Cloud:**
   - Replace `localhost` with server IP/hostname
   - Example: `mq-server.example.com(1414)`

---

## 🔐 Default Credentials

| Property | Value |
|----------|-------|
| Queue Manager Name | `QM1` |
| Username | `app` |
| Password | `passw0rd` |
| Channel | `DEV.APP.SVRCONN` |

---

## 🚀 Quick Start - Port Verification

```bash
# 1. Start MQ container
docker-compose up -d

# 2. Verify ports are listening
netstat -tuln | grep -E "1414|9080|9443"

# 3. Test MQ Listener port
telnet localhost 1414

# 4. Test MQ Console
curl -k https://localhost:9443/ibm/console

# 5. Check container logs
docker logs ibm-mq-container

# 6. Get container IP
docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' ibm-mq-container
```

---

## 📋 Default Queue Names

The following queues are automatically created in the MQ instance:

```
DEV.QUEUE.1       - Main request queue
DEV.QUEUE.2       - Response queue
DEV.QUEUE.ERROR   - Error handling queue
DEV.DLQ           - Dead Letter Queue
```

You can add more queues via MQ Console or through MQ scripts.

---

## 🌐 Access MQ Console

### HTTPS (Recommended)
```
URL: https://localhost:9443/ibm/console
Username: app
Password: passw0rd
```

### HTTP (Alternative)
```
URL: http://localhost:9080/ibm/console
Username: app
Password: passw0rd
```

**Note:** The browser will show SSL warning for HTTPS - this is normal for self-signed certificates. Click "Advanced" and proceed.

---

## 🔄 Connection String Examples

### Local Development
```
Connection Name: localhost(1414)
Channel: DEV.APP.SVRCONN
User: app
Password: passw0rd
```

### Docker Network (container to container)
```
Connection Name: ibm-mq-container(1414)
Channel: DEV.APP.SVRCONN
User: app
Password: passw0rd
```

### Remote Server
```
Connection Name: mq-server.example.com(1414)
Channel: CLOUD.APP.SVRCONN
User: cloud-user
Password: cloud-password
```

### AWS MQ or Managed Service
```
Connection Name: mq-broker-xxx.mq.region.amazonaws.com(1414)
Channel: DEV.APP.SVRCONN
User: admin
Password: generated-password
```

---

## 🔧 Environment-Specific Configuration

### Development (`application.yml`)
```yaml
mq.connection-name: localhost(1414)
mq.user: app
mq.password: passw0rd
server.port: 8080
```

### Production (`application-prod.yml`)
```yaml
mq.connection-name: ${MQ_CONNECTION_NAME}  # e.g., mq.example.com(1414)
mq.user: ${MQ_USER}
mq.password: ${MQ_PASSWORD}
server.port: 8080
```

**Set environment variables:**
```bash
export MQ_CONNECTION_NAME="mq-server.example.com(1414)"
export MQ_USER="prod-user"
export MQ_PASSWORD="prod-password"
java -jar app.jar --spring.profiles.active=prod
```

---

## 📊 Monitoring Ports

### Check if MQ is Running

**Windows:**
```powershell
netstat -ano | findstr ":1414"
netstat -ano | findstr ":9443"
```

**Linux/Mac:**
```bash
netstat -tuln | grep -E "1414|9443"
# or
ss -tuln | grep -E "1414|9443"
```

**Docker:**
```bash
docker port ibm-mq-container
```

---

## 🔌 Firewall Rules (if needed)

If using firewall, allow these inbound ports:

```bash
# Linux (ufw)
sudo ufw allow 1414/tcp
sudo ufw allow 9080/tcp
sudo ufw allow 9443/tcp

# Linux (firewall-cmd)
sudo firewall-cmd --permanent --add-port=1414/tcp
sudo firewall-cmd --permanent --add-port=9080/tcp
sudo firewall-cmd --permanent --add-port=9443/tcp
sudo firewall-cmd --reload

# Windows (PowerShell as Admin)
netsh advfirewall firewall add rule name="MQ-1414" dir=in action=allow protocol=tcp localport=1414
netsh advfirewall firewall add rule name="MQ-9080" dir=in action=allow protocol=tcp localport=9080
netsh advfirewall firewall add rule name="MQ-9443" dir=in action=allow protocol=tcp localport=9443
```

---

## 🆘 Troubleshooting Port Issues

### Port Already in Use

```bash
# Find what's using the port
lsof -i :1414        # Mac/Linux
netstat -ano | findstr :1414  # Windows

# Kill the process (get PID from above)
kill -9 <PID>         # Mac/Linux
taskkill /PID <PID> /F  # Windows
```

### Connection Timeout

```bash
# Test connectivity
telnet localhost 1414

# Check if MQ is actually running
docker ps | grep ibm-mq

# View logs
docker logs ibm-mq-container

# Restart container
docker-compose restart ibm-mq
```

### Cannot Access Console

```bash
# Verify HTTPS is working
curl -k -v https://localhost:9443/ibm/console

# Check HTTP alternative
curl -v http://localhost:9080/ibm/console

# View MQ logs for errors
docker exec ibm-mq-container dspmq -m QM1
```

---

## 📈 Scaling Considerations

### Multiple MQ Instances

To run multiple MQ containers on different ports:

```yaml
# docker-compose.yml
services:
  ibm-mq-1:
    ports:
      - "1414:1414"
      - "9443:9443"
    environment:
      MQ_QMGR_NAME: QM1
  
  ibm-mq-2:
    ports:
      - "1415:1414"
      - "9444:9443"
    environment:
      MQ_QMGR_NAME: QM2
```

### Application Configuration for Multiple Queues

```yaml
# application.yml
mq:
  - name: primary
    connection-name: localhost(1414)
  - name: secondary
    connection-name: localhost(1415)
```

---

## 📝 Reference Summary

**Quick Commands:**
```bash
# Start MQ
docker-compose up -d

# Stop MQ
docker-compose down

# View running containers
docker-compose ps

# Check logs
docker-compose logs -f ibm-mq

# Test connectivity
telnet localhost 1414

# Access console
https://localhost:9443/ibm/console
```

**Key Ports:**
- **1414** - MQ Client connections (CRITICAL)
- **9443** - MQ Console HTTPS
- **9080** - MQ Console HTTP

**Default Credentials:**
- User: `app`
- Password: `passw0rd`

