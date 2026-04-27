# Cloud Deployment Guide

Complete guide for deploying the Spring Boot IBM MQ application to cloud platforms.

## Table of Contents

1. [AWS Elastic Beanstalk](#aws-elastic-beanstalk)
2. [Azure App Service](#azure-app-service)
3. [Google Cloud Run](#google-cloud-run)
4. [Kubernetes](#kubernetes)
5. [Heroku](#heroku)
6. [Environment Variables](#environment-variables)

---

## AWS Elastic Beanstalk

### Prerequisites
- AWS account
- AWS CLI configured
- EB CLI installed

### Deployment Steps

1. **Initialize Elastic Beanstalk**
   ```bash
   eb init -p java-17 spring-boot-mq
   ```

2. **Create `.ebextensions/config.yml`**
   ```yaml
   option_settings:
     aws:elasticbeanstalk:cloudwatch:logs:
       StreamLogs: true
       DeleteOnTerminate: false
       RetentionInDays: 7
     
     aws:elasticbeanstalk:application:environment:
       JAVA_OPTS: "-Xmx512m -Xms256m -XX:+UseG1GC"
       SPRING_PROFILES_ACTIVE: prod
   
   files:
     "/etc/config.d/01_env_vars.sh":
       mode: "000755"
       owner: root
       group: root
       content: |
         #!/bin/bash
         export MQ_CONNECTION_NAME="${MQ_CONNECTION_NAME}"
         export MQ_USER="${MQ_USER}"
         export MQ_PASSWORD="${MQ_PASSWORD}"
   ```

3. **Create Elastic Beanstalk environment**
   ```bash
   eb create spring-mq-prod --instance-type t3.medium
   ```

4. **Set environment variables**
   ```bash
   eb setenv MQ_CONNECTION_NAME="mq.example.com(1414)"
   eb setenv MQ_USER="prod-user"
   eb setenv MQ_PASSWORD="prod-password"
   ```

5. **Deploy application**
   ```bash
   mvn clean package
   eb deploy
   ```

6. **Access application**
   ```bash
   eb open
   ```

### Configuration Reference
- Instance Type: `t3.medium` or higher
- Platform: Java 17 running on 64bit Amazon Linux 2
- Load Balancer: Application Load Balancer (ALB)

---

## Azure App Service

### Prerequisites
- Azure account
- Azure CLI installed
- Resource group created

### Deployment Steps

1. **Create resource group**
   ```bash
   az group create --name spring-mq-group --location eastus
   ```

2. **Create App Service plan**
   ```bash
   az appservice plan create \
     --name spring-mq-plan \
     --resource-group spring-mq-group \
     --sku B2 \
     --is-linux
   ```

3. **Create web app**
   ```bash
   az webapp create \
     --resource-group spring-mq-group \
     --plan spring-mq-plan \
     --name spring-mq-app \
     --runtime "java|17-java17"
   ```

4. **Configure application settings**
   ```bash
   az webapp config appsettings set \
     --resource-group spring-mq-group \
     --name spring-mq-app \
     --settings \
       MQ_CONNECTION_NAME="mq.example.com(1414)" \
       MQ_USER="prod-user" \
       MQ_PASSWORD="prod-password" \
       SPRING_PROFILES_ACTIVE="prod"
   ```

5. **Deploy application**
   ```bash
   # Create deployment package
   mvn clean package
   
   # Deploy JAR
   az webapp deployment source config-zip \
     --resource-group spring-mq-group \
     --name spring-mq-app \
     --src target/spring-boot-ibm-mq-1.0.0.jar
   ```

6. **Access application**
   ```
   https://spring-mq-app.azurewebsites.net/api/v1/messages/health
   ```

### Configuration
- App Service Plan: B2 or higher
- Runtime: Java 17
- Region: Select based on your location

---

## Google Cloud Run

### Prerequisites
- Google Cloud account
- gcloud CLI installed
- Container registry enabled

### Deployment Steps

1. **Build Docker image**
   ```bash
   mvn clean package
   docker build -t spring-boot-mq:latest .
   ```

2. **Tag image for GCP**
   ```bash
   docker tag spring-boot-mq:latest \
     gcr.io/PROJECT_ID/spring-boot-mq:latest
   ```

3. **Push to Container Registry**
   ```bash
   gcloud auth configure-docker
   docker push gcr.io/PROJECT_ID/spring-boot-mq:latest
   ```

4. **Deploy to Cloud Run**
   ```bash
   gcloud run deploy spring-boot-mq \
     --image gcr.io/PROJECT_ID/spring-boot-mq:latest \
     --platform managed \
     --region us-central1 \
     --memory 512Mi \
     --set-env-vars \
       MQ_CONNECTION_NAME="mq.example.com(1414)",\
       MQ_USER="prod-user",\
       MQ_PASSWORD="prod-password",\
       SPRING_PROFILES_ACTIVE="prod" \
     --allow-unauthenticated
   ```

5. **Access application**
   ```
   Cloud Run will provide a URL:
   https://spring-boot-mq-xxxxx-uc.a.run.app/api/v1/messages/health
   ```

### Configuration
- Memory: 512MB or higher
- CPU: 1
- Timeout: 300 seconds
- Max instances: Auto-scale

---

## Kubernetes Deployment

### Prerequisites
- Kubernetes cluster running
- kubectl configured
- Docker image pushed to registry

### Create Deployment Files

1. **Create `k8s-deployment.yaml`**
   ```yaml
   apiVersion: apps/v1
   kind: Deployment
   metadata:
     name: spring-boot-mq-deployment
     namespace: production
   spec:
     replicas: 2
     selector:
       matchLabels:
         app: spring-boot-mq
     template:
       metadata:
         labels:
           app: spring-boot-mq
       spec:
         containers:
         - name: spring-boot-mq
           image: your-registry/spring-boot-mq:latest
           imagePullPolicy: Always
           ports:
           - containerPort: 8080
           env:
           - name: SPRING_PROFILES_ACTIVE
             value: "prod"
           - name: MQ_CONNECTION_NAME
             valueFrom:
               configMapKeyRef:
                 name: mq-config
                 key: connection-name
           - name: MQ_USER
             valueFrom:
               secretKeyRef:
                 name: mq-secrets
                 key: username
           - name: MQ_PASSWORD
             valueFrom:
               secretKeyRef:
                 name: mq-secrets
                 key: password
           - name: JAVA_OPTS
             value: "-Xmx512m -Xms256m"
           resources:
             requests:
               memory: "256Mi"
               cpu: "250m"
             limits:
               memory: "512Mi"
               cpu: "500m"
           livenessProbe:
             httpGet:
               path: /api/v1/messages/health
               port: 8080
             initialDelaySeconds: 30
             periodSeconds: 10
           readinessProbe:
             httpGet:
               path: /api/v1/messages/health
               port: 8080
             initialDelaySeconds: 5
             periodSeconds: 5
   ---
   apiVersion: v1
   kind: Service
   metadata:
     name: spring-boot-mq-service
     namespace: production
   spec:
     type: LoadBalancer
     ports:
     - port: 80
       targetPort: 8080
       protocol: TCP
     selector:
       app: spring-boot-mq
   ```

2. **Create ConfigMap**
   ```bash
   kubectl create configmap mq-config \
     --from-literal=connection-name="mq.example.com(1414)" \
     -n production
   ```

3. **Create Secret**
   ```bash
   kubectl create secret generic mq-secrets \
     --from-literal=username="prod-user" \
     --from-literal=password="prod-password" \
     -n production
   ```

4. **Deploy to Kubernetes**
   ```bash
   kubectl apply -f k8s-deployment.yaml
   ```

5. **Check deployment status**
   ```bash
   kubectl get pods -n production
   kubectl get svc -n production
   ```

6. **Access application**
   ```bash
   # Get external IP
   kubectl get svc spring-boot-mq-service -n production
   
   # Access via: http://<EXTERNAL-IP>/api/v1/messages/health
   ```

---

## Heroku Deployment

### Prerequisites
- Heroku account
- Heroku CLI installed
- Git repository initialized

### Deployment Steps

1. **Create `Procfile`**
   ```
   web: java -Xmx512m -Xms256m -XX:+UseG1GC \
        -Dspring.profiles.active=prod \
        -jar target/spring-boot-ibm-mq-1.0.0.jar
   ```

2. **Create `system.properties`**
   ```
   java.runtime.version=17
   ```

3. **Login to Heroku**
   ```bash
   heroku login
   ```

4. **Create Heroku application**
   ```bash
   heroku create spring-boot-mq-app
   ```

5. **Set environment variables**
   ```bash
   heroku config:set \
     MQ_CONNECTION_NAME="mq.example.com(1414)" \
     MQ_USER="prod-user" \
     MQ_PASSWORD="prod-password" \
     SPRING_PROFILES_ACTIVE="prod"
   ```

6. **Deploy application**
   ```bash
   git push heroku main
   ```

7. **View logs**
   ```bash
   heroku logs --tail
   ```

8. **Access application**
   ```
   https://spring-boot-mq-app.herokuapp.com/api/v1/messages/health
   ```

---

## Environment Variables

### Required Variables

| Variable | Description | Example |
|----------|-------------|---------|
| `MQ_CONNECTION_NAME` | MQ Server address and port | `mq.example.com(1414)` |
| `MQ_USER` | MQ authentication user | `prod-user` |
| `MQ_PASSWORD` | MQ authentication password | `secure-password` |
| `SPRING_PROFILES_ACTIVE` | Spring profile | `prod` |

### Optional Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `MQ_QUEUE_MANAGER` | Queue Manager name | `QM1` |
| `MQ_CHANNEL` | MQ Channel | `DEV.APP.SVRCONN` |
| `MQ_CONNECTION_POOL_SIZE` | Connection pool size | `20` |
| `JAVA_OPTS` | JVM options | `-Xmx512m -Xms256m` |

### Setting Environment Variables

**AWS Elastic Beanstalk:**
```bash
eb setenv VAR_NAME="value"
```

**Azure App Service:**
```bash
az webapp config appsettings set --settings VAR_NAME="value"
```

**Google Cloud Run:**
```bash
gcloud run services update service-name --set-env-vars VAR_NAME="value"
```

**Kubernetes:**
```bash
kubectl set env deployment/app-deployment VAR_NAME="value"
```

**Heroku:**
```bash
heroku config:set VAR_NAME="value"
```

---

## Monitoring & Logging

### Health Check Endpoint

All platforms can use this health check endpoint:
```
GET /api/v1/messages/health
```

### Logging

Logs are available at:
- **Local**: `logs/application.log`
- **Cloud**: Platform-specific log aggregation

### Metrics

Prometheus metrics available at:
```
GET /actuator/metrics
```

---

## Scaling Considerations

1. **Connection Pooling**: Adjust `mq.connection-pool-size` for high throughput
2. **Message Timeout**: Increase `app.message.timeout` for slow networks
3. **Concurrency**: Adjust `spring.jms.listener.concurrency` for load
4. **Memory**: Start with 512MB, increase based on message volume

---

## Security Best Practices

1. **Never hardcode credentials** - Use environment variables/secrets management
2. **Use HTTPS** - Enable SSL/TLS in production
3. **Network isolation** - Run MQ and app in VPC/private network
4. **Authentication** - Use strong passwords for MQ
5. **Monitoring** - Enable application monitoring and alerting
6. **Backup** - Regular backups of MQ data and configuration

