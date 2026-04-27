# 🔓 IBM MQ Console - CORRECT ACCESS INSTRUCTIONS

## ✅ MQ Console IS RUNNING - Here's How to Access It

**The problem was**: Trying to access HTTP (9080) when it's only available on HTTPS (9443)

---

## 🌐 ACCESS IBM MQ CONSOLE

### **HTTPS Console (CORRECT WAY)**

**URL**: [https://localhost:9443/ibmmq/console](https://localhost:9443/ibmmq/console)

**Credentials**:
- **Username**: `admin`
- **Password**: `passw0rd`

### **Steps to Access**:

1. **Open your browser** (Chrome, Firefox, Edge, etc.)

2. **Go to**: `https://localhost:9443/ibmmq/console`

3. **You'll see a security warning** (this is normal - self-signed certificate)
   - Click "Advanced" or "Proceed anyway" to continue
   - This is safe - it's your local container

4. **Login with**:
   - Username: `admin`
   - Password: `passw0rd`

5. **You're in!** You'll now see:
   - Queue Manager status (QM1)
   - All queues (DEV.QUEUE.1, DEV.QUEUE.2, etc.)
   - Message counts
   - Connection details

---

## ⚠️ Why HTTP (9080) Doesn't Work

The IBM MQ container in this version **only enables HTTPS (port 9443)** for the web console. HTTP is not configured by default.

---

## 🔐 Why You See SSL Certificate Warning

This is **completely normal and safe**:
- The container uses a self-signed certificate
- Your browser warns you because it's not from a trusted authority
- Since it's your local container, you can safely bypass the warning
- Click "Advanced" → "Proceed" or "Accept Risk"

---

## 📱 Step-by-Step Browser Instructions

### **For Chrome/Edge**:
1. Go to: `https://localhost:9443/ibmmq/console`
2. See certificate warning
3. Click "Advanced"
4. Click "Proceed to localhost (unsafe)"
5. Login with admin / passw0rd

### **For Firefox**:
1. Go to: `https://localhost:9443/ibmmq/console`
2. Click "Advanced..."
3. Click "Accept the Risk and Continue"
4. Login with admin / passw0rd

### **For Safari**:
1. Go to: `https://localhost:9443/ibmmq/console`
2. Bypass the warning
3. Login with admin / passw0rd

---

## ✅ Verified Working

**From container logs:**
```
CWWKT0016I: Web application available (default_host): 
https://8d0b84c5d51b:9443/ibmmq/console/

CWWKO0219I: TCP Channel defaultHttpEndpoint-ssl has been started 
and is now listening for requests on host * (IPv6) port 9443.

CWWKF0011I: The mqweb server is ready to run a smarter planet. 
The mqweb server started in 6.642 seconds.
```

**Status**: ✅ **RUNNING**

---

## 🎯 What You Can Do in the Console

Once logged in, you can:
- ✅ View Queue Manager (QM1) status
- ✅ See all queues and their message counts
- ✅ Browse messages in queues
- ✅ Create/delete queues
- ✅ Monitor connections
- ✅ Check listener status

---

## 💡 Remember

| Item | Value |
|------|-------|
| **URL** | https://localhost:9443/ibmmq/console |
| **Username** | admin |
| **Password** | passw0rd |
| **Port** | 9443 (HTTPS only) |
| **Certificate** | Self-signed (bypass warning safely) |

---

## 🚀 Try It Now

**Open your browser and go to:**
```
https://localhost:9443/ibmmq/console
```

**Login with:**
- admin / passw0rd

**Done!** You should see the IBM MQ Console dashboard.

